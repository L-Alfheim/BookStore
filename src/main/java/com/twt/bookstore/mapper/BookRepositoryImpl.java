package com.twt.bookstore.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.twt.bookstore.exception.SqlException;
import com.twt.bookstore.poju.BookInfo;

@Repository
public class BookRepositoryImpl implements BookRepository {

    //获取sqlSessionTemplate
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 根据UUID查找主键id
     * @param uuid UUID
     * @return Long 主键id
     * @throws SqlException 202
     */
    @Override
    public Long queryIdByUUID(UUID uuid) throws SqlException {
        try {
            return sqlSessionTemplate.selectOne("queryIdByUUID", uuid);
        } catch (Exception e) {
            //查询出现错误
            throw new SqlException(202, "errors occurs when queryIdByUUID: " + uuid, e);
        }
    }
    /**
     * 根据书名查询书籍
     * 如果需要，必须手动回滚
     * 如果没有结果返回空list
     * @param title String书名
     * @return resultList List<BookInfo>
     * @throws SqlException 202
     */
    @Override
    public List<BookInfo> queryByName(String title) throws SqlException {
        try {
            return sqlSessionTemplate.selectList("queryByName", title);
        } catch(Exception e) {
            //查询出现错误
            throw new SqlException(202, "errors occurs when queryByName: " + title, e);
        }
    }

    /**
     * 根据作者查询书籍
     * 如果需要，必须手动回滚
     * 如果没有结果返回空list
     * @param title String书名
     * @return resultList List<BookInfo>
     * @throws SqlException 202
     */
    @Override
    public List<BookInfo> queryByAuthor(String author) throws SqlException {
        try {
            return sqlSessionTemplate.selectList("queryByAuthor", author);
        } catch(Exception e) {
            //查询出现错误
            throw new SqlException(202, "errors occurs when queryByAuthor: " + author, e);
        }
    }


    /**
     * 根据UUID查找书籍
     * Exception发生时，自动回滚，必须添加到事务中
     * 没有结果返回null
     * @param uuid 书籍UUID
     * @return Bookinfo 数据具体类 无结果返回null
     * @throws SqlException 202
     */
    @Override
    public BookInfo queryByUUID(UUID uuid) throws SqlException {
        try {
            return sqlSessionTemplate.selectOne("queryByUUID", uuid);
        } catch(Exception e) {
            //查询出现错误
            throw new SqlException(202, "errors occurs when queryByUUID" + uuid.toString(), e);
        }
    }

    /**
     * 分页查询书籍信息
     * @param rowBounds 
     * @return resultList List<BookInfo>
     * @throws SqlException 202
     */
    @Override
    public List<BookInfo> queryByPage() throws SqlException {
        try {
            return sqlSessionTemplate.selectList("queryByPage");
        } catch(Exception e) {
            //查询出现错误
            throw new SqlException(202, "errors occurs when queryByPage", e);
        }
    }

    /**
     * 更新书籍相关
     * @param book 书籍对象
     * @return int 影响的行数
     */
    @Override
    public int updateBookFields(BookInfo book) throws SqlException {
        try{
            return sqlSessionTemplate.update("updateBookFields", book);
        } catch(Exception e) {
            throw new SqlException(202, "errors occurs when updateBookFields, UUID: " + book.getUuid(), e);
        }
    }
    
    /**
     * 更新库存
     * @param bookId 书籍ID
     * @param changeQuantity Integer 增减数量，正为增加库存，负为减少库存
     * @return int 影响的行数
    */
   @Override
    public int updateStockQuantity(Long id, Integer changeQuantity) throws SqlException {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("bookId", id);
            params.put("changeQuantity", changeQuantity);
            
            return sqlSessionTemplate.update("updateStockQuantity", params);
        } catch (Exception e) {
            throw new SqlException(202,"errors occurs when updateBookFields, book_id: " + id, e);
        }
    }
    
    /**
     * 添加书籍
     * @param book 书籍对象
     * @return int 影响的行数
     */
    @Override
    public int insertBookFields(BookInfo book) throws SqlException {
        try{
            return sqlSessionTemplate.update("insertBookFields", book);
        } catch(Exception e) {
            throw new SqlException(202, "errors occurs when insertBookFields, UUID: " + book.getUuid(), e);
        }
    }
}
