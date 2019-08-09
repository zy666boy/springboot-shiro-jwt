package com.example.springbootshirojwt.util;

import java.io.Serializable;

//前后分离项目中用于统一后端返回数据的类，必不可少
//一般包含状态值和返回的具体内容
public class R<T> implements Serializable {

    // http 状态码
    private int code;

    // 返回信息
    private String msg;

    // 返回的数据
    private T data;

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public R(T data) {
        this.code = 200;
        this.msg = "";
        this.data = data;
    }
    public R(T data,String msg) {
        this.code = 200;
        this.msg = msg;
        this.data = data;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
