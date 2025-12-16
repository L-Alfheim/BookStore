package com.twt.bookstore.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.twt.bookstore.dto.request.CartAddDTO;
import com.twt.bookstore.dto.response.BaseResponse;
import com.twt.bookstore.dto.response.CartDTO;
import com.twt.bookstore.dto.userContext.UserContext;
import com.twt.bookstore.exception.ServiceException;
import com.twt.bookstore.mapper.BookRepository;
import com.twt.bookstore.mapper.CartRepository;
import com.twt.bookstore.mapper.UserRepository;
import com.twt.bookstore.poju.BookInfo;
import com.twt.bookstore.poju.Cart;

import lombok.RequiredArgsConstructor;

/**
 * 购物车服务类
 */
@Service
@RequiredArgsConstructor
public class CartService {
    
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;

    /**
     * 查询用户购物车的全部
     * @param userContext 用户身份
     * @return BaseResponse<List<CartDTO>>
     * @throws ServiceException 204
     */
    public BaseResponse<List<CartDTO>> queryCart(@AuthenticationPrincipal UserContext userContext) throws ServiceException {
        try {
            //查询用户id
            Long userId = userRepository.queryIdByUUID(userContext.uuid());
            if (userId == null) {
                return BaseResponse.error(404, "User not found");
            }
            
            //查询购物车
            List<Cart> cartResult = cartRepository.queryCartsByUserId(userId);
            if (cartResult.isEmpty()) {
                return BaseResponse.success(null);
            }

            //取不重复书籍id
            Set<Long> bookIds = cartResult.stream()
                    .map(Cart::getBookId)
                    .collect(Collectors.toSet());

            List<BookInfo> bookInfos = bookRepository.queryBookInfoByIds(bookIds);

            // 转换为Map去重
            Map<Long, BookInfo> bookInfoMap = bookInfos.stream()
                    .collect(Collectors.toMap(
                            BookInfo::getId, 
                            bookInfo -> bookInfo
                    ));
            
            // 数据拼接
            List<CartDTO> cartDTOList = cartResult.stream()
                .map(cart -> {
                    BookInfo bookInfo = bookInfoMap.get(cart.getBookId());
                    
                    if (bookInfo == null) {
                        return null; 
                    }
                    
                    // 7. 构造 CartDTO
                    return new CartDTO(
                                        bookInfo.getUuid(),
                                        bookInfo.getTitle(),
                                        bookInfo.getAuthor(),
                                        cart.getItemCount(),
                                        bookInfo.getPrice(),
                                        bookInfo.isAvailable()
                    );
                }).filter(dto -> dto != null) 
                  .collect(Collectors.toList());

                  return BaseResponse.success(cartDTOList);
        } catch (Exception e) {
            throw new ServiceException(204, "error occur in queryCart", e);
        }
    }

    /**
     * 向购物车添加商品
     * @param userContext 用户身份
     * @param addRequest 添加请求
     * @return
     * @throws ServiceException 204
     */
    public BaseResponse<Void> addOneItem(UserContext userContext, CartAddDTO addRequest) throws ServiceException {
        try {

            //查找人员主键id
            System.out.println("1");
            Long userId = userRepository.queryIdByUUID(userContext.uuid());
            if (userId == null) return BaseResponse.error(404, "User is not exsit");

            System.out.println("2");
            //查找书籍id
            Long bookId = bookRepository.queryIdByUUID(addRequest.uuid());
            if (bookId == null) return BaseResponse.error(404, "Book is not exsit");

            System.out.println("3");
            //查找是否已存在于购物车中
            Cart OldItem = cartRepository.queryCartItemByUserIdAndBookId(userId, bookId);
            if (OldItem == null) {
                //购物车没有该商品
                //构造Cart
                Cart newItem = new Cart(userId,
                                        bookId,
                                        addRequest.quantity(),
                                        Instant.now()
                );
    
                //插入数据
                cartRepository.insertCartItem(newItem);
            } else {
                //合并两者数量并更新
                System.out.println("4");
                cartRepository.updateCartItemCountById(OldItem.getId(), addRequest.quantity() + OldItem.getItemCount());
            }

            return BaseResponse.success();
        } catch (Exception e) {
            throw new ServiceException(204, "error occur when addCartItem", e);
        }
    }

    /**
     * 删除单个购物车商品
     * @param bookUuid 书籍UUID
     * @param userContext 用户身份
     * @return
     * @throws ServiceException 204
     */
    public BaseResponse<Void> deleteOneItem (UUID bookUuid, UserContext userContext) throws ServiceException{
        try {
            //查找人员主键id
            Long userId = userRepository.queryIdByUUID(userContext.uuid());
            if (userId == null) return BaseResponse.error(404, "User is not exsit");
    
            //查找所属购物车书籍主键id
            Long cartId = bookRepository.queryIdByUuidContainDelete(bookUuid);
            if (cartId == null) return BaseResponse.error(404, "Book is not exsit");

            //执行删除
            cartRepository.deleteCartItemById(cartId);
            return BaseResponse.success();
        } catch (Exception e) {
            throw new ServiceException(204, "error occur when deleteOneItem", e);
        }
    }
}
