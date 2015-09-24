package com.tencent.avsdk.c2c;

import android.text.TextUtils;

import com.tencent.avsdk.Utils.ChnToSpell;

public class UserInfo {

	private int id;
	public String name;
	private String nick;	
	private int avatar;
	private String header;
	//临时测试用
    public String openId;
    public String openKey;

	public UserInfo() {
		// TODO Auto-generated constructor stub
	}

	public UserInfo(int id, int avatar, int count, String name,
			String time, String msg,String header) {
		super();
		this.id = id;
		this.avatar = avatar;
		this.name = name;
		this.header = header;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getavatar() {
		return avatar;
	}
	
	public void setavatar(int avatar) {
		this.avatar = avatar;
	}
	
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	//	ProcessHeader();
	}
	
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}	


	public boolean equals(Object object) {
		if (object == null)
			return false;

		if (object == this)
			return true;

		if (object instanceof UserInfo) {
			UserInfo entity = (UserInfo) object;
			if (entity.id == this.id)
				return true;
		}
		return false;
	}

	public void ProcessHeader() {
		String headerName = null;
		if (!TextUtils.isEmpty(nick)) {
			headerName = nick;
		} else {
			headerName = name;
		}
		if (Character.isDigit(headerName.charAt(0))) {
			header = "#";
		} else {
			String tmpStr = ChnToSpell.MakeSpellCode(headerName.substring(0, 1), ChnToSpell.TRANS_MODE_PINYIN_INITIAL);
			header = tmpStr.substring(0,1).toUpperCase();
			char tmpHeader = header.toLowerCase().charAt(0);
			if (tmpHeader < 'a' || tmpHeader > 'z') {
				header = "#";
			}
		}
	}	
	
	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", avatar=" + avatar + ", " +
				" name=" + name + "]";
	}

}
