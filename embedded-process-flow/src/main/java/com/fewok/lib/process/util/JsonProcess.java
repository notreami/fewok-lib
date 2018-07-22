package com.fewok.lib.process.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 采用FastJson处理JSON
 *
 * @author notreami on 18/7/7.
 */
@Slf4j
public class JsonProcess {
    private static final SerializerFeature[] serializerFeatures = {
            SerializerFeature.QuoteFieldNames,//输出key时使用双引号
            //SerializerFeature.UseSingleQuotes,//输出key时使用单引号
            SerializerFeature.WriteMapNullValue,//Map值为null也输出
            //SerializerFeature.WriteEnumUsingToString,//用枚举toString()值输出
            SerializerFeature.WriteEnumUsingName,//用枚举name()输出
            //SerializerFeature.UseISO8601DateFormat,//Date使用ISO8601格式输出
            SerializerFeature.WriteNullListAsEmpty,//List字段如果为null,输出为[],而非null
            SerializerFeature.WriteNullStringAsEmpty,//字符类型字段如果为null,输出为”“,而非null
            //SerializerFeature.WriteNullNumberAsZero,//数值字段如果为null,输出为0,而非null
            //SerializerFeature.WriteNullBooleanAsFalse,//Boolean字段如果为null,输出为false,而非null
            //SerializerFeature.SkipTransientField,//类中的Get方法对应的Field是transient，序列化时将会被忽略
            //SerializerFeature.SortField,//按字段名称排序后输出
            //SerializerFeature.PrettyFormat,//结果格式化
            //SerializerFeature.WriteClassName,//序列化时写入类型信息
            SerializerFeature.DisableCircularReferenceDetect, //消除对同一对象循环引用的问题
            //SerializerFeature.WriteSlashAsSpecial,//对斜杠’/’进行转义
            //SerializerFeature.BrowserCompatible,//将中文都会序列化，字节数会多一些，但是能兼容IE 6
            SerializerFeature.WriteDateUseDateFormat,//全局修改日期格式  yyyy-MM-dd HH:mm:ss
            //SerializerFeature.NotWriteRootClassName,//
            //SerializerFeature.BeanToArray,//将对象转为array输出
            //SerializerFeature.WriteNonStringKeyAsString,//将非字符串类型的key当成字符串来处理
            //SerializerFeature.NotWriteDefaultValue,//不输出缺省值
            SerializerFeature.BrowserSecure,//防御 xss 安全攻击
            //SerializerFeature.IgnoreNonFieldGetter,//忽略没有getter的字段
            //SerializerFeature.WriteNonStringValueAsString,//非字符串类型的值输出为字符串
            SerializerFeature.IgnoreErrorGetter,//忽略报错的getter方法
            SerializerFeature.WriteBigDecimalAsPlain//BigDecimal 序列化 消除科学计数
            //SerializerFeature.MapSortField//Map的序列化后排序再输出
    };

    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
     * (1)转换为普通JavaBean：readValue(json,Student.class)
     * (2)转换为List,如List<Student>,将第二个参数传递为Student[].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        try {
            return JSON.parseObject(jsonString, clazz);
        } catch (Exception e) {
            log.error("json对象转obj异常 e=", e);
        }
        return null;
    }

    /**
     * List<MyBean> beanList = JSON.parseObject(listString, new TypeReference<List<MyBean>>() {});
     *
     * @param jsonString
     * @param typeReference
     * @return
     */
    public static <T> T parseObject(String jsonString, TypeReference<T> typeReference) {
        try {
            return JSON.parseObject(jsonString, typeReference);
        } catch (Exception e) {
            log.error("json转obj异常 e=", e);
        }
        return null;
    }

    /**
     * Str转list
     *
     * @param jsonString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> parseArray(String jsonString, Class<T> clazz) {
        try {
            return JSON.parseArray(jsonString, clazz);
        } catch (Exception e) {
            log.error("json对象转list异常 e=", e);
        }
        return null;
    }

    /**
     * 把JavaBean转换为json字符串
     *
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return JSON.toJSONString(object, serializerFeatures);
        } catch (Exception e) {
            log.error("Obj转json异常 e=", e);
        }
        return null;
    }
}
