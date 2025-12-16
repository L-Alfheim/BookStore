package com.twt.bookstore.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.twt.bookstore.poju.BookInfo;

@Repository
public class BookRepositoryImpl implements BookRepository {

    //获取sqlSessionTemplate
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    //命名空间
    private static final String NAMESPACE = "com.twt.bookstore.mapper.BookRepository.";

    /**
     * 根据UUID查找主键id
     * @param uuid UUID
     * @return Long 主键id
     */
    @Override
    public Long queryIdByUUID(UUID uuid) {
        return sqlSessionTemplate.selectOne(NAMESPACE + "queryIdByUUID", uuid);
    }

    /**
     * 根据UUID查找书籍
     * Exception发生时，自动回滚，必须添加到事务中
     * 没有结果返回null
     * @param uuid 书籍UUID
     * @return Bookinfo 数据具体类 无结果返回null
     */
    @Override
    public BookInfo queryByUUID(UUID uuid) {
        return sqlSessionTemplate.selectOne(NAMESPACE + "queryByUUID", uuid);
    }

    /**
     * 模糊查找书籍信息，时间降序排列
     * 配合pageHelper使用
     * @param title 书名，可为null
     * @param author 作者，可为null
     * @return List<BookInfo>
     */
    public List<BookInfo> queryByTitleOrAuthorAndPaged(String title, String author) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("author", author);
        return sqlSessionTemplate.selectList(NAMESPACE + "queryByTitleOrAuthorAndPaged", params);
    }

    /**
     * 更新书籍相关
     * @param book 书籍对象
     * @return int 影响的行数
     */
    @Override
    public int updateBookFields(BookInfo book) {
        return sqlSessionTemplate.update(NAMESPACE + "updateBookFields", book);
    }
    
    /**
     * 更新库存
     * @param bookId 书籍ID
     * @param changeQuantity Integer 增减数量，正为增加库存，负为减少库存
     * @return int 影响的行数
    */
   @Override
    public int updateStockQuantity(Long id, Integer changeQuantity) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("changeQuantity", changeQuantity);
        
        return sqlSessionTemplate.update(NAMESPACE + "updateStockQuantity", params);
    }
    
    /**
     * 添加书籍
     * @param book 书籍对象
     * @return int 影响的行数
     */
    @Override
    public int insertBookFields(BookInfo book) {
        return sqlSessionTemplate.insert(NAMESPACE + "insertBookFields", book);
    }

    /**
     * 根据一组 bookId 批量查询 BookInfo
     * @param bookIds  bookId
     * @return 对应的 BookInfo 列表
     */
    @Override
    public List<BookInfo> queryBookInfoByIds(Collection<Long> bookIds) {
        return sqlSessionTemplate.selectList(NAMESPACE + "queryBookInfoByIds", bookIds);
    }

    /**
     * 根据UUID查找主键id
     * 包含已删除书籍，专用于购物车映射
     * @param uuid UUID
     * @return Long 主键id
     */
    @Override
    public Long queryIdByUuidContainDelete(UUID uuid) {
        return sqlSessionTemplate.selectOne(NAMESPACE + "queryIdByUuidContainDelete", uuid);
    }
}
