package client;

import pojo.RPCRequest;
import pojo.RPCResponse;

// 共性抽取出来
public interface RPCClient {
    RPCResponse sendRequest(RPCRequest request);
}
