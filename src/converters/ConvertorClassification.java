package converters;



import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import controllers.Controller;
import controllers.EJB;
import entities.DocumentClassification;



@Named
@FacesConverter(value = "Convclass")
public class ConvertorClassification implements Converter {


	
	
private Controller con = EJB.lookup(Controller.class);
	


@Override
public Object getAsObject(FacesContext context, UIComponent component, String value) {
	List<DocumentClassification> list = con.getclassificationlist();
	for (DocumentClassification classi:list) {
		if (value.equals(classi.getClassificationid() + "" ) )
			return classi ;		
	}
	
	
	return null;
}


	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value!=null) {
			return ((DocumentClassification)value).getClassificationid() + "" ;
		}
		return null;
	}
	
	
		
		
		
	

}
