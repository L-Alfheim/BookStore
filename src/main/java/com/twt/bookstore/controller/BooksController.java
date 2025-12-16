package com.twt.bookstore.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twt.bookstore.dto.request.BookQueryDTO;
import com.twt.bookstore.dto.response.BaseResponse;
import com.twt.bookstore.dto.response.BookDTO;
import com.twt.bookstore.exception.ServiceException;
import com.twt.bookstore.service.BookService;

import lombok.RequiredArgsConstructor;


/**
 * 公开书籍控制器
 */
@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;

    /**
     * 精确查找
     * @param uuid
     * @return
     * @throws ServiceException 204交给{@link GlobalException}
     */
    @GetMapping("/{uuid}")
    public BaseResponse<BookDTO> getbook(@PathVariable("uuid") UUID uuid) throws ServiceException {
        return bookService.getBookOne(uuid);
    }
    
    /**
     * 泛式查找
     * @param queryDTO
     * @return
     * @throws ServiceException 204交给{@link GlobalException}
     */
    @GetMapping
    public BaseResponse<List<BookDTO>> getBooks(BookQueryDTO queryDTO) throws ServiceException {
        return bookService.getBooks(queryDTO);
    }
    /**
     * ping网络测试
     * @return
     */
    @GetMapping("/ping")
    public String ping() {
        System.out.println("PING CONTROLLER");
        return "ok";
    }
}
