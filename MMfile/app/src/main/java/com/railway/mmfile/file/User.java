package com.railway.mmfile.file;
import cn.bmob.v3.BmobUser;

public class User extends BmobUser{
	private String nickname;
	public String getNickname() {
		return this.nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
