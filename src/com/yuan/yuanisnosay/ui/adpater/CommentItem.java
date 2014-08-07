package com.yuan.yuanisnosay.ui.adpater;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class CommentItem implements Serializable {
	
	private int confessID;
	private int commentID;
	private String commentContent;
	private String authorName;
	private Date publishDate;
	private String headPic;
	
	public int getConfessID() {
		return confessID;
	}
	public void setConfessID(int confessID) {
		this.confessID = confessID;
	}
	public int getCommentID() {
		return commentID;
	}
	public void setCommentID(int commentID) {
		this.commentID = commentID;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public String getHeadPic() {
		return headPic;
	}
	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}
	
	public String toString() {
		return "CommentItem [id=" + confessID + ", content="
				+ commentContent + "]";
		
	}
	public CommentItem(JSONObject json) throws JSONException {
		//this.authorName = json.getString("");
	}
	
}
