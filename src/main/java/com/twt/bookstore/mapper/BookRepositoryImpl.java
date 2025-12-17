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
     * @param bookIds  bookId Collection<Long>
     * @return 对应的 BookInfo 列表
     */
    @Override
    public List<BookInfo> queryBookInfoByIds(Collection<Long> bookIds) {
        if (bookIds == null) {
            throw new IllegalArgumentException("bookIds cannot be null");
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("bookIds", bookIds);
        
        return sqlSessionTemplate.selectList(NAMESPACE + "queryBookInfoByIds", params);
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

    /**
     * 根据一组 bookId 批量查询 BookInfo
     * 过滤删除的和不在售的书籍
     * 用于下单验证
     *
     * @param bookIds 书籍主键id List
     * @return List<BookInfo>
     * @apiNote 下单验证使用这个方法
     */
    @Override
    public List<BookInfo> selectAvailableBooksByIds(List<Long> bookIds) {
        if (bookIds == null) {
            throw new IllegalArgumentException("bookIds cannot be null");
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("bookIds", bookIds);

        return sqlSessionTemplate.selectList(NAMESPACE + "selectAvailableBooksByIds", params);
    }
    
    /**
     * 根据一组 bookId 批量查询 BookInfo
     * 包括删除的和不在售的书籍
     * 用于下单验证
     *
     * @param bookIds 书籍主键id List
     * @return List<BookInfo>
     * @apiNote 订单查询使用这个方法
     */
    @Override
    public List<BookInfo> queryBookInfoByIdsContainDelete(List<Long> bookIds) {
        if (bookIds == null) {
            throw new IllegalArgumentException("bookIds cannot be null");
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("bookIds", bookIds);

        return sqlSessionTemplate.selectList(NAMESPACE + "queryBookInfoByIdsContainDelete", params);
    }

    
    /**
     * 下单扣除商品库存
     * 采用数据库层面的乐观锁
     *
     * @param bookId 书籍 ID
     * @param quantity 扣除数量
     * @return 受影响的行数
     * @apiNote 如果影响行数为0，则扣除失败，需要取消订单生成
     */
    @Override
    public int decreaseStock(Long bookId, Integer quantity) {
        Map<String, Object> params = new HashMap<>();
        params.put("bookId", bookId);
        params.put("quantity", quantity);

        return sqlSessionTemplate.update(NAMESPACE + "decreaseStock", params);
    }

    /**
     * 通过主键id查找UUID
     * @param bookId 主键id
     * @return uuid UUID
     */
    @Override
    public UUID queryUuidById(Long bookId) {
        return sqlSessionTemplate.selectOne(NAMESPACE + "queryUuidById", bookId);
    }
}
