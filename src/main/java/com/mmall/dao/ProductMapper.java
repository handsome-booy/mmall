package com.mmall.dao;

import com.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    //@Param注解的作用就是给参数命名，参数命名后就能根据名字得到参数值。这样之后，我们就能在sql语句中利用#{xxx}的方式来取得传入的值，其中{}中的值就是我们在@Param中指定的值。
    //参数大于1个的时候都要用@Param注解
    List<Product> selectByNameAndProductId(@Param("productName") String productName, @Param("productId") Integer productId);
}