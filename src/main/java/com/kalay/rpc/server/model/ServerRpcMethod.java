package com.kalay.rpc.server.model;

import java.lang.reflect.Method;

public class ServerRpcMethod {

    private Object serverInstance;
    private Method targetMethod;

    public ServerRpcMethod() {
    }

    public ServerRpcMethod(Object serverInstance, Method targetMethod) {
        this.serverInstance = serverInstance;
        this.targetMethod = targetMethod;
    }

    public Object getServerInstance() {
        return serverInstance;
    }

    public void setServerInstance(Object serverInstance) {
        this.serverInstance = serverInstance;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(Method targetMethod) {
        this.targetMethod = targetMethod;
    }
}
