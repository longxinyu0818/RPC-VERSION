package client;

import jdk.nashorn.internal.runtime.Scope;
import lombok.AllArgsConstructor;
import pojo.RPCRequest;
import pojo.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 重构客户端代码:
 * 客户端的代码太乱了， 我们先进行代码重构，才有利于后面使用netty的方式实现客户端，
 * 使之不同方式网络连接的客户端有着同样的结构，同样的api
 * <p>
 * 假如我们现在已经有了两个客户端：SimpleRPCClient(使用java BIO的方式)， NettyRPCClient
 * （使用netty进行网络传输），那么它们两者的共性是啥?发送请求与得到response是共性， 而建立连接与发送请求的方式是不同点。
 */

@AllArgsConstructor
// SimpleRPCClient实现这个接口，不同的网络方式有着不同的实现
public class SimpleRPCClient implements RPCClient {
    private String host;
    private int port;

    @Override
    public RPCResponse sendRequest(RPCRequest request) {
        // 客户端发起一次请求调用，Socket建立连接，发起请求Request，得到响应Response
        // 这里的request是封装好的，不同的service需要进行不同的封装， 客户端只知道Service接口，
        // 需要一层动态代理根据反射封装不同的Service
        try {
            Socket socket = new Socket(host, port);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            System.out.println(request);
            oos.writeObject(request);
            oos.flush();

            RPCResponse response = (RPCResponse) ois.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            //e.printStackTrace();
            System.out.println();
            return null;
        }
    }
}
