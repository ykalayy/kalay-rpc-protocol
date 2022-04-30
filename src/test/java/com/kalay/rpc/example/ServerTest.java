package com.kalay.rpc.example;

import com.kalay.rpc.RpcService;
import com.kalay.rpc.server.KalayRpcServer;

public class ServerTest {

    public static void main(String[] args) {
        KalayRpcServer rpcServer = new KalayRpcServer(new RpcService() {
            public String exampleRemoteFunc() {
                return "Hey this is remote function response";
            }
        });
        rpcServer.startService(5000);
    }

}
