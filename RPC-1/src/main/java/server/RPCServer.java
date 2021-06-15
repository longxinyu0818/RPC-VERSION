package server;

import pojo.RPCRequest;
import pojo.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * 服务端接受解析reuqest与封装发送response对象
 */
public class RPCServer {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        try {
            ServerSocket serverSocket = new ServerSocket(8899);
            System.out.println("服务端启动了");
            //BIO的方式监听socket
            while (true) {
                Socket socket = serverSocket.accept(); //等待接收客户端的连接的方式监听请求
                //开启一个线程去处理,，这个类负责的功能太复杂，以后代码重构中，这部分功能要分离出来
                new Thread(() -> {
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        //读取从客户端传来的request
                        RPCRequest request = (RPCRequest) ois.readObject();
                        //反射调用对应方法
                        Method method = userService.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
                        Object invoke = method.invoke(userService, request.getParams());
                        //封装，写入response对象
                        oos.writeObject(RPCResponse.success(invoke));
                        oos.flush();
                    } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        System.out.println("从IO中读取数据错误");
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务端启动失败");
        }
    }
}

