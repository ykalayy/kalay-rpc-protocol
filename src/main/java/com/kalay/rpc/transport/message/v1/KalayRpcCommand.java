package com.kalay.rpc.transport.message.v1;

public class KalayRpcCommand {

    private Type type;
    private String version;
    private String method;

    public KalayRpcCommand() {
    }

    public KalayRpcCommand(Type type, String version, String method) {
        this.type = type;
        this.version = version;
        this.method = method;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
