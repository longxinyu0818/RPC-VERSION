package pojo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 上个例子中response传输的是User对象，显然在一个应用中我们不可能只传输一种类型的数据
 * 由此我们将传输对象抽象成为Object
 * Rpc需要经过网络传输，有可能失败，类似于http，引入状态码和状态信息表示服务调用成功还是失败
 * <p>
 * 因此在网络传输过程都是request与response格式的数据了，客户端与服务器端就要负责封装与解析以上结构数据
 */

@Data
@Builder
public class RPCResponse implements Serializable {
    //状态信息
    private int code;
    private String message;
    //具体数据
    private Object data;

    public static RPCResponse success(Object data) {
        return RPCResponse.builder().code(200).data(data).build();
    }

    public static RPCResponse fail() {
        return RPCResponse.builder().code(500).message("服务器发生错误").build();
    }

}
