package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("manage/category")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private IUserService iUserService;

    /**
     * 如果请求中没有传入parentId，就用默认值0，代表这是一个根节点
     * @RequestParam注解的作用就是将前端请求的参数一对一绑定到我们的控制器方法参数上去
     * @param request
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping(value = "add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpServletRequest request, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //校验一下当前登录用户是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //增加品类
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    /**
     * 更新品类名称
     * @param request
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpServletRequest request, Integer categoryId, String categoryName) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //校验一下当前登录用户是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //增加品类
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    /**
     * 获取当前节点的子节点（不递归）
     * 如果没有传入当前节点，默认就是0
     * @param request
     * @param categoryId
     * @return
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpServletRequest request, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //校验一下当前登录用户是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //查询子节点的category信息，并且不递归，保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    /**
     * 获取当前节点的子节点，以及子节点的子节点（递归）
     * @param request
     * @param categoryId
     * @return
     */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpServletRequest request, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessgae(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //校验一下当前登录用户是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //查询子节点的category信息，并且递归向下查询
            return iCategoryService.getCategoryAndDeepChildrenCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

}
