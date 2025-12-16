package com.twt.bookstore.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twt.bookstore.dto.request.BookCreateDTO;
import com.twt.bookstore.dto.request.BookUpdateDTO;
import com.twt.bookstore.dto.response.BaseResponse;
import com.twt.bookstore.dto.response.BookDTO;
import com.twt.bookstore.exception.GlobalExceptionHandler;
import com.twt.bookstore.exception.ServiceException;
import com.twt.bookstore.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 管理员书籍控制器
 */
@RestController
@RequestMapping("api/admin/books")
@RequiredArgsConstructor
public class AdminBooksController {

    private final BookService bookService;

    /**
     * 创建图书
     * @throws ServiceException 204交给{@link GlobalExceptionHandler}
     */
    @PostMapping
    public BaseResponse<BookDTO> createBook(@Valid @RequestBody BookCreateDTO createDTO) throws ServiceException {
        return bookService.createBook(createDTO);
    }

    /**
     * 更新图书
     * PUT /books/{id}
     * @throws ServiceException 204交给{@link GlobalExceptionHandler}
     */
    @PutMapping("/{uuid}")
    public BaseResponse<BookDTO> updateBook(
            @PathVariable UUID uuid,
            @Valid @RequestBody BookUpdateDTO updateDTO) throws ServiceException {
                System.err.println("Recieve AdminBookPut");
        
        return bookService.updateBook(uuid, updateDTO);
    }
    
    /**
     * 删除图书
     * DELETE /books/{id}
     * @throws ServiceException 204交给{@link GlobalExceptionHandler}
     */
    @DeleteMapping("/{uuid}")
    public BaseResponse<Void> deleteBook(@PathVariable("uuid") UUID uuid) throws ServiceException {
        System.err.println("Recieve AdminBookDelete");
        return bookService.deleteBook(uuid);
    }

}
