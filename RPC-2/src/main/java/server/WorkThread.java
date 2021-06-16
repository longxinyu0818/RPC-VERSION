package server;

import lombok.AllArgsConstructor;
import pojo.RPCRequest;
import pojo.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * 工作任务类，从服务端代码分离出来，简化服务端代码，单一职责原则
 * <p>
 * 这里负责解析得到的request请求，执行服务方法，返回给客户端
 * 1. 从request得到interfaceName 2. 根据interfaceName在serviceProvide Map中获取服务端的实现类
 * 3. 从request中得到方法名，参数， 利用反射执行服务中的方法 4. 得到结果，封装成response，写入socket
 */
@AllArgsConstructor
public class WorkThread implements Runnable {
    private Socket socket;
    private ServiceProvider serviceProvider;

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //获取客户端传来的request
            RPCRequest request = (RPCRequest) ois.readObject();
            //反射调用服务端的方法获得返回值
            RPCResponse response = getResponse(request);
            //写如客户端
            oos.writeObject(response);
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("从IO中读取数据失败");
        }
    }

    private RPCResponse getResponse(RPCRequest request) {
        //获取服务器名
        String interfaceName = request.getInterfaceName();
        //得到对应服务端相应的服务类
        Object server = serviceProvider.getService(interfaceName);
        //反射调用方法
        Method method = null;
        try {
            method = server.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
            Object invoke = method.invoke(server, request.getParams());
            return RPCResponse.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RPCResponse.fail();
        }
    }
}
