package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import com.mmall.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ICartService iCartService;

    /**
     * 添加商品到购物车
     * @param request
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpServletRequest request, Integer count, Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iCartService.add(user.getId(), productId, count);
    }

    /**
     * 更新购物车
     */
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpServletRequest request, Integer count, Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iCartService.update(user.getId(), productId, count);
    }

    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpServletRequest request, String productIds) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iCartService.deleteProduct(user.getId(), productIds);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iCartService.list(user.getId());
    }

    /**
     * 全选购物车
     * @param request
     * @return
     */
    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.CHECKED, null);
    }

    /**
     * 全反选购物车
     * @param request
     * @return
     */
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.UN_CHECKED, null);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpServletRequest request, Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.CHECKED, productId);
    }

    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpServletRequest request, Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.UN_CHECKED, productId);
    }

    /**
     * 查询购物车中的商品数量
     * @param httpSession
     * @return
     */
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }
}
