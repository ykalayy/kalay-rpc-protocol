package com.kalay.rpc.example;

import com.kalay.rpc.client.KalayRpcClient;
import com.kalay.rpc.client.ResponseHandler;
import com.kalay.rpc.common.ProtoCommon;
import com.kalay.rpc.transport.message.v1.KalayRpcCommand;
import com.kalay.rpc.transport.message.v1.Type;

public class ClientTest {

    public static void main(String[] args) throws InterruptedException {

        KalayRpcClient rpcClient = new KalayRpcClient("localhost", 5000);
        rpcClient.call(new KalayRpcCommand(Type.REQUEST, ProtoCommon.VERSION, "exampleRemoteFunc"), new ResponseHandler() {
            @Override
            public void handleResponse(String response) {
                System.out.println("This is the response from the server.\nMessage: " + response);
            }
        });
    }
}
