package com.twt.bookstore.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
 *管理员订单控制器
 */
@RestController
@RequestMapping("api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    /**
     * 管理员分页查询订单主记录
     * @param userContext 用户身份
     * @param request OrderQueryDTO
     * @return
     * @throws ServiceException
     */
    @GetMapping()
    public BaseResponse<List<OrderDTO>> getOrders(@AuthenticationPrincipal UserContext userContext, OrderQueryDTO request) throws ServiceException {
        return orderService.getAllOrdersByPage(request);
    }

    /**
     * 管理员查询订单明细
     * @param userContext 用户身份
     * @param id 订单号
     * @return
     * @throws ServiceException
     */
    @GetMapping("/{id}")
    public BaseResponse<List<OrderDetailDTO>> getMethodName(@AuthenticationPrincipal UserContext userContext, @PathVariable("id") String id) throws ServiceException {
        return orderService.getAnyOrderDetails(id);
    }
    
    

}
