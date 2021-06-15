package service;

import pojo.User;

public interface UserService {
    User getUserById(Integer id);

    //添加一个功能， 服务端接受request请求，并调用request中的对应的方法
    Integer insertUserId(User user);
}
