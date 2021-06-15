package server;

import pojo.User;
import service.UserServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * 此版本背景：
 * 服务端：
 *      有一个User表
 *      UserServiceImpl 实现了UserService接口
 *      UserService里暂时只有一个功能: getUserByUserId(Integer id)
 * 客户端：
 *      传一个Id给服务端，服务端查询到User对象返回给客户端
 *------------------------------------------------------------------
 * 同步： 同步就是发起一个调用后，被调用者未处理完请求之前，调用不返回。
 *
 * 异步： 异步就是发起一个调用后，立刻得到被调用者的回应表示已接收到请求，但是被调用者并没有返回结果，
 *       此时我们可以处理其他的请求，被调用者通常依靠事件，回调等机制来通知调用者其返回结果。
 *
 * 阻塞： 阻塞就是发起一个请求，调用者一直等待请求结果返回，也就是当前线程会被挂起，无法从事其他任务，只有当条件就绪才能继续。
 *
 * 非阻塞： 非阻塞就是发起一个请求，调用者不用一直等着结果返回，可以先去干其他事情。
 * BIO:同步阻塞I/O模式
 * NIO:同步非阻塞的I/O模型
 * AIO:也就是 NIO 2。在 Java 7 中引入了 NIO 的改进版 NIO 2,它是异步非阻塞的IO模型
 *
 *
 * 总结：
 * 这个例子以不到百行的代码，实现了客户端与服务端的一个远程过程调用，非常适合上手，当然它是及其不完善的，
 * 甚至连消息格式都没有统一，我们将在接下来的版本更新中逐渐完善它。
 *
 * 此版本的缺点：
 * 1.只能调用服务端Service唯一确定的方法，如果有两个方法需要调用呢?（Reuest需要抽象）
 * 2.返回值只支持User对象，如果需要传一个字符串或者一个Dog，String对象呢（Response需要抽象）
 * 3.客户端不够通用，host，port， 与调用的方法都特定（需要抽象）
 *
 */


public class RPCServer {
    public static void main(String[] args) throws IOException {
        UserServiceImpl userService = new UserServiceImpl();
        try {
            ServerSocket serverSocket = new ServerSocket(8899);
            System.out.println("服务端启动了");
            //BIO的方式监听socket
            while (true) {
                Socket socket = serverSocket.accept(); //等待接收客户端的连接的方式监听请求
                //开启一个线程去处理
                new Thread(() -> {
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        //读取从客户端传来的id
                        Integer id = ois.readInt();
                        User userByUserId = userService.getUserById(id);
                        //写入User对象给客户端
                        oos.writeObject(userByUserId);
                        oos.flush();
                    } catch (IOException e) {
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
