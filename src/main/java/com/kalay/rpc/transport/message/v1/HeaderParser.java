package com.kalay.rpc.transport.message.v1;

public class HeaderParser {

    private static final String SPACE = " ";
    private static final String COLON = ":";


    private HeaderParser() {
    }

    public static Type parseCommandType(String line) {
        return Type.valueOf(line);
    }

    public static String versionParser(String line) {
        String[] secondHeader = line.split(SPACE);
        if(secondHeader.length < 1) {
            throw new IllegalStateException("Unknown second header. KALAY VERSION parse is failed");
        }
        return secondHeader[1];
    }

    public static String methodParser(String line) {
        String[] methodHeader = line.split(COLON);
        if(methodHeader.length < 1) {
            throw new IllegalStateException("Unknown method header. Method:{{methodName}} parse is failed");
        }
        return methodHeader[1];
    }
}
