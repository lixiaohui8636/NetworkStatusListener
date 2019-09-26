package com.lixiaohui.network.sdk.features;

import com.lixiaohui.network.sdk.features.annotation.Network;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Lee
 * @Date: 2019-09-26
 * @Desc:
 */
public class ObserverManager {
    private static ObserverManager observerManager;

    private Map<Object, List<ObserverMethod>> networkList;

    private ObserverManager() {
        networkList = new HashMap<>();
    }

    public static ObserverManager getInstance() {
        if (observerManager == null) {
            synchronized (ObserverManager.class) {
                if (observerManager == null) {
                    observerManager = new ObserverManager();
                }
            }
        }
        return observerManager;
    }

    public void registerObserver(Object register) {
        List<ObserverMethod> methodList = networkList.get(register);
        if (methodList == null) {
            methodList = findAnnotationMethod(register);
            networkList.put(register, methodList);
        }
    }

    public void unregisterObserver(Object register) {
        if (!networkList.isEmpty()) {
            networkList.remove(register);
        }
    }

    public void unregisterAllObserver() {
        if (!networkList.isEmpty()) {
            networkList.clear();
        }
    }

    protected void post(NetType netType) {
        Set<Object> set = networkList.keySet();
        for (final Object getter : set) {
            List<ObserverMethod> methodList = networkList.get(getter);
            if (methodList != null) {
                //只有匹配的type，再去发送事件
                for (final ObserverMethod observerMethod : methodList) {
                    if (observerMethod.getType().isAssignableFrom(netType.getClass())) {
                        switch (observerMethod.getNetType()) {
                            case AUTO:
                                invoke(observerMethod, getter, netType);
                                break;
                            case WIFI:
                                if (netType == NetType.WIFI || netType == NetType.DISABLE) {
                                    invoke(observerMethod, getter, netType);
                                }
                                break;
                            case CMNET:
                                if (netType == NetType.CMNET || netType == NetType.DISABLE) {
                                    invoke(observerMethod, getter, netType);
                                }
                                break;
                            case CMWAP:
                                if (netType == NetType.CMWAP || netType == NetType.DISABLE) {
                                    invoke(observerMethod, getter, netType);
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
     * 找到所有的方法
     *
     * @param register
     * @return
     */
    private List<ObserverMethod> findAnnotationMethod(Object register) {
        List<ObserverMethod> observerMethods = new ArrayList<>();

        Class<?> clazz = register.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Network network = method.getAnnotation(Network.class);
            if (network == null) {
                continue;
            }
            //返回值
            Type returnType = method.getGenericReturnType();
            if (!"void".equals(returnType.toString())) {
                throw new RuntimeException(method.getName() + "方法返回必须是void");
            }
            //参数判断
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException(method.getName() + "方法有且只有一个参数");
            }

            ObserverMethod observerMethod = new ObserverMethod(parameterTypes[0], network.netType(), method);
            observerMethods.add(observerMethod);
        }
        return observerMethods;
    }

    /**
     * 反射执行对象的方法
     * @param observerMethod
     * @param getter
     * @param netType
     */
    private void invoke(ObserverMethod observerMethod, Object getter, NetType netType) {
        Method method = observerMethod.getMethod();
        try {
            method.invoke(getter, netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
