package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

public interface IUserService {
    ServerResponse<User> login(String username, String password);
    ServerResponse<String> register(User user);
    ServerResponse<String> checkValid(String str, String type);
    ServerResponse<String> forgetGetQuestion(String username);
    ServerResponse<String> checkAnswer(String username, String question, String answer);
    ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken);
    ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user);
    ServerResponse<User> updateInfo(User newUser);
    ServerResponse<User> getInfo(Integer userId);
}
