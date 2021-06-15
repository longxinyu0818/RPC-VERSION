package pojo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RPCRequest implements Serializable {
    private String interfaceName; //服务类名，客户端只知道接口名，在服务端中用接口指向实现类
    private String methodName; // 方法名
    private Object[] params; // 参数名
    private Class<?>[] paramsTypes; // 参数类型
}
