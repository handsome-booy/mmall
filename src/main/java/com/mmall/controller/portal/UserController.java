package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.RedisPool;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * crtl+R 全局替换
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession httpSession,
                                      HttpServletResponse httpServletResponse) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            //httpSession.setAttribute(Const.CURRENT_USER, response.getData());
            //二期改造款
            CookieUtil.writeLoginToken(httpServletResponse, httpSession.getId());
            //用户信息存入redis
            RedisPoolUtil.setEx(httpSession.getId(), JsonUtil.obj2String(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    /**
     * 用户登出
     * 只需要把session中保存的用户信息删除就行
     */
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletResponse response, HttpServletRequest request) {
        //httpSession.removeAttribute(Const.CURRENT_USER);
        //二期改造
        String loginToken = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request, response);
        //必须要把redis存的数据也删除掉
        RedisPoolUtil.del(loginToken);
        return ServerResponse.createBySuccess();
    }

    /**
     * 用户注册
     */
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {//这里使用到了MVC的数据自动绑定
        return iUserService.register(user);
    }

    /**
     *校验用户名的有效性
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取登陆用户的信息
     */
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest request) {
        //User user = (User)httpSession.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
    }

    /**
     * 获取找回密码的提示问题
     * @param username
     * @return
     */
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.forgetGetQuestion(username);
    }

    /**
     * 校验问题答案是否正确
     * 这一步需要用到token和guava的缓存机制
     */
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 重置密码
     * @param username
     * @param newPassword
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        return iUserService.forgetResetPassword(username, newPassword, forgetToken);
    }

    /**
     * 登陆状态的重置密码
     */
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpServletRequest request, String oldPassword, String newPassword) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(oldPassword, newPassword, user);
    }

    /**
     * 在用户登录时更新信息
     * @param request
     * @param newUser
     * @return
     */
    @RequestMapping(value = "update_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInfo(HttpServletRequest request, User newUser) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        newUser.setId(user.getId());
        newUser.setUsername(user.getUsername());
        ServerResponse<User> response = iUserService.updateInfo(newUser);
        if (response.isSuccess()) {
            response.getData().setUsername(user.getUsername());
            RedisPoolUtil.setEx(loginToken, JsonUtil.obj2String(user), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);//更新redis中的用户信息
        }
        return response;
    }

    @RequestMapping(value = "get_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInfo(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }

        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.ERROR.getCode(), "未登录，需要强制登录status=10");
        }
        return iUserService.getInfo(user.getId());
    }
}
