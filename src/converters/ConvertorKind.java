package converters;



import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import controllers.Controller;
import controllers.EJB;
import entities.DocumentKindt;


@Named
@FacesConverter(value = "Convkind")
public class ConvertorKind implements Converter {


	
	
private Controller con = EJB.lookup(Controller.class);
	


@Override
public Object getAsObject(FacesContext context, UIComponent component, String value) {
	List<DocumentKindt> list = con.getkindlist();
	for (DocumentKindt kind:list) {
		if (value.equals(kind.getKindid() + "" ) )
			return kind ;		
	}
	
	
	return null;
}


	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value!=null) {
			return ((DocumentKindt)value).getKindid() + "" ;
		}
		return null;
	}
	
	
		
		
		
	

}
