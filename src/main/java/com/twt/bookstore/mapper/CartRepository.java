package com.twt.bookstore.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twt.bookstore.poju.Cart;

public interface CartRepository {

    /**
     * 查询购物车的指定商品
     * @param userId 所属用户id
     * @param bookId 书籍id
     * @return 匹配的购物车记录 {@link Cart} 对象，如果不存在或已删除则返回 {@code null}
     */
    Cart queryCartItemByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);

    /**
     * 查询购物车
     * @param userId 所属用户id
     * @return 该用户所有未删除的购物车记录 {@link Cart} 对象列表，如果没有则返回空列表
     */
    List<Cart> queryCartsByUserId(Long userId);

    /**
     * 添加商品
     * @param cart Cart对象
     * @return int 影响的行数
     */
    int insertCartItem(Cart cart);

    /**
     * 软删除购物车
     * @param id 购物车记录的主键ID
     * @return int 影响的行数
     */
    int deleteCartItemById(Long id);
    
    /**
     * 软删除商品
     * @param userId 所属用户id
     * @param bookId 书籍id
     * @return int 受影响的行数
     */
    int deleteCartItemByUserIdAndBookId(Long userId, Long bookId);

    /**
     * 更新购物车商品数量
     * 当购物车已有相同商品时应该使用该方法
     * @param id cart_id
     * @param itemCount 数量
     * @return 影响行数
     */
    int updateCartItemCountById(Long id, Integer itemCount);

    /**
     * 删除购物车所有商品
     * @param uesrId 用户主键id
     * @return 收影响的行数
     */
    int deleteAllItemByPersonId(Long userId);
}