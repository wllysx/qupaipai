package com.qupp.client.network.bean;


import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * 一层地址model
 * author: MrWang on 2019/7/15
 * email:773630555@qq.com
 * date: on 2019/7/15 15:50
 */

public class MessageForAddress {

	/**
	 * 返回码
	 */
	private String code;
	/**
	 * 返回信息
	 */
	private String msg;
	/**
	 * 返回数据
	 */
	private ArrayList<JsonBean> data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ArrayList<JsonBean> getData() {
		return data;
	}

	public void setData(ArrayList<JsonBean> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
