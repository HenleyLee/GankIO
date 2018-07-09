package com.liyunlong.gankio.http;

/**
 * 网络异常
 *
 * @author liyunlong
 * @date 2018/7/9 11:38
 */
public class NetErrorException extends RuntimeException {

    public NetErrorException() {
        super("The network is unavailable.");
    }

}
