package psswrd.reset;

public class UserBean {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String sendPassword(){
		@SuppressWarnings("unused")
		boolean isUsernameValid;
		if (this.name == null || this.name.equals("") )
		{ isUsernameValid = false; } 
		else { isUsernameValid = true; }  
		@SuppressWarnings("unused")
		boolean validationComplete = true; 
		return "success"; } 


	}


