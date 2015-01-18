package model;

import android.graphics.Bitmap;

public class Recipe {
	private String creator;
	private String name;
	private String description;
	private String imgUrl;
	private String date;
	private String id;
	private Bitmap img;
	private String ingredients;
	private String steps;
	
	public Recipe(String name_,String id_,String creator_, String desc, String imgUrl_, String date_, String ingredients_, String steps_) {
		creator = creator_;
		name = name_;
		id = id_;
		description = desc;
		imgUrl = imgUrl_;
		date = date_;
		ingredients = ingredients_;
		steps = steps_;
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
    public String getSteps() {
    	return steps;
    }
    
    public void setSteps(String steps_) {
    	steps = steps_;
    }
    public String getIngredients() {
    	return ingredients;
    }
    
    public void setIngredients(String nutrients_) {
    	ingredients = nutrients_;
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
