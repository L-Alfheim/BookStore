package com.twt.bookstore.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.twt.bookstore.dto.request.OrderQueryDTO;
import com.twt.bookstore.dto.response.BaseResponse;
import com.twt.bookstore.dto.response.OrderDTO;
import com.twt.bookstore.dto.response.OrderDetailDTO;
import com.twt.bookstore.dto.userContext.UserContext;
import com.twt.bookstore.exception.ServiceException;
import com.twt.bookstore.mapper.BookRepository;
import com.twt.bookstore.mapper.CartRepository;
import com.twt.bookstore.mapper.OrderRepository;
import com.twt.bookstore.mapper.UserRepository;
import com.twt.bookstore.poju.BookInfo;
import com.twt.bookstore.poju.Cart;
import com.twt.bookstore.poju.Order;
import com.twt.bookstore.poju.OrderDetail;
import com.twt.bookstore.util.OrderIdGenerator;

import lombok.RequiredArgsConstructor;

/**
 * 这个类用于下单和查找订单的操作
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderIdGenerator orderIdGenerator;

    /**
     * 订单业务处理服务实现类.
     * <p>
     * 执行下单的完整闭环逻辑，包括：
     * 1. 校验购物车
     * 2. 批量校验商品状态与软删除标记
     * 3. 原子化扣减数据库库存
     * 4. 计算总价并持久化订单
     * 5. 清理已下单的购物车条目
     * </p>
     * @throws ServiceException 
     */

    //对任意异常都回滚
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Void> createOrder(UserContext userContext) throws ServiceException {
        //查询用户
        Long userId = userRepository.queryIdByUUID(userContext.uuid());
        if (userId == null) throw new ServiceException(101, "User is not exsit");


        //获取购物车商品
        List<Cart> cartItems = cartRepository.queryCartsByUserId(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new ServiceException(101, "The cart is empty");
        }

        //查询商品在售状态
        List<Long> bookIds = cartItems.stream().map(Cart::getBookId).collect(Collectors.toList());
        List<BookInfo> availableBooks = bookRepository.selectAvailableBooksByIds(bookIds);

        //校验在售数量
        if (availableBooks.size() != bookIds.size()) {
            throw new ServiceException(101, "Some books is not available, please check cart again");
        }

        BigDecimal totalPrice = BigDecimal.ZERO;

        //遍历购物车
        for (Cart item : cartItems) {
            //扣除库存
            int rows = bookRepository.decreaseStock(item.getBookId(), item.getItemCount());

            //查询具体书目
            UUID errorBook = bookRepository.queryUuidById(item.getBookId());
            if (rows == 0) {
                //扣除失败
                throw new ServiceException(101, "The book with UUID: " + errorBook + " is sold out");
            }

            // 计算该条目金额
            BookInfo book = availableBooks.stream()
                    //匹配书籍 获取第一个
                    .filter(b -> b.getId().equals(item.getBookId()))
                    .findFirst().get();
            totalPrice = totalPrice.add(book.getPrice().multiply(new BigDecimal(item.getItemCount())));
        }

        // 4. 生成订单主表记录
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderIdTime(Long.toString(orderIdGenerator.nextId()));
        order.setTotalPrice(totalPrice);
        orderRepository.insertOrder(order);

        //生成详细订单
        List<OrderDetail> details = cartItems.stream().map(item -> {
            OrderDetail detail = new OrderDetail();

            //获取订单主键id，书籍id，总数
            detail.setOrderId(order.getId());
            detail.setBookId(item.getBookId());
            detail.setQuantity(item.getItemCount());
            
            //匹配单价
            BigDecimal currentPrice = availableBooks.stream()
                    .filter(b -> b.getId().equals(item.getBookId()))
                    .findFirst()
                    .map(BookInfo::getPrice)
                    .orElseThrow(() -> new RuntimeException("error occur when generating order detail"));
                    
            detail.setPrice(currentPrice);
            return detail;
        }).collect(Collectors.toList());

        //插入订单详细
        int detailRows = orderRepository.insertOrderDetails(details);
        if (detailRows != details.size()) {
            throw new ServiceException(101,"error occur when generating order detail");
        }

        // 6. 清除购物车
        cartRepository.deleteAllItemByPersonId(userId);

        return BaseResponse.success("Order success", null);
    }


    //          用户查询接口


    /**
     * 用户查询
     * 分页查询订单主记录
     * 不包含明细
     *
     * @param userContext 用户身份
     * @param request OrderQueryDTO
     * @return BaseResponse<List<Order>>
     * @throws ServiceException 204
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<OrderDTO>> getOrdersByPage(UserContext userContext, OrderQueryDTO request) throws ServiceException {
        try {
            //查询用户主键id
            Long userId = userRepository.queryIdByUUID(userContext.uuid());
            if (userId == null) return BaseResponse.error(404, "User is not exsit");

            //分页查询
            int pageNum = (request.page() == null) ? 1 : request.page();
            int pageSize = (request.size() == null) ? 10 : request.size();
            PageHelper.startPage(pageNum, pageSize);
            List<Order> orders = orderRepository.queryOrdersByUserId(userId);

            if (orders == null || orders.isEmpty()) {
                return BaseResponse.success("No orders found", List.of());
            }

            // 构造DTO
            List<OrderDTO> result = orders.stream().map(order -> new OrderDTO(
                    order.getOrderIdTime(),
                    userContext.uuid(),
                    order.getTotalPrice(),
                    order.getCreateTime()
            )).collect(Collectors.toList());

            return BaseResponse.success(result);
        } catch (Exception e) {
            throw new ServiceException(204, "Error occurr when query orders for user: " + userContext.uuid(), e);
        }
    }

    /**
     * 用户查询
     * 根据订单编号查询订单明细
     *
     * @param orderIdTime 订单号
     * @return BaseResponse<Order>
     * @throws ServiceException 204
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<OrderDetailDTO>> getOrderDetails(UserContext userContext, String orderIdTime) throws ServiceException {
        try {
            //获取订单主表，带明细
            Order order = orderRepository.queryOrderByIdTime(orderIdTime);
            if (order == null || order.getOrderDetails() == null) {
                return BaseResponse.error(404, "Order or details not found");
            }

            //人员匹配
            Long userId = userRepository.queryIdByUUID(userContext.uuid());
            if  (!order.getUserId().equals(userId)) {
                return BaseResponse.error(403, "Access denied");
            }

            //构造id list
            List<Long> bookIds = order.getOrderDetails().stream()
                    .map(OrderDetail::getBookId)
                    .collect(Collectors.toList());
            //查询，包括已删除
            List<BookInfo> books = bookRepository.queryBookInfoByIdsContainDelete(bookIds);

            //构造DTO
            List<OrderDetailDTO> detailDTOs = order.getOrderDetails().stream().map(detail -> {
                BookInfo book = books.stream()
                        .filter(b -> b.getId().equals(detail.getBookId()))
                        .findFirst()
                        .orElse(new BookInfo());

                return new OrderDetailDTO(
                        book.getUuid(),
                        book.getTitle(),
                        detail.getQuantity(),
                        detail.getPrice()
                );
            }).collect(Collectors.toList());

            return BaseResponse.success(detailDTOs);
        } catch (Exception e) {
            throw new ServiceException(204, "Error occur in getOrderDetails", e);
        }
    }

    //           管理员查询接口

    /**
     * 分页查询所有订单主记录
     * 管理员
     * @param page 当前页码
     * @param size 每页展示数量
     * @return BaseResponse<List<OrderDTO>>
     * @throws ServiceException 204
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<OrderDTO>> getAllOrdersByPage (OrderQueryDTO request) throws ServiceException {
        try {
            //分页参数处理
            int pageNum = (request.page() == null) ? 1 : request.page();
            int pageSize = (request.size() == null) ? 10 : request.size();
            PageHelper.startPage(pageNum, pageSize);
            List<Order> orders = orderRepository.queryOrdersWithDetailsForPage(0, 0);

            if (orders == null || orders.isEmpty()) {
                return BaseResponse.success("No orders found in system", List.of());
            }

            //构造DTO
            List<OrderDTO> result = orders.stream().map(order -> {
                //查找用户uuid
                UUID userUuid = userRepository.queryUuidById(order.getUserId());
                
                return new OrderDTO(
                    order.getOrderIdTime(),
                    userUuid,
                    order.getTotalPrice(),
                    order.getCreateTime()
                );
            }).collect(Collectors.toList());

            return BaseResponse.success(result);
        } catch (Exception e) {
            throw new ServiceException(204, "Admin error: occurred when querying all orders", e);
        }
    }

    /**
     * 根据订单号订单明细
     * 管理员查询方法
     *
     * @param orderIdTime 订单业务编号（String 类型）
     * @return BaseResponse<List<OrderDetailDTO>>
     * @throws ServiceException 204
     */
    @Transactional(readOnly = true)
    public BaseResponse<List<OrderDetailDTO>> getAnyOrderDetails(String orderIdTime) throws ServiceException {
        try {
            // 获取订单主表及明细
            Order order = orderRepository.queryOrderByIdTime(orderIdTime);
            if (order == null || order.getOrderDetails() == null) {
                return BaseResponse.error(404, "Order not found with ID: " + orderIdTime);
            }

            //构造书籍主键id List
            List<Long> bookIds = order.getOrderDetails().stream()
                    .map(OrderDetail::getBookId)
                    .collect(Collectors.toList());
            
            //查询书籍信息
            List<BookInfo> books = bookRepository.queryBookInfoByIdsContainDelete(bookIds);

            //构造DTO
            List<OrderDetailDTO> detailDTOs = order.getOrderDetails().stream().map(detail -> {
                BookInfo book = books.stream()
                        .filter(b -> b.getId().equals(detail.getBookId()))
                        .findFirst()
                        .orElse(new BookInfo());

                return new OrderDetailDTO(
                        book.getUuid(),
                        book.getTitle(),
                        detail.getQuantity(),
                        detail.getPrice()
                );
            }).collect(Collectors.toList());

            return BaseResponse.success(detailDTOs);
        } catch (Exception e) {
            throw new ServiceException(204, "error occurred when getting order details: " + orderIdTime, e);
        }
    }
}