package com.rsh.framework.weixin.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * 封装 API 响应结果，将 json 字符串转换成 java 数据类型
 * Created By Rsh
 *
 * @Description
 * @Date: 2017/12/21
 * @Time: 15:50
 */
public class ApiResult implements Serializable {
    private static final long serialVersionUID = -8344781685793761300L;

    private String json;
    private Map<String, Object> attrs;


    /**
     * 通过 json 构造 ApiResult
     *
     * @param jsonStr json字符串
     */
    public ApiResult(String jsonStr) {
        this.json = jsonStr;
        try {
            Map<String, Object> temp = JSON.parseObject(jsonStr, Map.class);
            this.attrs = temp;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过 json 创建 ApiResult 对象，等价于 new ApiResult(jsonStr)
     *
     * @param jsonStr json字符串
     * @return {ApiResult}
     */
    public static ApiResult create(String jsonStr) {
        return new ApiResult(jsonStr);
    }

    public String getJson() {
        return json;
    }

    public String toString() {
        return getJson();
    }

    /**
     * APi 请求是否成功返回
     *
     * @return {boolean}
     */
    public boolean isSucceed() {
        Integer errorCode = getErrorCode();
        return (errorCode != null && errorCode == 0);
    }

    public Integer getErrorCode() {
        return getInt("errcode");
    }

    public String getErrorMsg() {
        Integer errorCode = getErrorCode();
        if (errorCode != null) {
            String result = ApiResultErrorCode.get(errorCode);
            if (result != null) {
                return result;
            }
        }
        return (String) attrs.get("errmsg");
    }

    public <T> T get(String name) {
        return (T) attrs.get(name);
    }

    public String getStr(String name) {
        return (String) attrs.get(name);
    }

    public Integer getInt(String name) {
        Number number = (Number) attrs.get(name);
        return number == null ? null : number.intValue();
    }

    public Long getLong(String name) {
        Number number = (Number) attrs.get(name);
        return number == null ? null : number.longValue();
    }

    public BigInteger getBigInteger(String name) {
        return (BigInteger) attrs.get(name);
    }

    public Double getDouble(String name) {
        return (Double) attrs.get(name);
    }

    public BigDecimal getBigDecimal(String name) {
        return (BigDecimal) attrs.get(name);
    }

    public Boolean getBoolean(String name) {
        return (Boolean) attrs.get(name);
    }

    public List getList(String name) {
        return (List) attrs.get(name);
    }

    public Map getMap(String name) {
        return (Map) attrs.get(name);
    }

    public Map<String, Object> getAttrs() {
        return this.attrs;
    }

}