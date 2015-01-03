package model;

public abstract class AuthentificationFacebook extends Authentification {
	
	public AuthentificationFacebook() {
		type = "facebook";
		isConnected = false;
		user = null;
	}
	
	public String getType() {
		return type;
	}
	public  boolean isConnected() {
		return isConnected;
	}
	public  User getUser() {
		return user;
	}
	
	public  void proceedAuthentication() {
		
	}
}
