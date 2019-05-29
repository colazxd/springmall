package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Order;
import com.mmall.vo.OrderProductVo;
import com.mmall.vo.OrderVo;

import java.util.Map;

/**
 * @author colaz
 * @date 2019/5/17
 **/
public interface IOrderService {

    ServerResponse create(Integer userId, Integer shippingId);

    ServerResponse cancel(Integer userId, Long orderNo);

    ServerResponse<OrderVo> detail(Integer userId, Long orderNo);

    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);

    ServerResponse<OrderProductVo> getOrderCartProduct(Integer userId);

    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse alipayCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    //backend
    ServerResponse manageList(int pageNum, int pageSize);

    ServerResponse<OrderVo> manageDetail(Long orderNo);

    ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);

    ServerResponse<String> sendGoods(Long orderNo);



}
