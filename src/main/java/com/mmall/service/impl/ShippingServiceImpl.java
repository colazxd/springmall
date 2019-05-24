package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author colaz
 * @date 2019/5/16
 **/
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            Map mapResult = Maps.newHashMap();
            mapResult.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess(mapResult);
        }
        return ServerResponse.createByErrorMsg("新增地址失败");
    }

    @Override
    public ServerResponse del(Integer userId, Integer shippingId) {
        int rowCount = shippingMapper.deleteByPrimaryKeyAndUserId(shippingId, userId);
        if (rowCount > 0) {
            ServerResponse.createBySuccessMsg("删除地址成功");
        }
        return ServerResponse.createByErrorMsg("删除地址失败");
    }

    @Override
    public ServerResponse update(Integer userId, Shipping shipping) {
        //需要从session中拿到userId，防止横向越权，重写update根据userId和shippingId对shipping更新
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByPrimaryKeyAndUserId(shipping);
        if (rowCount > 0) {
            ServerResponse.createBySuccessMsg("更新地址成功");
        }
        return ServerResponse.createByErrorMsg("更新地址失败");
    }

    @Override
    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId, shippingId);
        if (shipping != null) {
            return ServerResponse.createBySuccess(shipping);
        }
        return ServerResponse.createByErrorMsg("找不到该地址");
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByuserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }


}
