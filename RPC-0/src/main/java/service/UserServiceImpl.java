package service;

import pojo.User;

import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    @Override
    public User getUserById(Integer id) {
        System.out.println("客户端查询了" + id + "的用户");
        //模拟从数据库中查询数据的行为
        Random random = new Random();
        User user = User.builder().userName(UUID.randomUUID().toString())
                .id(id)
                .sex(random.nextBoolean()).build();
        return user;
    }
}
