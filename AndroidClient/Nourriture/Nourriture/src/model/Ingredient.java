package model;

import java.util.List;

import android.graphics.Bitmap;

public class Ingredient {
    private String name;
    private String id;
    private String description;
    private List<String> category;
    private List<String> nutrients;
    private String imgUrl;
    private Bitmap img;
	public static final String URL_API = "https://192.168.0.103:8081";

    
    public Ingredient(String id_, String imgUrl_, String description_, String name_, List<String> category_, List<String> nutrients_) {
        name = name_;
        id = id_;
        description = description_;
        category = category_;
        nutrients = nutrients_;
        imgUrl = URL_API + imgUrl_;
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
    public List<String> getCategory() {
    	return category;
    }
    
    public void setCategory(List<String> category_) {
    	category = category_;
    }
    public List<String> getNutrients() {
    	return nutrients;
    }
    
    public void setNutrients(List<String> nutrients_) {
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
}
