package model;

import java.io.File;
import java.util.Date;

import android.graphics.Bitmap;

public class Moment {
	String description;
	String imgUrl;
	Bitmap img;
	File imgFile;
	String id;
	String authorId;
	Date date;
	
	public Moment(String id_, String d,String i, String a, Date date_)
	{
		id = id_;
		description = d;
		imgUrl = i;
		authorId = a;
		date = date_;
	}
	
	public String getId() {
		return id;
	}

	public String getAuthor() {
		// TODO Auto-generated method stub
		return authorId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Date getDate() {
		return date;
	}
	
	public File getImgFile() {
		return imgFile;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	
	public Bitmap getImg() {
		return img;
	}

	public void setImg(Bitmap current_pic_) {
		// TODO Auto-generated method stub
		img = current_pic_;
	}
	public void setImgFile(File current_pic_) {
		// TODO Auto-generated method stub
		imgFile = current_pic_;
	}
	public void setImgPath(String current_pic_) {
		// TODO Auto-generated method stub
		imgUrl = current_pic_;
	}
}
