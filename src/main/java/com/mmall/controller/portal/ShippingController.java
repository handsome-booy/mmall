package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("shipping")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    /**
     * 这里使用了springMVC的对象绑定方法，不用一个个字段都传进来
     * @param request
     * @param shipping
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpServletRequest request, Shipping shipping) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iShippingService.add(user.getId(), shipping);
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ServerResponse del(HttpServletRequest request, Integer shippingId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iShippingService.del(user.getId(), shippingId);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpServletRequest request, Shipping shipping) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iShippingService.update(user.getId(), shipping);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse select(HttpServletRequest request, Integer shippingId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iShippingService.select(user.getId(), shippingId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iShippingService.list(user.getId(), pageNum, pageSize);
    }
}
