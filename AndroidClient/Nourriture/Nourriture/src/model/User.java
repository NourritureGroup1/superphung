package model;

public class User {
	private String email;
	private String name;
	private String password;
	private String authId;
	private String role;
	private boolean connected = false;
	private String type;
	
	public User(String name_, String role_,
			String email_, String password_) {
		name = name_;
		email = email_;
		role = role_;
		password = password_;
		type = "regular";
	}
	
	public User(String name_, String role_,
			String email_, String authId_, String type_) {
		name = name_;
		email = email_;
		role = role_;
		authId = authId_;
		type = type_;
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
}
