package com.yuan.yuanisnosay.ui.adpater;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.util.Log;

import com.yuan.yuanisnosay.confessandprofile.TencentLocationModule;

/**
 * 表白条目
 * @author 志彬
 *
 */
public class ConfessItem implements Serializable{
	public static final String TAG="yuan_ConfessItem";
	public static final int TYPE_NORMAL=0;
	public static final int TYPE_JUSTPOST=1;
	
	private int type;
	private int id;
	private String content;
	private String author;
	private Date publishDate;
	private String icon;
	private String picture;
	private int flowersCount;
	private int commentCount;
	private TencentLocationModule position;
	
	//----test-------------
	static int size=0;
	static LinkedList<ConfessItem> testList=new LinkedList<ConfessItem>();
//	static String testIcon="file:///sdcard/Universal Image Loader @#&=+-_.,!()~'%20.png";
	static String testIcon="http://10.66.95.61:8080/p/";
	static TencentLocationModule location=new TencentLocationModule();
	static Random random=new Random(System.currentTimeMillis());
	static public List<ConfessItem> getConfess(int start,int n){
		List<ConfessItem> result=new ArrayList<ConfessItem>();
		for(int i=0;i<n;i++){
			if(start+i>=testList.size()) break;
			ConfessItem item=testList.get(start+i);
			result.add(item);
		}
		return result;
	}
	static public void addConfess(int n){
		for(int i=0;i<n;i++){
			ConfessItem item=new ConfessItem(TYPE_NORMAL, size, size++ +"i love you", "tobynma", new Date(), testIcon+random.nextInt(15), testIcon, 1,1,location);
			testList.addFirst(item);
		}
	}
	static{
		location.setRegionName("腾讯大厦");
		for(int i=0;i<10;i++){
			ConfessItem item=new ConfessItem(TYPE_NORMAL, size, size++ +"i love you", "tobynma", new Date(), testIcon+random.nextInt(15), testIcon, 1,1,location);
			testList.addFirst(item);
		}
		new Thread(){
			@Override
			public void run() {
				try {
					sleep(2000);
					addConfess(2);
					Log.e(TAG, "testList:"+testList.toString());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
		Log.e(TAG, "testList:"+testList.toString());
	}
	
	//----test-------------
	
	private ConfessItem(){
	}
	
	public ConfessItem(int type, int id, String content, String author,
			Date publishDate, String icon, String picture, int flowersCount,
			int commentCount, TencentLocationModule position) {
		super();
		this.type = type;
		this.id = id;
		this.content = content;
		this.author = author;
		this.publishDate = publishDate;
		this.icon = icon;
		this.picture = picture;
		this.flowersCount = flowersCount;
		this.commentCount = commentCount;
		this.position = position;
	}

	public int getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public String getAuthor() {
		return author;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public String getIcon() {
		return icon;
	}

	public String getPicture() {
		return picture;
	}

	public int getFlowersCount() {
		return flowersCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public TencentLocationModule getPosition() {
		return position;
	}

	public static String getTestIcon() {
		return testIcon;
	}
//	@Override
//	public String toString() {
//		return "ConfessItem [type=" + type + ", id=" + id + ", content="
//				+ content + ", author=" + author + ", publishDate="
//				+ publishDate + ", icon=" + icon + ", picture=" + picture
//				+ ", flowersCount=" + flowersCount + ", commentCount="
//				+ commentCount + ", position=" + position + "]";
//	}
	
	@Override
	public String toString() {
		return "ConfessItem [id=" + id + ", content="
				+ content + "]";
	}
	
}
