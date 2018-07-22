package com.fewok.lib.process.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fewok.lib.process.util.JsonProcess;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author notreami on 18/7/7.
 */
@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonInput<T> {


    private String operator;

    /**
     * 泛型数据ClassName
     */
    @JSONField(serialize = false)
    private String dataClassName;

    /**
     * 泛型数据JSON str
     */
    @JSONField(serialize = false)
    private String dataJsonValue;

    /**
     * 具体返回数据
     */
    private T data;


    public T getData() {
        if (data != null) {
            return data;
        } else if (dataJsonValue == null || dataJsonValue.trim().length() == 0) {
            return null;
        } else if (dataClassName == null || dataClassName.trim().length() == 0) {
            return null;
        }

        try {
            return (T) JsonProcess.parseObject(dataJsonValue, Class.forName(dataClassName));
        } catch (Exception e) {
            log.error("数据转换异常, clazz={}, value={}", dataClassName, dataJsonValue, e);
            return null;
        }
    }

    public void setData(T data) {
        this.data = data;
        if (data != null) {
            setDataClassName(data.getClass().getName());
            setDataJsonValue(JsonProcess.toJSONString(data));
        }
    }
}
