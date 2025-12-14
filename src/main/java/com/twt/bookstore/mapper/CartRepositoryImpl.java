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
     * @throws SqlException 202 需要手动回滚
     */
    @Override
    public Cart queryCartItemByUserIdAndBookId(Long userId, Long bookId) throws SqlException{
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("bookId", bookId);
            return sqlSessionTemplate.selectOne(NAMESPACE + "queryCartItemByUserIdAndBookId", params);
        } catch (Exception e) {
            //查询出现错误
            throw new SqlException(202, "errors occurs when queryCartItemByUserIdAndBookId, userId: " + userId + "bookId: " + bookId, e);
        }
    }

    /**
     * 查询购物车
     * @param userId 所属用户id
     * @return 该用户所有未删除的购物车记录 {@link Cart} 对象列表，如果没有则返回空列表
     * @throws SqlException 202 需要手动回滚
     */
    @Override
    public List<Cart> queryCartsByUserId(Long userId) throws SqlException{
        try {
            return sqlSessionTemplate.selectList(NAMESPACE + "queryCartsByUserId", userId);
        } catch (Exception e) {
            throw new SqlException(202, "errors occurs when queryCartsByUserId, userId: " + userId, e);
        }

    }

    /**
     * 添加商品
     * @param cart Cart对象
     * @return int 影响的行数
     * @throws SqlException 202 需要手动回滚
     */
    @Override
    public int insertCartItem(Cart cart) throws SqlException{
        try {
            return sqlSessionTemplate.update(NAMESPACE + "insertCartItem", cart);
        } catch (Exception e) {
            throw new SqlException(202, "errors occurs when insertCartItem", e);
        }

    }

    /**
     * 软删除购物车
     * @param id 购物车记录的主键ID
     * @return int 影响的行数
     * @throws SqlException 202 需要手动回滚
     */
    @Override
    public int deleteCartItemById(Long id) throws SqlException {
        try{
            return sqlSessionTemplate.delete(NAMESPACE + "deleteCartItemById", id);
        } catch(Exception e) {
            throw new SqlException(202, "errors occurs when deleteCartItemById, id: " + id, e);
        }

    }
    
    /**
     * 软删除商品
     * @param userId 所属用户id
     * @param bookId 书籍id
     * @return int 受影响的行数
     * @throws SqlException 202 需要手动回滚
     */
    @Override
    public int deleteCartItemByUserIdAndBookId(Long userId, Long bookId) throws SqlException {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", userId);
            params.put("book_id", bookId);
            return sqlSessionTemplate.update(NAMESPACE + "deleteCartItemByUserIdAndBookId", params);
        } catch (Exception e) {
            throw new SqlException(202, "errors occurs when deleteCartItemByUserIdAndBookId", e);
        }

    }
}
