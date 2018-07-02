package com.ppdai.raptor.codegen.java.option;


import java.util.Objects;

/**
 * @author zhangchengxi
 * Date 2018/4/27
 */
public enum Method {
    //http方法
    GET(0, "GET"),
    HEAD(1, "HEAD"),
    POST(2, "POST"),
    PUT(3, "PUT"),
    PATCH(4, "PATCH"),
    DELETE(5, "DELETE"),
    OPTIONS(6, "OPTIONS"),
    TRACE(7, "TRACE");

    private int tag;
    private String name;

    Method(int tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    public static Method get(String name) {
        if (Objects.isNull(name)) {
            return null;
        }
        String upperCase = name.toUpperCase();
        return Method.valueOf(upperCase);
    }

    public static void main(String[] args) {
        System.out.println(Method.get("head").tag);
    }

    public int getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

}
