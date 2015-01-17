package model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private String email;
	private String name;
	private String password;
	private String authId;
	private String id;
	private String role;
	private boolean connected = false;
	private String type;
	private String badFood;
	private String dislikes;
	private String favoriteFood;
	private String followings;
	private String likes;
	private String recipesCreated;
	private String restrictedFood;
	
	public User(String id_,String name_, String role_,
			String email_, String password_) {
		name = name_;
		email = email_;
		role = role_;
		password = password_;
		type = "regular";
		id = id_;
	}
	
	public User(String id_,String name_, String role_,
			String email_, String authId_, String type_) {
		name = name_;
		email = email_;
		role = role_;
		authId = authId_;
		type = type_;
		id = id_;
	}
	
	public void fillDatas(JSONObject datas) {
		try {
			badFood = getArrayToString(datas.get("badFood").toString());
			dislikes = getArrayToString(datas.get("dislikes").toString());
			favoriteFood = getArrayToString(datas.get("favoriteFood").toString());
			followings = getArrayToString(datas.get("followings").toString());
			likes = getArrayToString(datas.get("likes").toString());
			recipesCreated = getArrayToString(datas.get("recipesCreated").toString());
			restrictedFood = getArrayToString(datas.get("restrictedFood").toString());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

	public String getBadFood() {
		return badFood;
	}
	
	public String getDislikes() {
		return dislikes;
	}
	
	public String getFavoriteFood() {
		return favoriteFood;
	}
	
	public String getFollowings() {
		return followings;
	}
	
	public String getLikes() {
		return likes;
	}
	
	public String getRecipesCreated() {
		return recipesCreated;
	}
	
	public String getRestrictedFood() {
		return restrictedFood;
	}
	
	private String getArrayToString(String json) {
		JSONArray jsonArrayDatas = null;
		try {
			jsonArrayDatas = new JSONArray(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String toReturn = "";
		for(int i=0;i<jsonArrayDatas.length();i++)
		{			
			if ((i-1) >= 0)
				toReturn += ",";
			try {
				toReturn += jsonArrayDatas.getString(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return toReturn;
	}

	public String getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setAuthId(String authId_) {
		authId = authId_;
	}
	
	public String getAuthId() {
		return authId;
	}

	public void setRole(String role_)
	{
		role = role_;
	}
	
	public String getRole()
	{
		return role;
	}
	
	public void setEmail(String email_) 
	{
		email = email_;
	}
	
	public String getEmail() 
	{
		return email;
	}
	
	public void setName(String name_)
	{
		name = name_;		
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setPassword(String password_)
	{
		password = password_;
	}
	
	public String getPassword()
	{
		return password;
	}

	public void setConnected(boolean state)
	{
		connected = state;
	}

	public boolean getConnected()
	{
		return connected;
	}
	
	public boolean doILikeIt(String id) {
		if (!favoriteFood.equals("")) {
			System.out.println("voici mes likes>>"+likes);
			String[] tmp = favoriteFood.split(",");
			for (String value : tmp) {
				if (value.equals(id)) {
					System.out.println("je compare =>"+value+" et "+id);
					return true;
				}
			}
		}
		return false;
	}

	public boolean isItRestrictedFood(String id) {
		if (!restrictedFood.equals("")) {
			System.out.println("voici mes likes>>"+restrictedFood);
			String[] tmp = restrictedFood.split(",");
			for (String value : tmp) {
				if (value.equals(id)) {
					System.out.println("je compare =>"+value+" et "+id);
					return true;
				}
			}
		}
		return false;
	}
	
	public void setFavoriteFood(String favFood) {
		favoriteFood = favFood;
	}
	
	public void setRestrictedFood(String favFood) {
		restrictedFood = favFood;
	}

	public boolean areWeFriends(String id_user) {
		System.out.println("voici mes followings>>"+followings);
		if (followings != null) {
			if (!followings.equals("")) {
				if (followings.contains(id_user))
					return true;
			}			
		}
		return false;
	}

	public void setFollowing(String followings_) {
		followings = followings_;
	}
}
