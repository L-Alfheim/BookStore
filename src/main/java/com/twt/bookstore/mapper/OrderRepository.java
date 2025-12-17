package com.twt.bookstore.mapper;

import java.util.List;

import com.twt.bookstore.poju.Order;
import com.twt.bookstore.poju.OrderDetail;

/**
 * 订单及订单明细数据访问接口
 * 负责对数据库中 {@code orders} 和 {@code order_detail} 表进行操作。
 */
public interface OrderRepository {

    // --- 订单主表操作 ---

    /**
     * 插入新的订单主记录，并回写生成的主键ID。
     * @param order {@link Order}订单实体对象
     * @return int 受影响的行数，通常为 1
     */
    int insertOrder(Order order);

    /**
     * 根据订单号查询订单明细
     * @param orderId 订单主键ID
     * @return 包含明细列表的 {@link Order} 对象
     */
    Order queryOrderByIdTime(String orderIdTime);

    /**
     * 根据用户ID查询该用户的所有订单主记录（不包含明细）。
     * @param userId 所属用户的主键ID
     * @return List<Order> 订单列表，没有则返回null
     */
    List<Order> queryOrdersByUserId(Long userId);

    /**
     * 分页查询用户订单
     * 不包括明细
     * @param offset 偏移数量
     * @param limit 页面大小
     * @return 订单列表，如果没有则返回空列表
     */
    List<Order> queryOrdersWithDetailsForPage(int offset, int limit);
                                                      
    /**
     * 查询订单总数
     * @param userId 所属用户的主键id
     * @return 订单总数
     */
    Long countOrders(Long userId);

    
    // --- 订单明细表操作 ---

    /**
     * 批量插入订单明细记录。
     * @param details 订单明细列表
     * @return int 受影响的总行数
     */
    int insertOrderDetails(List<OrderDetail> details);
}