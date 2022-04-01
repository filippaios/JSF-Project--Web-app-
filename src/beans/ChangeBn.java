package beans;

import java.io.Serializable;
import java.security.SecureRandom;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import controllers.Controller;
import entities.User;


@Named("changeBn")

@ViewScoped
public class ChangeBn implements Serializable {
	@Inject
	private LoginBean log;
	
	@EJB
	private Controller mycontroller;

	private String username;

	private String password;
	private String password1;
	private String password2;
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	public String Changepassmail() {
		String retur = "";
		if(mycontroller.checkUsername(username)==true) {
			User usertemp3 = mycontroller.findUser(username);
			//RANDOM PASS
			StringBuilder sb = new StringBuilder(8);
			   for( int i = 0; i < 8; i++ )  sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
			
			String passwordmail = sb.toString();
			
			usertemp3.setPassword(passwordmail);
			mycontroller.savePwd(usertemp3, passwordmail);
			
			String body = "Ο νέος κωδικός είναι : "+passwordmail;
			//////////////////////////////////////////////////////////////////////////////////////////
			mail email = new mail();
			email.send(usertemp3.getEmail(), "Αλλαγή Κωδικου πρόσβασης", body);
			//////////////////////////////////////////////////////////////////////////////////////////
			retur = "/main.xhtml";
		}else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Προσοχη!", "Μη έγγυρος χρήστης.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return retur;

	}
	

	public String ChangeMe() {
		String ret = "";
		if (password1.equals(password2)) {
			User usertemp = log.getUser();
			if (mycontroller.checkUser(usertemp.getUsername(), password) == true) {
				usertemp.setPassword(password1);
				mycontroller.savePwd(usertemp, password1);
				log.logout();
				ret = "/main.xhtml";
			} else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Προσοχη,μη έγκυρος κωδικός!",
						"Προσπαθήστε Ξανά");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Προσοχη!", "Προσπαθήστε Ξανά");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return ret;
	}

	public LoginBean getLog() {
		return log;
	}

	public void setLog(LoginBean log) {
		this.log = log;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}