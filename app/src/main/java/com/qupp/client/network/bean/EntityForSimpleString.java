package com.qupp.client.network.bean;


import java.io.Serializable;

/**
 * 二层modelbean
 * author: MrWang on 2019/7/15
 * email:773630555@qq.com
 * date: on 2019/7/15 15:50
 */


public class EntityForSimpleString implements Serializable{

    private String code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private String data;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getData() {
        return data;
    }
}

