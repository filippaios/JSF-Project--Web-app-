package beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import controllers.Controller;
import entities.User;


@Named("registerBean")
@ViewScoped
public class RegisterBean implements Serializable {

	@EJB
	private Controller myController;
	private String username;
	private String password;
	private String passwordconfirm;
	private String name;
	private String email;
	private String diavatmisi;

	private String lname;
	//--------------------
	private boolean terms;
	//--------------------

	public String RegBean() {
		if(password.equals(passwordconfirm)) {
			User user = new User();
			user.setName(name);
			user.setLname(lname);
			user.setEmail(email);
			user.setUsername(username);
			user.setDiavatmisiUser("0");
			if(myController.checkUsername(username)==true) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Το όνομα χρήστη υπάρχει ηδη!", ""));
				password =" ";
				passwordconfirm = " ";
				return "";
			}
			
			myController.savePwd(user, password);
			return "Login.xhtml?facesRedirect=true";
		}else{
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Οι κωδικοι δεν ταιριάζουν", ""));
			return "";
		}
		

	}
	
	public String checkbox() {
		if(terms==true) {
			return "";
		}else{
			return "Must check";
		}
		

	}
	
	
//GETTERS-SETTERS	
//----------------------------------------------------------------------------------------
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getPasswordconfirm() {
		return passwordconfirm;
	}
	public void setPasswordconfirm(String passwordconfirm) {
		this.passwordconfirm = passwordconfirm;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
//----------------------------------------------------------------------------------------


	public boolean isTerms() {
		return terms;
	}


	public void setTerms(boolean terms) {
		this.terms = terms;
	}
//----------------------------------------------------------------------------------------
	public String getDiavatmisi() {
		return diavatmisi;
	}
	public void setDiavatmisi(String diavatmisi) {
		this.diavatmisi = diavatmisi;
	}
}
