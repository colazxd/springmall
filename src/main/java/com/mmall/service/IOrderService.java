package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

/**
 * @author colaz
 * @date 2019/5/17
 **/
public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse alipayCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);
}
