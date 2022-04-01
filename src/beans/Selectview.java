package beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class Selectview {

	private boolean value1;
	
//--------------------------------------------------
	public  void change() {
		value1 = !value1;
    }
    public String showpass() {
    	if(value1==true) {
    		return "password";
    	}else{
    		return "text";
    	}
    }
//--------------------------------------------------
  	public boolean isValue1() {
  		return value1;
  	}



  	public void setValue1(boolean value1) {
  		this.value1 = value1;
  	}
}
