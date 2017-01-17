package com.protostar.billnstock.until.data;

public class ServerMsg {
	private String msg;
	private boolean returnBool;
	private double returnBalance;

	public double getReturnBalance() {
		return returnBalance;
	}

	public void setReturnBalance(double returnBalance) {
		this.returnBalance = returnBalance;
	}

	public boolean getReturnBool() {
		return returnBool;
	}

	public void setReturnBool(boolean returnBool) {
		this.returnBool = returnBool;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private String token;

}
