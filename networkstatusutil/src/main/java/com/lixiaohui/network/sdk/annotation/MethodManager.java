package com.lixiaohui.network.sdk.annotation;

import com.lixiaohui.network.sdk.type.NetType;

import java.lang.reflect.Method;

/**
 * @Author: Lee
 * @Date: 2019-08-07
 * @Desc: 保存符合要求的网络监听注解反方法，封装类
 */
public class MethodManager {
    //参数类型
    private Class<?> type;

    //网络类型
    private NetType netType;

    //需要执行的方法
    private Method method;

    public MethodManager(Class<?> type, NetType netType, Method method) {
        this.type = type;
        this.netType = netType;
        this.method = method;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public NetType getNetType() {
        return netType;
    }

    public void setNetType(NetType netType) {
        this.netType = netType;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
