package com.tw.auction.common.exception;

import lombok.Getter;

@Getter
public class NetWorkException extends RuntimeException {
    private int code;
    private String message;

    public NetWorkException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
