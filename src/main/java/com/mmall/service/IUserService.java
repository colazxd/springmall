package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @author colaz
 * @date 2019/4/30 10:42
 **/
public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String type, String field);

    ServerResponse<User> getInfomation(Integer userId);

    ServerResponse<String> getForgetQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse resetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse checkAdminRole(User user);

}
