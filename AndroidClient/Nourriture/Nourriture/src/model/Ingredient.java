package model;

import java.util.List;

import android.graphics.Bitmap;

public class Ingredient {
    private String name;
    private String id;
    private String description;
    private String category;
    private String nutrients;
    private String imgUrl;
    private Bitmap img;
	private boolean checked;
	public static final String URL_API = "https://54.64.212.101";
	//public static final String URL_API = "https://192.168.0.103:8081";

    
    public Ingredient(String id_, String imgUrl_, String description_, String name_, String category_, String nutrients_) {
        name = name_;
        id = id_;
        description = description_;
        category = category_;
        nutrients = nutrients_;
        imgUrl = imgUrl_;
    }

    
    
    public String getId() {
    	return id;
    }
    
    public void setId(String id_) {
    	id = id_;
    }
    
    public String getName() {
    	return name;
    }
    
    public void setName(String name_) {
    	name = name_;
    }
    public String getDescription() {
    	return description;
    }
    
    public void setDescription(String description_) {
    	description = description_;
    }
    public String getCategory() {
    	return category;
    }
    
    public void setCategory(String category_) {
    	category = category_;
    }
    public String getNutrients() {
    	return nutrients;
    }
    
    public void setNutrients(String nutrients_) {
    	nutrients = nutrients_;
    }
    
    public String getImgUrl() {
    	return imgUrl;
    }
    
    public void setImgUrl(String imgUrl_) {
    	imgUrl = imgUrl_;
    }
    
    public void setImg(Bitmap img_) {
    	img = img_;
    }
    
    public Bitmap getImg() {
    	return img;
    }



	public void setChecked(boolean b) {
		// TODO Auto-generated method stub
		checked = b;
	}
	
	public boolean getChecked() {
		return checked;
	}
}
