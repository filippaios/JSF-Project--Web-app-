package charts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;

import org.primefaces.model.chart.CartesianChartModel;


import controllers.Controller;


@Named
@RequestScoped
public class Chartsearch implements Serializable {
	
	@EJB
	private Controller myController;
	 
    
    private CartesianChartModel combinedModel;
    private HashMap<String,Integer> List;
    
    
	

 
    @PostConstruct
    public void init() {
    	List=myController.getsearchchart();
        createCombinedModel();
        
    }
 
    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());
 
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
 
   
 
   

	private void createCombinedModel() {
        combinedModel = new BarChartModel();
 
        BarChartSeries kind = new BarChartSeries();
        kind.setLabel("Αναζήτηση Εγγράφων");
 
        for (Map.Entry me : List.entrySet()) {        	
        	 kind.set(me.getKey(), (Number) me.getValue() );
        	 
        }   
        
        
        combinedModel.addSeries(kind);   
        combinedModel.setTitle("Αναζήτηση Εγγράφων");
        combinedModel.setLegendPosition("ne");
        combinedModel.setShowPointLabels(true);
        //combinedModel.setDatatipFormat("#{}");
        Axis xAxis = combinedModel.getAxis(AxisType.X);
        xAxis.setTickAngle(-15);
        
        Axis yAxis = combinedModel.getAxis(AxisType.Y);
       
        yAxis.setMin(0);
        yAxis.setMax(30);
    }
 
    
	 public CartesianChartModel getCombinedModel() {
			return combinedModel;
		}

		public void setCombinedModel(CartesianChartModel combinedModel) {
			this.combinedModel = combinedModel;
		}
    
}