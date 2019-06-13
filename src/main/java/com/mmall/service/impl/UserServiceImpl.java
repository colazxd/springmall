package com.mmall.service.impl;

import com.github.pagehelper.StringUtil;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author colaz
 * @date 2019/4/30
 **/
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resCount = userMapper.checkUsername(username);
        if (resCount == 0) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        user.setPassword(StringUtils.EMPTY);
        if (user == null) {
            return ServerResponse.createByErrorMsg("密码错误");
        }
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse validResponde = this.checkValid(Const.USERNAME, user.getUsername());
        if (!validResponde.isSuccess()) {
            return ServerResponse.createByErrorMsg("用户名已存在");
        }
        validResponde = this.checkValid(Const.EMAIL, user.getEmail());
        if (!validResponde.isSuccess()) {
            return ServerResponse.createByErrorMsg("邮箱已存在");
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resCount = userMapper.insert(user);
        if (resCount > 0) {
            return ServerResponse.createBySuccessMsg("注册成功");
        }
        return ServerResponse.createByErrorMsg("注册失败");
    }

    @Override
    public ServerResponse<String> checkValid(String type, String field) {
        if (StringUtils.isNotBlank(field)) {
            if (Const.USERNAME.equals(type)) {
                int resCount = userMapper.checkUsername(field);
                if (resCount > 0) {
                    return ServerResponse.createByErrorMsg("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resCount = userMapper.checkEmail(field);
                if (resCount > 0) {
                    return ServerResponse.createByErrorMsg("邮箱已被注册");
                }
            }
        }
        return ServerResponse.createBySuccessMsg("校验成功");
    }

    @Override
    public ServerResponse<User> getInfomation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMsg("找不到当前用户信息");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<User> updateUserInformation(User user) {
        //userName不能更新
        //email需要检验，不能和其他用户重复（查找数据库是否存在新的eamil，不存在直接更新，存在且不是该用户的失败）
        int resCount = userMapper.checkEmailByUserId(user.getId(), user.getEmail());
        if (resCount > 0) {
            return ServerResponse.createByErrorMsg("email重复，修改email后尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0)
            return ServerResponse.createBySuccess("更新用户信息成功", updateUser);

        return ServerResponse.createByErrorMsg("更新用户信息失败");
    }

    @Override
    public ServerResponse<String> getForgetQuestion(String username) {
        String question = userMapper.selectQuestionByUsername(username);
        if (question == null) {
            return ServerResponse.createByErrorMsg("该用户未设置找回密码问题");
        }
        return ServerResponse.createBySuccess(question);
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resCount = userMapper.checkForgetQuestionAnswerByUsername(username, question, answer);
        if (resCount > 0) {
            String token = UUID.randomUUID().toString();
            RedisPoolUtil.setEx(Const.TOKEN_PREFIX+username, token, 60*60*12);
            return ServerResponse.createBySuccess(token);
        }
        return ServerResponse.createByErrorMsg("问题答案错误");
    }

    @Override
    public ServerResponse resetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMsg("参数错误，需要token");
        }
        //保证tokencache中key的安全，先验证一下username
        ServerResponse<String> response = this.checkValid(Const.USERNAME, username);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMsg("该用户不存在");
        }

        String token = RedisPoolUtil.get(Const.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(token)) {
            ServerResponse.createByErrorMsg("Token无效或已过期");
        }
        if (StringUtils.equals(token, forgetToken)) {
            String passwordMD5 = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, passwordMD5);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMsg("更新密码成功");
            }
        } else {
            return ServerResponse.createByErrorMsg("token校验失败，请重新获取token");
        }
        return ServerResponse.createByErrorMsg("密码更新失败");
    }

    /**
     * 检验user是否为admin
     * @param user
     * @return
     */
    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN)
            return ServerResponse.createBySuccess();
        return ServerResponse.createByError();
    }


}


