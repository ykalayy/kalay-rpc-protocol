Kalay custom rpc protocol
==============================================================================

Custom Rpc server/client project for fun

Example RPC Command
------------------------------------------------------------------------------

```
REQUEST
KALAY 0.0.1
Method:{{RemoteFunction}}
```

Example RPC Response
------------------------------------------------------------------------------

```
{{PlainTextFunctionOutPut}}
```

HelloWorld Server
------------------------------------------------------------------------------
It will bind port 5000. To listen incoming KalayRpcCommands
```java
KalayRpcServer rpcServer = new KalayRpcServer(new RpcService() {
    public String exampleRemoteFunc() {
        return "Hey this is remote function response";
    }
});
rpcServer.startService(5000);
```

Example Client
------------------------------------------------------------------------------
```java
KalayRpcClient rpcClient = new KalayRpcClient("localhost", 5000);
rpcClient.call(new KalayRpcCommand(Type.REQUEST, ProtoCommon.VERSION, "exampleRemoteFunc"), new ResponseHandler() {
    @Override
    public void handleResponse(String response) {
        System.out.println("This is the response from the server.\nMessage: " + response);
    }
});
```
