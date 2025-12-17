package com.twt.bookstore.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twt.bookstore.dto.request.OrderQueryDTO;
import com.twt.bookstore.dto.response.BaseResponse;
import com.twt.bookstore.dto.response.OrderDTO;
import com.twt.bookstore.dto.response.OrderDetailDTO;
import com.twt.bookstore.dto.userContext.UserContext;
import com.twt.bookstore.exception.ServiceException;
import com.twt.bookstore.service.OrderService;

import lombok.RequiredArgsConstructor;


/**
 * 用户订单控制器
 */
@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class UserOrderController {

    private final OrderService orderService;

    /**
     * 用户订单提交
     * @param userContext 用户身份
     * @return
     * @throws ServiceException
     */
    @PostMapping()
    public BaseResponse<Void> postOrder(@AuthenticationPrincipal UserContext userContext) throws ServiceException {
        return orderService.createOrder(userContext);
    }

    /**
     * 用户分页查询订单主记录
     * @param userContext 用户身份
     * @param request OrderQueryDTO
     * @return
     * @throws ServiceException
     */
    @GetMapping()
    public BaseResponse<List<OrderDTO>> getOrders(@AuthenticationPrincipal UserContext userContext, OrderQueryDTO request) throws ServiceException {
        return orderService.getOrdersByPage(userContext, request);
    }

    /**
     * 用户查询订单明细
     * @param userContext 用户身份
     * @param id 订单号
     * @return
     * @throws ServiceException
     */
    @GetMapping("/{id}")
    public BaseResponse<List<OrderDetailDTO>> getMethodName(@AuthenticationPrincipal UserContext userContext, @PathVariable("id") String id) throws ServiceException {
        return orderService.getOrderDetails(userContext, id);
    }
    
    

}
