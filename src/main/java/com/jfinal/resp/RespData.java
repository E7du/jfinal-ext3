/**
 * 
 */
package com.jfinal.resp;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jobsz
 *
 */
public class RespData extends ConcurrentHashMap<String, Object> {

	private static final long serialVersionUID = 8977509309400180937L;
	
	private static final String SUCCESS_EC = "200";
	private static final String FAILURE_EC = "500";
	
	public static final String EC = "ec";
	public static final String EM = "em";
	public static final String DATA = "data";
	
	/**
	 * Get data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData() {
		return (T)this.get(DATA);
	}

	/**
	 * 是否成功
	 * @return
	 */
	public Boolean successed() {
		if (!this.containsKey(EC)) {
			return false;
		}
		return this.get(EC).equals(SUCCESS_EC);
	}
	
	/**
	 * 请求不合法
	 */
	public static RespData invalidReq() {
		return RespData.failure("请求数据不合法");
	}
	
	/**
	 * 成功
	 * @return
	 */
	public static RespData success() {
		return (new RespData(SUCCESS_EC, "请求成功"));
	}
	
	/**
	 * 成功
	 * @return
	 */
	public static RespData success(Object data) {
		return (new RespData(SUCCESS_EC, "请求成功", data));
	}
	
	/**
	 * 失败
	 * @param msg
	 * @return
	 */
	public static RespData failure(String msg) {
		return (new RespData(FAILURE_EC, msg));
	}

	private RespData() {
		
	}
	
	private RespData(String ec, String em) {
		this.setEc(ec);
		this.setEm(em);
	}
	
	private RespData(String ec, String em, Object data) {
		this(ec, em);
		this.setData(data);
	}

	private RespData setEc(String ec) {
		this.put(EC, ec);
		return this;
	}

	private RespData setEm(String em) {
		this.put(EM, em);
		return this;
	}

	private RespData setData(Object data) {
		this.put(DATA, data);
		return this;
	}
}
