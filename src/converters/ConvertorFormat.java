package converters;



import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import controllers.Controller;
import controllers.EJB;
import entities.DocumentFormat;




@Named
@FacesConverter(value = "Convformat")
public class ConvertorFormat implements Converter {


	
	
private Controller con = EJB.lookup(Controller.class);
	


@Override
public Object getAsObject(FacesContext context, UIComponent component, String value) {
	List<DocumentFormat> list = con.getformatlist();
	for (DocumentFormat format:list) {
		if (value.equals(format.getFormatid() + "" ) )
			return format ;		
	}
	
	
	return null;
}


	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value!=null) {
			return ((DocumentFormat)value).getFormatid() + "" ;
		}
		return null;
	}
	
	
		
		
		
	

}
