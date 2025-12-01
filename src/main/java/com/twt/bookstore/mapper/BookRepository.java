package com.twt.bookstore.mapper;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Mapper;

import com.twt.bookstore.exception.SqlException;
import com.twt.bookstore.poju.BookInfo;

@Mapper
public interface BookRepository {
    /**
     * 根据UUID查找主键id
     * @param uuid UUID
     * @return Long 主键id
     * @throws SqlException 202
     */
    public Long queryIdByUUID(UUID uuid) throws SqlException;

    /**
     * 根据书名查询书籍
     * 如果需要，必须手动回滚
     * 如果没有结果返回空list
     * @param title String书名
     * @return resultList List<BookInfo>
     * @throws SqlException 202
     */
    public List<BookInfo> queryByName(String title) throws SqlException;

    /**
     * 根据作者查询书籍
     * 如果需要，必须手动回滚
     * 如果没有结果返回空list
     * @param title String书名
     * @return resultList List<BookInfo>
     * @throws SqlException 202
     */
    public List<BookInfo> queryByAuthor(String author) throws SqlException;

    /**
     * 根据UUID查找书籍
     * Exception发生时，自动回滚，必须添加到事务中
     * 没有结果返回null
     * @param uuid 书籍UUID
     * @return Bookinfo 数据具体类 无结果返回null
     * @throws SqlException 202
     */
    public BookInfo queryByUUID(UUID uuid) throws SqlException;

    /**
     * 分页查询书籍信息
     * @param rowBounds 
     * @return resultList List<BookInfo>
     * @throws SqlException 202
     */
    public List<BookInfo> queryByPage() throws SqlException;

    /**
     * 动态更新书籍字段。
     * 使用场景:
     * 1. 管理员修改书籍信息 (如: title, price, isAvailable)。
     * 2. 用户下单/取消订单时更新库存 (stockQuantity)。
     * 3. 逻辑删除书籍 (isDeleted)。
     *
     * 只会更新传入对象中非空或需要变更的字段。
     *
     * @param book 要更新的书籍信息对象 (必须包含 bookId)
     * @return 影响的行数
     */
    int updateBookFields(BookInfo book) throws SqlException;

    /**
     * 更新库存
     * @param bookId 书籍ID
     * @param changeQuantity 变动数量 (正数为增加库存，负数为减少库存)
     * @return 影响的行数
     */
    int updateStockQuantity(Long bookId, Integer changeQuantity) throws SqlException;

    /**
     * 添加书籍
     * @param book 书籍对象
     * @return int 影响的行数
     */
    public int insertBookFields(BookInfo book) throws SqlException;
}
