package beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import controllers.Controller;
import entities.User;



@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {

	@EJB
	private Controller myController;

	private String username;
	private String password;
	private boolean loggedIn = false;
	private boolean value1;
	//--------------------------------------------
	private User user;
		
	public String login() {
		String result = "";
		try {
			user = myController.findUser(username);
			boolean pass = myController.checkUser(username, password);
			if (pass) {
				if(user.getDiavatmisiUser().equals("0")) {
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Αναμονή επιβεβαίωσης", ""));
				}else if(user.getDiavatmisiUser().equals("1")){
					loggedIn = true;
					result = "Admin/main.xhtml?facesRedirect=true";
				}else {
					loggedIn = true;
					result = "User/main.xhtml?facesRedirect=true";
				}
				
			} else {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Λάθος Κωδικός Πρόσβασης", ""));
			}
		}catch (Exception e){
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Λάθος Όνομα Χρήστη", ""));
		}

		return result;
	}
	public String logout() {
		((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(loggedIn)).invalidate();
			loggedIn = false;
			username= ""; password = "";
		    return "/main.xhtml";
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	//---------------------------------------------------------

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isValue1() {
		return value1;
	}

	public void setValue1(boolean value1) {
		this.value1 = value1;
	}
	//--------------------------------------------------
		public  void change() {
			value1 = !value1;
	    }
	    public String showpass() {
	    	if(value1==true) {
	    		return "text";
	    	}else{
	    		return "password";
	    	}
	    }
	//--------------------------------------------------
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
	

}
