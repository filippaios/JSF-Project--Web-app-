package converters;



import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import controllers.Controller;
import controllers.EJB;
import entities.DocumentLanguage;


@Named
@FacesConverter(value = "Conv")
public class ConvertorLanguage implements Converter {


	
	
private Controller con = EJB.lookup(Controller.class);
	


@Override
public Object getAsObject(FacesContext context, UIComponent component, String value) {
	List<DocumentLanguage> list = con.getlanguageslist();
	for (DocumentLanguage languages:list) {
		if (value.equals(languages.getLanguageid() + "" ) )
			return languages ;		
	}
	
	
	return null;
}


	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value!=null) {
			return ((DocumentLanguage)value).getLanguageid() + "" ;
		}
		return null;
	}
	
	
		
		
		
	

}
