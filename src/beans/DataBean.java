package beans;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import controllers.Controller;
import entities.User;

@Named ("dataBean")
@ViewScoped
public class DataBean implements Serializable {

	
	@EJB 
	private Controller myController;
	private List<User> userList;
	private User selectedUser;
	
	///////////////////////////////////////////////
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	
	/////////////////////////////////////////////////
	
	
	
	
	@PostConstruct
	public void init() {
		
		userList=myController.getAllUsers();
	}
	
	public void saveUser() {
		myController.saveUser(selectedUser);
		userList=myController.getAllUsers();
	}
	public void newUser() {
		selectedUser= new User();
	}	
	public void createUser() {
		myController.createUser(selectedUser);
		userList=myController.getAllUsers();
	}	
	public void deleteUser() {
		myController.deleteUser(selectedUser);	
		userList=myController.getAllUsers();
	}

}