package com.qupp.client.network.bean;


import java.io.Serializable;

/**
 * 二层modelbean
 */


public class EntityForSimpleB implements Serializable{

    private String code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private boolean data;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isData() {
        return data;
    }
}

