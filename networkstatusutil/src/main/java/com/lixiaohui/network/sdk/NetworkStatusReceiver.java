package com.lixiaohui.network.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lixiaohui.network.sdk.annotation.MethodManager;
import com.lixiaohui.network.sdk.annotation.Network;
import com.lixiaohui.network.sdk.type.NetType;
import com.lixiaohui.network.sdk.utils.Constants;
import com.lixiaohui.network.sdk.utils.NetworkUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Lee
 * @Date: 2019-08-07
 * @Desc:
 */
public class NetworkStatusReceiver extends BroadcastReceiver {
    private NetType netType;
    private Map<Object, List<MethodManager>> networkList;

    public NetworkStatusReceiver() {
        this.netType =NetType.NONE;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null || intent.getAction() == null){
            return;
        }

        //处理广播事件
        if(Constants.ANDROID_NET_CHANGE_ACTION.equalsIgnoreCase(intent.getAction())){
            netType = null;
            if(NetworkUtils.isNetworkAvailable(context)){
                netType = NetworkUtils.getNetType(context);
            }else{
                netType = NetType.NONE;
            }

        }

        //通知所有注册的方法，网络发生变化
        post(netType);
    }

    /**
     * 给所有注册者发送事件
     * @param netType
     */
    private void post(NetType netType) {
        Set<Object> set = networkList.keySet();
        for (final Object getter : set) {
            List<MethodManager> methodList = networkList.get(getter);
            if(methodList != null){
                //只有匹配的type，再去发送事件
                for (final MethodManager methodManager : methodList) {
                    if(methodManager.getType().isAssignableFrom(netType.getClass())){
                        switch (methodManager.getNetType()){
                            case AUTO:
                                invoke(methodManager,getter,netType);
                                break;
                            case WIFI:
                                if(netType == NetType.WIFI || netType == NetType.NONE){
                                    invoke(methodManager,getter,netType);
                                }
                                break;
                            case CMNET:
                                if(netType == NetType.CMNET || netType == NetType.NONE){
                                    invoke(methodManager,getter,netType);
                                }
                                break;
                            case CMWAP:
                                if(netType == NetType.CMWAP || netType == NetType.NONE){
                                    invoke(methodManager,getter,netType);
                                }
                                break;
                                default:
                                    break;
                        }
                    }

                }
            }
        }
    }

    /**
     * 执行一个事件
     * @param methodManager
     * @param getter
     * @param netType
     */
    private void invoke(MethodManager methodManager, Object getter, NetType netType) {
        Method method = methodManager.getMethod();
        try {
            method.invoke(getter,netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * 注册
     * @param register
     */
    public void registerObserver(Object register){
        //获取所有方法
        List<MethodManager> methodList = networkList.get(register);
        if(methodList == null){
            methodList = findAnnotationMethod(register);
            networkList.put(register,methodList);
        }
    }

    /**
     * 找到所有的方法
     * @param register
     * @return
     */
    private List<MethodManager> findAnnotationMethod(Object register) {
        List<MethodManager> methodManagers = new ArrayList<>();

        Class<?> clazz= register.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Network network = method.getAnnotation(Network.class);
            if(network == null){
                continue;
            }
            //返回值
            Type returnType = method.getGenericReturnType();
            if(!"void".equals(returnType.toString())){
                throw new RuntimeException(method.getName()+"方法返回必须是void");
            }
            //参数判断
            Class<?>[] parameterTypes = method.getParameterTypes();
            if(parameterTypes.length !=1){
                throw new RuntimeException(method.getName()+"方法有且只有一个参数");
            }

            MethodManager methodManager = new MethodManager(parameterTypes[0], network.netType(), method);
            methodManagers.add(methodManager);
        }
        return methodManagers;
    }

    /**
     * 反注册
     * @param register
     */
    public void unregisterObserver(Object register){
        if(!networkList.isEmpty()){
            networkList.remove(register);
        }
    }

    /**
     * 反注册
     */
    public void unregisterAllObserver(){
        if(!networkList.isEmpty()){
            networkList.clear();
        }
        //是否需要
        NetworkManager.getInstance().getApplication().unregisterReceiver(this);
    }


}
