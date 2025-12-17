package com.twt.bookstore.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twt.bookstore.dto.request.CartAddDTO;
import com.twt.bookstore.dto.response.BaseResponse;
import com.twt.bookstore.dto.response.CartDTO;
import com.twt.bookstore.dto.userContext.UserContext;
import com.twt.bookstore.exception.ServiceException;
import com.twt.bookstore.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/**
 * 用户购物车控制器
 */
@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 获取用户购物车所有商品
     * @param userContext 用户身份
     * @return BaseResponse<List<CartDTO>>
     * @throws ServiceException
     */
    @GetMapping
    public BaseResponse<List<CartDTO>> getAllItem(@AuthenticationPrincipal UserContext userContext) throws ServiceException {
        return cartService.queryCart(userContext);
    }

    /**
     * 增加购物车商品
     * @param userContext 用户身份
     * @param request CartAddDTO
     * @return BaseResponse<Void>
     * @throws ServiceException
     */
    @PostMapping("/add")
    public BaseResponse<Void> postItemToCart(@AuthenticationPrincipal UserContext userContext, @Valid @RequestBody CartAddDTO request) throws ServiceException {
        return cartService.addOneItem(userContext, request);
    }

    /**
     * 删除购物车单一商品
     * @param uuid uuid
     * @param userContext 用户身份
     * @return BaseResponse<Void>
     * @throws ServiceException
     */
    @DeleteMapping("/{uuid}")
    public BaseResponse<Void> deleteItemToCart(@PathVariable UUID uuid, @AuthenticationPrincipal UserContext userContext) throws ServiceException {
        return cartService.deleteOneItem(uuid, userContext);
    }

    /**
     * 删除购物车所有商品
     * @param userContext 用户身份
     * @return BaseResponse<Void>
     * @throws ServiceException
     */
    @DeleteMapping
    public BaseResponse<Void> deleteAllItemToCart(@AuthenticationPrincipal UserContext userContext) throws ServiceException {
        return cartService.deleteAllItem(userContext);
    }
    
}
