package com.twt.bookstore.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.twt.bookstore.exception.SqlException;
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
     * @throws SqlException 202 数据库错误
     */
    int insertOrder(Order order) throws SqlException;

    /**
     * 根据订单号查询订单明细
     * @param orderId 订单主键ID
     * @return 包含明细列表的 {@link Order} 对象
     * @throws SqlException 202 数据库错误
     */
    Order queryOrderByIdTime(String orderIdTime) throws SqlException;

    /**
     * 根据用户ID查询该用户的所有订单主记录（不包含明细）。
     * @param userId 所属用户的主键ID
     * @return List<Order> 订单列表，没有则返回null
     * @throws SqlException 202 数据库错误
     */
    List<Order> queryOrdersByUserId(Long userId) throws SqlException;

    /**
     * 分页查询用户订单
     * @param offset 偏移数量
     * @param limit 页面大小
     * @return 订单列表，如果没有则返回空列表
     * @throws SqlException 202 数据库错误
     */
    List<Order> queryOrdersWithDetailsForPage(int offset, int limit) throws SqlException;
                                                      
    /**
     * 查询订单总数
     * @param userId 所属用户的主键id
     * @return 订单总数
     * @throws SqlException 202 数据库错误
     */
    Long countOrders(@Param("userId") Long userId) throws SqlException;

    
    // --- 订单明细表操作 ---

    /**
     * 批量插入订单明细记录。
     * @param details 订单明细列表
     * @return int 受影响的总行数
     * @throws SqlException 202 数据库错误
     */
    int insertOrderDetails(List<OrderDetail> details) throws SqlException;
}