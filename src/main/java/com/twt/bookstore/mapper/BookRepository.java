package com.twt.bookstore.mapper;

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
     */
    int updateStockQuantity(Long bookId, Integer changeQuantity);

    /**
     * 添加书籍
     * @param book 书籍对象
     * @return int 影响的行数
     */
    public int insertBookFields(BookInfo book);
}
