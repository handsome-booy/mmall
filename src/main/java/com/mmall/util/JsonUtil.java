package com.mmall.util;

import com.google.common.collect.Lists;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
public class JsonUtil {

    //将Java对象与我们想要的结果进行映射
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        /**
         * 以下为序列化的参数
         */
        //表示对象的所有字段都要全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        //表示取消默认转换timestamps(即时间戳/毫秒数)形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        //表示忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        //表示所有的日期格式统一为yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        /**
         * 以下为反序列化的参数
         */
        //表示忽略在json字符串中存在，但是在java对象中不存在对应属性的情况
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Pares object to string error", e);
            return null;
        }
    }

    /**
     * 返回格式化好的Josn字符串
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Pares object to string error", e);
            return null;
        }
    }

    /**
     * Class<T>中的泛型类似于List<String>。首先我们知道Class是一个类，但是他这个类内部还存在一个类，
     也就是通过Class的newInstance()方法得到的一个对象的类。如果不传入泛型，那么newInstance()方法得到就是一个Object类的对象，
     如果传入了泛型T，那么就可以直接得到T这个类的对象
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            log.warn("Pares string to object error", e);
            return null;
        }
    }

    /**
     * 该方法是用来转化集合对象的
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (IOException e) {
            log.warn("Pares string to object error", e);
            return null;
        }
    }

    /**
     * 该方法是用来转化集合对象的
     * @param str
     * @param collectionClass 是最外层集合的类型
     * @param elementClasses 是集合内部元素的类型
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            log.warn("Pares string to object error", e);
            return null;
        }
    }


    public static void main(String[] args) {
        User user1 = new User();
        user1.setId(1);
        user1.setPhone("13867428767");

        String user1Json = JsonUtil.obj2String(user1);
        String user1PrettyJson = JsonUtil.obj2StringPretty(user1);
        log.info("user1Json:{}", user1Json);
        log.info("user1PrettyJson:{}", user1PrettyJson);

        User user = JsonUtil.string2Obj(user1Json, User.class);
        log.info("user:{}", user);
        log.info("user1:{}", user1);

        List<User> userList = Lists.newArrayList();
        userList.add(user);
        userList.add(user1);
        System.out.println("================================");
        String userListStr = JsonUtil.obj2StringPretty(userList);
        log.info("userList:{}", userListStr);
        System.out.println("================================");
        List<User> list = JsonUtil.string2Obj(userListStr, new TypeReference<List<User>>() {});
    }

}
