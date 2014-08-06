package com.yuan.yuanisnosay.ui.adpater;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ItemData_newcomment implements Serializable{
	private String HisNickname;
	private String NewComment;
	private int RawConfessid;
	private String RawConfess;
	
	

	public ItemData_newcomment(String hisnickname,String newcomment,String rawconfess,int rawconfessid){
	
		this.HisNickname=hisnickname;
		this.NewComment=newcomment;
		this.RawConfess=rawconfess;
		this.RawConfessid=rawconfessid;
	}
	
	public ItemData_newcomment() {
		// TODO Auto-generated constructor stub
	}

	public void sethisNickname(String hisnickname){
		HisNickname=hisnickname;
	}
	public String gethisNickname(){
		return HisNickname+" : ";
	}
	public void setNewComment(String newcomment){
		NewComment=newcomment;
	}
	public String getNewComment(){
		return NewComment;
	}
	public void setRawConfess(String rawconfess){
		RawConfess=rawconfess;
	}
	public String getRawConfess(){
		return "回复我的评论 : "+RawConfess;
	}
	public void setRawConfessid(int rawconfessid){
		RawConfessid = rawconfessid;
	}
	public int getRawConfessid(){
		return RawConfessid;
	}
	public String tostring(){
		return gethisNickname()+getNewComment()+getRawConfess();
	}
}
