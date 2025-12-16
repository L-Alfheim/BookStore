package com.twt.bookstore.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.twt.bookstore.dto.request.BookCreateDTO;
import com.twt.bookstore.dto.request.BookQueryDTO;
import com.twt.bookstore.dto.request.BookUpdateDTO;
import com.twt.bookstore.dto.response.BaseResponse;
import com.twt.bookstore.dto.response.BookDTO;
import com.twt.bookstore.exception.ServiceException;
import com.twt.bookstore.mapper.BookRepository;
import com.twt.bookstore.poju.BookInfo;

import lombok.RequiredArgsConstructor;


/**
 * 书籍查询类
 * 包含方法
 * 1. 通过UUID查询单个书籍信息
 */
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    /**
     * 通过UUID查询单个书籍信息
     * @param uuid UUID
     * @return BookInfo
     * @throws ServiceException 204
     */
    @Transactional(rollbackFor = ServiceException.class)
    public BaseResponse<BookDTO> getBookOne(UUID uuid) throws ServiceException {
        try {
            BookInfo resutBookInfo = bookRepository.queryByUUID(uuid);

            
            //结果不存在，直接返回
            if (resutBookInfo == null) {
                return BaseResponse.error(404, "Book: " + uuid.toString() + " is not found");
            }
            
            BookDTO resultDTO = new BookDTO(resutBookInfo.getUuid(), 
                                            resutBookInfo.getTitle(), 
                                            resutBookInfo.getAuthor(), 
                                            resutBookInfo.getPrice(), 
                                            resutBookInfo.getDescription(), 
                                            resutBookInfo.getStockQuantity(), 
                                            resutBookInfo.isAvailable());
            return BaseResponse.success(resultDTO);
        } catch (Exception e) {
            throw new ServiceException(204, "error occur when getBookOne, uuid: " + uuid, e);
        }
    }

    /**
     * 通过 queryDTO 泛式查询书籍信息
     * @param queryDTO
     * @return BaseResponse 对象 含List<BookDTO>
     * @throws ServiceException 204
     */
    @Transactional(rollbackFor = ServiceException.class)
    public BaseResponse<List<BookDTO>> getBooks(BookQueryDTO queryDTO) throws ServiceException {
        try {
            PageHelper.startPage(queryDTO.page(), queryDTO.size());
            List<BookInfo> result = bookRepository.queryByTitleOrAuthorAndPaged(queryDTO.title(),queryDTO.author());
            if(result == null) {
                return BaseResponse.success("No result", null);
            } else {
                return BaseResponse.success(convertToDtoList(result));
            }
        } catch (Exception e) {
            throw new ServiceException(204, "error occur when getBooks, queryDTO: " + queryDTO.toString(), e);
        }
    }

    /**
     * 创建新书籍
     * @param bookCreateDTO
     * @return uuid
     * @throws ServiceException 204
     */
    @Transactional(rollbackFor = ServiceException.class)
    public BaseResponse<BookDTO> createBook(BookCreateDTO bookCreateDTO) throws ServiceException {
        UUID uuid = UUID.randomUUID();
        BookInfo book = new BookInfo(null, 
                                    uuid, 
                                    bookCreateDTO.title(), 
                                    bookCreateDTO.author(), 
                                    bookCreateDTO.price(), 
                                    bookCreateDTO.description(), 
                                    bookCreateDTO.stockQuantity(), 
                                    bookCreateDTO.isAvailable(), 
                                    false, 
                                    null, 
                                    null);

        try {
            bookRepository.insertBookFields(book);
            BookDTO result = new BookDTO(uuid, 
                                        bookCreateDTO.title(), 
                                        bookCreateDTO.author(), 
                                        bookCreateDTO.price(), 
                                        bookCreateDTO.description(), 
                                        bookCreateDTO.stockQuantity(), 
                                        bookCreateDTO.isAvailable());
            return BaseResponse.success(result);
        } catch (Exception e) {
            throw new ServiceException(204, "error occur when createBook, bookCreateDTO: " + bookCreateDTO.toString(), e);
        }
    }

    /**
     * 通过UUID 查询书籍并完成字段更新
     * @param uuid
     * @param updateDTO
     * @return
     * @throws ServiceException 204
     */
    @Transactional(rollbackFor = ServiceException.class)
    public BaseResponse<BookDTO> updateBook(UUID uuid, BookUpdateDTO updateDTO) throws ServiceException {
        try {
            //查询主键id
            Long id = bookRepository.queryIdByUUID(uuid);
            if (id == null) {
                return BaseResponse.error(202, "Book: " + uuid + "is not found");

            }

            //构造BookInfo
            Instant deleteTime = null;
            if (updateDTO.isDeleted() == true) {
                deleteTime = Instant.now();
            }
            BookInfo book = new BookInfo(id,
                                    uuid, 
                                    updateDTO.title(), 
                                    updateDTO.author(), 
                                    updateDTO.price(), 
                                    updateDTO.description(), 
                                    updateDTO.stockQuantity(), 
                                    updateDTO.isAvailable(), 
                                    updateDTO.isDeleted(), 
                                    null, 
                                    deleteTime);

            //数据库执行
            int n = bookRepository.updateBookFields(book);
            if(n == 1) {
                return BaseResponse.success(new BookDTO(uuid, 
                                                        updateDTO.title(), 
                                                        updateDTO.author(), 
                                                        updateDTO.price(), 
                                                        updateDTO.description(), 
                                                        updateDTO.stockQuantity(), 
                                                        updateDTO.isAvailable()));
            } else {            
                throw new ServiceException(204, "error occur when getBooks, updateDTO: " + updateDTO.toString());
            }
        } catch (Exception e) {
            throw new ServiceException(204, "error occur when getBooks, updateDTO: " + updateDTO.toString(), e);

        }
    }
    
    /**
     * 更具uuid删除单个书籍
     * @param uuid
     * @return {@link BaseResponse}
     * @throws ServiceException 204
     */
    @Transactional(rollbackFor = ServiceException.class)
    public BaseResponse<Void> deleteBook(UUID uuid) throws ServiceException{
        try {
            Long id = bookRepository.queryIdByUUID(uuid);
            if(id == null) {
                return BaseResponse.error(202, "Book: " + uuid + " is not found");
            }
            BookInfo book = new BookInfo();
            book.setId(id);
            book.setDeleted(true);
            int n = bookRepository.updateBookFields(book);
            if (n == 1) {
                return BaseResponse.success("Delete success", null);
            } else {
                throw new ServiceException(204,  "error occur when getBooks,the result should be 1, but it's not. uuid: ", null);
            }
        } catch (Exception e) {
            throw new ServiceException(204, "error occur when getBooks, uuid: " + uuid, e);
        }


    }

    /**
     * 将单个 BookInfo 实体转换为 BookDTO 数据传输对象。
     * @param bookInfo 原始的 BookInfo 实体对象。
     * @return 转换后的 BookDTO 对象。
     * @author Deepseek
     */
    private BookDTO convertToDto(BookInfo bookInfo) {
        if (bookInfo == null) {
            return null;
        }
        
        return new BookDTO(
            bookInfo.getUuid(),
            bookInfo.getTitle(),
            bookInfo.getAuthor(),
            bookInfo.getPrice(),
            bookInfo.getDescription(),
            bookInfo.getStockQuantity(), 
            bookInfo.isAvailable() 
        );
    }

    /**
     * 将查询到的 BookInfo list转换为 BookDTO list
     * @param bookInfoList List<BookInfo>
     * @return List<BookDTO> 
     * @author DeepSeek
     */
    private List<BookDTO> convertToDtoList(List<BookInfo> bookInfoList) {
        if (bookInfoList == null || bookInfoList.isEmpty()) {
            // 返回一个不可变的空列表
            return List.of();
        }

        return bookInfoList.stream()
            // 1. 映射：对列表中的每一个 BookInfo 执行 convertToDto 方法
            .map(this::convertToDto) 
            // 2. 过滤：如果 convertToDto 返回 null（例如 BookInfo 为 null），则跳过该元素
            .filter(dto -> dto != null) 
            // 3. 收集：将所有转换后的 DTO 收集到一个新的 List 中
            .collect(Collectors.toList());
    }
}
