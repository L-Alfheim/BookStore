package com.twt.bookstore.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.twt.bookstore.exception.SqlException;
import com.twt.bookstore.poju.Cart;

/**
 * 购物车数据库mapper实现类
 * 需要手动回滚
 */
@Repository
public class CartRepositoryImpl implements CartRepository {

    //获取sqlSessionTemplate
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    //命名空间
    private static final String NAMESPACE = "com.twt.bookstore.mapper.CartRepository.";


    /**
     * 查询购物车的指定商品
     * @param userId 所属用户id
     * @param bookId 书籍id
     * @return 购物车单条记录 {@link Cart} 对象，不存在则返回 {@code null}
     */
    @Override
    public Cart queryCartItemByUserIdAndBookId(Long userId, Long bookId){
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("bookId", bookId);
        return sqlSessionTemplate.selectOne(NAMESPACE + "queryCartItemByUserIdAndBookId", params);
    }

    /**
     * 查询购物车
     * @param userId 所属用户id
     * @return 该用户所有未删除的购物车记录 {@link Cart} 对象列表，如果没有则返回空列表
     */
    @Override
    public List<Cart> queryCartsByUserId(Long userId){
        return sqlSessionTemplate.selectList(NAMESPACE + "queryCartsByUserId", userId);

    }

    /**
     * 添加商品
     * @param cart Cart对象
     * @return int 影响的行数
     */
    @Override
    public int insertCartItem(Cart cart){
        return sqlSessionTemplate.update(NAMESPACE + "insertCartItem", cart);

    }

    /**
     * 软删除购物车
     * @param id 购物车记录的主键ID
     * @return int 影响的行数
     */
    @Override
    public int deleteCartItemById(Long id) {
        return sqlSessionTemplate.update(NAMESPACE + "deleteCartItemById", id);

    }
    
    /**
     * 软删除商品
     * @param userId 所属用户id
     * @param bookId 书籍id
     * @return int 受影响的行数
     * @throws SqlException 202 需要手动回滚
     */
    @Override
    public int deleteCartItemByUserIdAndBookId(Long userId, Long bookId) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("book_id", bookId);
        return sqlSessionTemplate.update(NAMESPACE + "deleteCartItemByUserIdAndBookId", params);
    }

    /**
     * 更新购物车商品数量
     * 当购物车已有相同商品时应该使用该方法
     * @param id cart_id
     * @param itemCount 数量
     * @return 影响行数
     */
    @Override
    public int updateCartItemCountById(Long id, Integer itemCount) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("itemCount", itemCount); 

        return sqlSessionTemplate.update(NAMESPACE + "updateCartItemCountById", params);
    }

    /**
     * 删除购物车所有商品
     * @param uesrId 用户主键id
     * @return 收影响的行数
     */
    @Override
    public int deleteAllItemByPersonId(Long userId) {
        return sqlSessionTemplate.update(NAMESPACE + "deleteAllItemByPersonId", userId);
    }
}
