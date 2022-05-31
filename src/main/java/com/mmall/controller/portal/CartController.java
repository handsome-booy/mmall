package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ICartService iCartService;

    /**
     * 添加商品到购物车
     * @param httpSession
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession httpSession, Integer count, Integer productId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse<CartVo> update(HttpSession httpSession, Integer count, Integer productId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iCartService.update(user.getId(), productId, count);
    }

    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpSession httpSession, String productIds) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iCartService.deleteProduct(user.getId(), productIds);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iCartService.list(user.getId());
    }

    /**
     * 全选购物车
     * @param httpSession
     * @return
     */
    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.CHECKED, null);
    }

    /**
     * 全反选购物车
     * @param httpSession
     * @return
     */
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.UN_CHECKED, null);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpSession httpSession, Integer productId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.CHECKED, productId);
    }

    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpSession httpSession, Integer productId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
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
    public ServerResponse<Integer> getCartProductCount(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }
}
