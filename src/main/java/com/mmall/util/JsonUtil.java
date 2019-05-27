package com.mmall.util;

import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author colaz
 * @date 2019/5/25
 **/
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 对象的所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);
        // 是否将日期转化成timestamp格式（Long类型， 1970年后的间隔时间）
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        // 日期统一格式为 "yyyy-MM-dd HH:mm:ss"
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        // 反序列化时，忽略在json字符串中存在，但java对象中不存在的对应属性，防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String obj2String(T obj) {
        if (obj == null)
            return null;
        try {
            return obj instanceof String ? (String)obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("parse object to string error", e);
            return null;
        }
    }

    //object转化成"格式化好"的json字符串
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null)
            return null;
        try {
            return obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("parse object to string error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<T> cls) {
        if (StringUtils.isEmpty(str) || cls == null) {
            return null;
        }
        try {
            return cls.equals(String.class) ? (T)str : objectMapper.readValue(str, cls);
        } catch (IOException e) {
            log.warn("parse string to object error", e);
            return null;
        }
    }

    // eg: List<User> userList = JsonUtil.string2Obj(str, List.class);
    // string反序列化成List，默认返回元素被转为LinkedHashMap,不符合要求

    // Map<Category, Product> map = new HashMap<>();
    // 包含多类型的反序列化对象
    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (IOException e) {
            log.warn("parse string to object error", e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            log.warn("parse string to object error", e);
            return null;
        }
    }


    public static void main(String[] args) {
        User user1 = new User();
        user1.setUsername("zhangsan");
        User user2 = new User();
        user2.setUsername("lisi");

        String strUser1 = JsonUtil.obj2String(user1);
        System.out.println(strUser1);
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        String strUserList = JsonUtil.obj2String(userList);
        System.out.println(strUserList);

        List userList2 = JsonUtil.string2Obj(strUserList, List.class);
        List userList3 = JsonUtil.string2Obj(strUserList, new TypeReference<List<User>>() {
        });

        List userList4 = JsonUtil.string2Obj(strUserList, List.class, User.class);
    }

}
