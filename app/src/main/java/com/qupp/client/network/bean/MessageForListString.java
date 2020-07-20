package com.qupp.client.network.bean;


import java.util.ArrayList;

/**
 * 一层modelbean
 * author: MrWang on 2019/7/15
 * email:773630555@qq.com
 * date: on 2019/7/15 15:50
 */

public class MessageForListString {

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
	private ArrayList<String> data;

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public ArrayList<String> getData() {
		return data;
	}
}
