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
import entities.DocumentPeriod;


@Named
@FacesConverter(value = "Convperiod")
public class ConvertorPeriod implements Converter {


	
	
private Controller con = EJB.lookup(Controller.class);
	


@Override
public Object getAsObject(FacesContext context, UIComponent component, String value) {
	List<DocumentPeriod> list = con.getperiodlist();
	for (DocumentPeriod period:list) {
		if (value.equals(period.getPeriodid() + "" ) )
			return period ;		
	}
	
	
	return null;
}


	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value!=null) {
			return ((DocumentPeriod)value).getPeriodid() + "" ;
		}
		return null;
	}
	
	
		
		
		
	

}
