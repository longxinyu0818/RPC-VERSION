package client;

import lombok.AllArgsConstructor;
import pojo.RPCRequest;
import pojo.RPCResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 而RPCClientProxy类中需要加入一个RPCClient类变量即可，
 * 传入不同的client(simple,netty), 即可调用公共的接口sendRequest发送请求，所以客户端代码结构很清晰了:
 * <p>
 * RPCClient: 不同的网络连接，网络传输方式的客户端分别实现这个接口
 * <p>
 * XXXRPCClient: 具体实现类
 * <p>
 * RPCClientProxy： 动态代理Service类，封装不同的Service请求为Request对象，并且持有一个RPCClient对象，
 * 负责与服务端的通信，
 */

@AllArgsConstructor
public class RPCClientProxy implements InvocationHandler {
    // 传入参数Service接口的class对象，反射封装成一个request
    RPCClient rpcClient;


    // jdk 动态代理， 每一次代理对象调用方法，会经过此方法增强（反射获取request对象，socket发送至客户端）
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // request的构建，使用了lombok中的builder，代码简洁
        RPCRequest request = RPCRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramsTypes(method.getParameterTypes()).build();

        //数据传输
        RPCResponse response = rpcClient.sendRequest(request);
        return response.getData();
    }

    public <T> T getProxy(Class<T> clazz) {
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T) o;
    }
}
