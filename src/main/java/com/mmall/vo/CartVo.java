package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 这个类属于是购物车的抽象类，里面包含了购物车下面的所有商品和总价
 */
public class CartVo {
    List<CartProductVo> cartProductVoList;//一个userId下面所有的产品
    private BigDecimal cartTotalPrice;//一个userId下面所有产品的总价
    private Boolean allChecked;
    private  String imageHost;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
