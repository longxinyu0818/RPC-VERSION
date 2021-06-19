package server;

import service.BlogService;
import service.UserService;

public class TestServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();
//        Map<String,Object> serviceProvide = new HashMap<>();
//        //暴露两个服务器的端口，即在RPCServer中加一个HashMap
//        serviceProvide.put("com.service.UserService",userService);
//        serviceProvide.put("com.service.BlogService",blogService);
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.providerServiceInterface(userService);
        serviceProvider.providerServiceInterface(blogService);

        RPCServer rpcServer = new ThreadPoolRPCServer(serviceProvider);
        rpcServer.start(8899);
    }
}

// 这里先不去讨论实现其中的细节，因为这里还应该进行优化，我们先去把服务端代码松耦合，再回过来讨论