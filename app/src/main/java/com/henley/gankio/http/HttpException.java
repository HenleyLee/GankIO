package com.henley.gankio.http;

import java.util.Arrays;

/**
 * Http请求异常
 *
 * @author Henley
 * @date 2018/7/9 11:32
 */
public class HttpException extends Exception {

    private int code;
    private String desc;
    private String message;

    public HttpException(retrofit2.HttpException httpException) {
        if (httpException != null) {
            this.code = httpException.code();
            this.message = httpException.message();
        }
    }

    public HttpException(Throwable throwable) {
        super(throwable);
        this.message = throwable.getMessage();
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            this.code = httpException.getCode();
            this.desc = httpException.getDesc();
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 是否为网络错误
     */
    public boolean isNetworkError() {
        return ExceptionCode.NETWORD_ERROR == code;
    }

    /**
     * 是否为网络不佳
     */
    public boolean isNetworkPoor() {
        return Arrays.asList(408, ExceptionCode.CONNECT_TIMEOUT, ExceptionCode.SOCKET_TIMEOUT).contains(code);
    }

    @Override
    public String toString() {
        return "HttpException{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}
