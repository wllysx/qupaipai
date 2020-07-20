package com.qupp.client.network.bean;


import com.google.gson.Gson;

/**
 * 一层modelbean
 */

public class MessageForSimple {

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
	private EntityForSimple data;

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

	public EntityForSimple getData() {
		return data;
	}

	public void setData(EntityForSimple data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
