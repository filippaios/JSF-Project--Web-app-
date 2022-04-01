package converters;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

import beans.Insertdoc;
import controllers.Controller;
import controllers.EJB;
import entities.DocumentLanguage;
import entities.DocumentSubject;



@Named
@FacesConverter(value = "Convsub")
public class ConvertorSubject implements Converter {

	private Controller con = EJB.lookup(Controller.class);
	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		List<DocumentSubject> list = con.getsubjectlist();
		for (DocumentSubject subject:list) {
			if (value.equals(subject.getSubjectid() + "" ) )
				return subject ;		
		}
		
		
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value!=null) {
			return ((DocumentSubject)value).getSubjectid() + "" ;
		}
		return null;
	}

		
		
		
	

}
