package com.tencent.avsdk.Utils;

import com.tencent.TIMMessage;
import com.tencent.avsdk.c2c.UserInfo;

public class RecentEntity extends UserInfo {
	
	private TIMMessage message;
	private String msg;
	private String time;	
	private long count;
	private boolean bGroupMsg;
	

	public RecentEntity() {
		// TODO Auto-generated constructor stub
	}
	
	public void setMessage(TIMMessage message){
		this.message = message;
	}
	
	public TIMMessage getMessage(){
		return message;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public long getCount() {
		return count;
	}
	
	public void setCount(long l) {
		this.count = l;
	}
	
	public boolean getIsGroupMsg() {
		return bGroupMsg;
	}

	public void setIsGroupMsg(boolean bGroupMsg) {
		this.bGroupMsg = bGroupMsg;
	}
	
}
