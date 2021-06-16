package client;

import pojo.Blog;
import pojo.User;
import service.BlogService;
import service.UserService;

/**
 *
 * 总结：
 * 在一版本中，我们重构了服务端的代码，代码更加简洁，
 *
 * 添加线程池版的服务端的实现，性能应该会有所提升（未测）
 *
 * 并且服务端终于能够提供不同服务了， 功能更加完善，不再鸡肋
 *
 *
 *
 * 此RPC最大的痛点:
 *  传统的BIO与线程池网络传输性能低
 *
 *
 * */

public class RPCClient {
    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 8899);
        UserService proxy = clientProxy.getProxy(UserService.class);
        // 服务的方法1
        User userByUserId = proxy.getUserById(10);
        System.out.println("从服务端得到的user为：" + userByUserId);
        // 服务的方法2
        User user = User.builder().userName("张三").id(10).sex(true).build();
        Integer integer = proxy.insertUserId(user);
        System.out.println("服务端插入数据：" + integer);

        BlogService blogService = clientProxy.getProxy(BlogService.class);
        Blog  blogById = blogService.getBlogById(1000);
        System.out.println("从服务端得到的blog为："+blogById);
    }
}
