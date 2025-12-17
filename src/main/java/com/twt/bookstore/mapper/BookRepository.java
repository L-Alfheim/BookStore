package com.twt.bookstore.mapper;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.twt.bookstore.poju.BookInfo;

public interface BookRepository {
    /**
     * 根据UUID查找主键id
     * @param uuid UUID
     * @return Long 主键id
     */
    public Long queryIdByUUID(UUID uuid);

    /**
     * 根据UUID查找书籍
     * 没有结果返回null
     * @param uuid 书籍UUID
     * @return Bookinfo 数据具体类 无结果返回null
     */
    public BookInfo queryByUUID(UUID uuid);

    /**
     * 模糊查找书籍信息，时间降序排列
     * 配合pageHelper使用
     * @param title 书名，可为null
     * @param author 作者，可为null
     * @return List<BookInfo>
     */
    List<BookInfo> queryByTitleOrAuthorAndPaged(String title, String author);

    /**
     * 更新书籍相关
     * @param book 书籍对象
     * @return int 影响的行数
     */
    int updateBookFields(BookInfo book);

    /**
     * 更新库存
     * @param bookId 书籍ID
     * @param changeQuantity 变动数量 (正数为增加库存，负数为减少库存)
     * @return 影响的行数
     * @deprecated  不安全 使用{@link #decreaseStock(Long, Integer)}
     */
    int updateStockQuantity(Long bookId, Integer changeQuantity);

    /**
     * 添加书籍
     * @param book 书籍对象
     * @return int 影响的行数
     */
    public int insertBookFields(BookInfo book);

    /**
     * 根据一组 bookId 批量查询 BookInfo
     * 用于购物车查询映射
     * @param bookIds  bookId Collection<Long>
     * @return 对应的 BookInfo 列表
     */
    List<BookInfo> queryBookInfoByIds(Collection<Long> bookIds);

    /**
     * 根据UUID查找主键id
     * 包含已删除书籍，专用于购物车映射
     * @param uuid UUID
     * @return Long 主键id
     */
    Long queryIdByUuidContainDelete(UUID uuid);

    /**
     * 根据一组 bookId 批量查询 BookInfo
     * 过滤删除的和不在售的书籍
     * 用于下单验证
     *
     * @param bookIds 书籍主键id List
     * @return List<BookInfo>
     * @apiNote 下单验证使用这个方法
     */
    List<BookInfo> selectAvailableBooksByIds(List<Long> bookIds);

        /**
     * 根据一组 bookId 批量查询 BookInfo
     * 包括删除的和不在售的书籍
     * 用于下单验证
     *
     * @param bookIds 书籍主键id List
     * @return List<BookInfo>
     * @apiNote 订单查询使用这个方法
     */
    List<BookInfo> queryBookInfoByIdsContainDelete(List<Long> bookIds);

    /**
     * 下单扣除商品库存
     * 采用数据库层面的乐观锁
     *
     * @param bookId 书籍 ID
     * @param quantity 扣除数量
     * @return 受影响的行数
     * @apiNote 如果影响行数为0，则扣除失败，需要取消订单生成
     */
    int decreaseStock(Long bookId, Integer quantity);

    UUID queryUuidById(Long bookId);
}

