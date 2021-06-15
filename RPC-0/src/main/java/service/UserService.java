package service;

import pojo.User;

public interface UserService {
    //客户端通过这个接口调用服务端的实体类
    User getUserById(Integer id);
}
