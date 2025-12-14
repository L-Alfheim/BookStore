package com.twt.bookstore.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.twt.bookstore.exception.SqlException;
import com.twt.bookstore.poju.Order;
import com.twt.bookstore.poju.OrderDetail;

/**
 * 订单数据访apper的实现类
 * 需要手动回滚
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    // 获取 sqlSessionTemplate
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    // 命名空间
    private static final String NAMESPACE = "com.twt.bookstore.mapper.OrderRepository.";


    /**
     * 插入新的订单主记录
     * @param order 订单实体对象
     * @return int 受影响的行数，通常为 1
     * @throws SqlException 202 数据库错误
     */
    @Override
    public int insertOrder(Order order) throws SqlException {
        try {
            // 使用 insert 方法
            return sqlSessionTemplate.insert(NAMESPACE + "insertOrder", order);
        } catch (Exception e) {
            throw new SqlException(202, "errors occurs when insertOrder", e);
        }
    }

    /**
     * 根据订单号查询订单明细
     * @param orderId 订单主键ID
     * @return 包含明细列表的 {@link Order} 对象
     * @throws SqlException 202 数据库错误
     */
    @Override
    public Order queryOrderByIdTime(String orderIdTime) throws SqlException {
        try {
            return sqlSessionTemplate.selectOne(NAMESPACE + "queryOrderByIdTime", orderIdTime);
        } catch (Exception e) {
            throw new SqlException(202, "errors occurs when queryOrderByIdTime, IdTime: " + orderIdTime, e);
        }
    }

    /**
     * 根据用户ID查询该用户的所有订单记录。
     * @param userId 所属用户的主键ID
     * @return List<Order> 订单列表，没有则返回null
     * @throws SqlException 202 数据库错误
     */
    @Override
    public List<Order> queryOrdersByUserId(Long userId) throws SqlException {
        try {
            return sqlSessionTemplate.selectOne(NAMESPACE + "queryOrdersByUserId", userId);
        } catch (Exception e) {
            throw new SqlException(202, "errors occurs when queryOrdersByUserId, userId: " + userId, e);
        }
    }

    /**
     * 分页查询用户订单
     * @param offset 偏移数量
     * @param limit 页面大小
     * @return List<Order> 订单列表，如果没有则返回空列表
     * @throws SqlException 202 数据库错误
     */
    @Override
    public List<Order> queryOrdersWithDetailsForPage(int offset, int limit) throws SqlException {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("offset", offset);
            params.put("limit", limit);
            return sqlSessionTemplate.selectList(NAMESPACE + "queryOrdersForPage", params);
        } catch (Exception e) {
            throw new SqlException(202, "errors occurs when queryOrdersWithDetailsForPage", e);
        }
    }

    /**
     * 查询订单总数
     * @param userId 所属用户的主键id
     * @return 订单总数
     * @throws SqlException 202 数据库错误
     */
    @Override
    public Long countOrders(Long userId) throws SqlException {
        try {
            return sqlSessionTemplate.selectOne(NAMESPACE + "countOrders", userId);
        } catch (Exception e) {
            throw new SqlException(202, "errors occurs when countOrders", e);
        }
    }

    /**
     * 插入订单明细记录
     * @param details List<OrderDetail> 订单明细列表
     * @return int 受影响的行数
     * @throws SqlException 202 数据库错误
     */
    @Override
    public int insertOrderDetails(List<OrderDetail> details) throws SqlException {
        try {
            return sqlSessionTemplate.insert(NAMESPACE + "insertOrderDetails", details);
        } catch (Exception e) {
            throw new SqlException(202, "errors occurs when insertOrderDetails", e);
        }
    }
}