package com.mmall.common;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T date;

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T date) {
        this.status = status;
        this.date = date;
    }

    private ServerResponse(int status, String msg, T date) {
        this.status = status;
        this.msg = msg;
        this.date = date;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
