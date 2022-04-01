package beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.google.common.io.Files;
import controllers.Controller;
import entities.Chart;
import entities.DocumentClassification;
import entities.DocumentFormat;
import entities.DocumentGeneral;
import entities.DocumentKindt;
import entities.DocumentLanguage;
import entities.DocumentPeriod;
import entities.DocumentSubject;
import entities.Pdf;
import solr.IndexFile;
import solr.IndexedField;
import solr.SolrIndex;


@Named("SearchBean")
@ViewScoped
public class Searchbean implements Serializable {

	@EJB
	private Controller myController;
	private List<DocumentGeneral> docList;
	
	private List<DocumentGeneral> docssearch;
	private List<DocumentGeneral> searchlist ;
	private List<DocumentGeneral> result ;

	private List<Pdf> pdflist;
	private List<Pdf> pdfedit;
	
	private List<DocumentKindt> kindlist;
	private List<DocumentFormat> formatlist;
	private List<DocumentClassification> classlist;
	private List<DocumentPeriod> periodlist;
	private List<DocumentLanguage> languageslist;
	private List<DocumentSubject> subjectslist;
	
	private DocumentKindt kind = new DocumentKindt();
	private DocumentFormat format = new DocumentFormat();
	private DocumentClassification classi = new DocumentClassification();
	
	private List<DocumentLanguage> selectlanguage = new ArrayList<>();
	private List<DocumentSubject> selectsubject = new ArrayList<>();
	private List<DocumentPeriod> selectperiod = new ArrayList<>();
	
	private DocumentGeneral selectedDoc;
	private Pdf selectedpdf ;
	private String selectedsearch;

	private StreamedContent streamedContent;
	private DocumentGeneral document;
	
	@PostConstruct	
	public void init() {
		pdflist = new ArrayList();
		pdfedit = new ArrayList();
		
		
		docList = myController.getAllDocs();
		
		kindlist = new ArrayList<>();		
		kindlist = myController.getkindlist();
		
		docssearch = new ArrayList<>();
		
		
		formatlist = new ArrayList<>();
		formatlist = myController.getformatlist();
		
		classlist = new ArrayList<>();
		classlist = myController.getclassificationlist();
		
		languageslist = new ArrayList<>();
		languageslist = myController.getlanguageslist();

		subjectslist = new ArrayList<>();
		subjectslist = myController.getsubjectlist();

		periodlist = new ArrayList<>();
		periodlist = myController.getperiodlist();
	}
	
	
	
	public void selectdoclist() {
		selectedDoc= myController.Find(selectedDoc.getId());
		selectlanguage.addAll(selectedDoc.getDocumentLanguages());
		selectsubject.addAll(selectedDoc.getDocumentSubjects());
		selectperiod.addAll(selectedDoc.getDocumentPeriods());
		
	}

	
	
	public void deletepdf() {		
		SolrIndex mySolr = new SolrIndex();
		
		mySolr.delete(selectedpdf.getId()+"");
		myController.deletePdf(selectedpdf);
		pdflist=myController.getallPdffiler(selectedpdf.getDocumentGeneral().getId());
	}


	public void pdffilter() {
		pdflist=myController.getallPdffiler(selectedDoc.getId());
		selectedDoc=myController.Find(selectedDoc.getId());
	}	

	public void saveDoc() throws IOException {
		selectedDoc.setDocumentLanguages(selectlanguage);
		selectedDoc.setDocumentSubjects(selectsubject);
		selectedDoc.setDocumentPeriods(selectperiod);
		myController.saveDoc(selectedDoc,pdfedit);
		
		searchDoc();
		
		pdfedit.clear();
		selectlanguage.clear();
		selectsubject.clear();
		selectperiod.clear();
	
	}
	
	public void deleteDoc() {		
		myController.deleteDoc(selectedDoc);
		searchDoc();
	}

	
////////////////////////////////////////////////////////////////////////////////
	//UPLOAD - DOWNLOAD
	public void handleFileUpload(FileUploadEvent event) {
		String fileName = System.getProperty("jboss.server.data.dir");
		File dir = new File(fileName + "//pdf");
		UUID uuid = UUID.randomUUID();
		File file = new File(dir, uuid.toString());
		
		try {
			Files.write(event.getFile().getContents(), file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Pdf arxeio = new Pdf();
		arxeio.setName(event.getFile().getFileName());
		arxeio.setType(event.getFile().getContentType());
		arxeio.setUuid(uuid.toString());
		pdfedit.add(arxeio);

		FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void  Provolipdf(Pdf pdf) throws IOException   {
		
		String uuid = pdf.getUuid();		
		String fileName = System.getProperty("jboss.server.data.dir");
		File dir = new File(fileName + "//pdf");
		File file = new File(dir, uuid);
		
		 InputStream targetStream = new FileInputStream(file);
		setStreamedContent(new DefaultStreamedContent(targetStream, "application/pdf",pdf.getName()));	
		
		Chart mychart = new Chart();
		mychart.setDocumentGeneral(selectedDoc);
		myController.createchart(mychart);
		
	}
	
///////////////////////////////////////////////////////////////
	
	
	public  void searchDoc() {
		
		docssearch = new ArrayList<>();
		searchlist = new ArrayList<>();
		result = new ArrayList<>();
		
		List<DocumentGeneral> key = new ArrayList<>();
		
				
		
		Map<String,String> map=new HashMap<String,String>();  
		map.put("content",selectedsearch+"~2"); 
		SolrIndex mySolr = new SolrIndex();
		List<IndexFile> docs  = mySolr.query(map);
		
		List<Integer> ids  = new ArrayList<>();
		int iddoc;
		
		for(IndexFile sdoc : docs) { 
			IndexedField<String> x  = sdoc.getId();
			int y = Integer.parseInt(x.getFieldValue());

			DocumentGeneral docgen = myController.Findiddoc(y);
			iddoc = docgen.getId();
			
			//се пеяиптысг поу сто идио еццяажо упаявоум покка апотекеслата
			if(!ids.contains(iddoc)) {
				if(docgen!=null) docssearch.add(docgen);
				
				ids.add(iddoc);
			}
			
		}	
		
		
		
	    
	    if(docssearch==null) {
	    	result.clear();
	    }
		    if(docssearch!=null) {	    	
	    	for (DocumentGeneral x : docssearch) {	
	    		
	    		//all null
	    		if(kind==null && format==null && classi==null ) {
	    			key=docssearch;
	    		}
	    		
	    		//1 null
	    		 if(kind!=null && format!=null && classi==null) {
	    			 if( ( x.getDocumentKindt().getKindid()==kind.getKindid() ) && (x.getDocumentFormat().getFormatid()==format.getFormatid())   ) {
	    				 key.add(x);
	    			 }	    			
	    		 }
	    			 
	    		 if(kind!=null && format==null && classi!=null) {	
	    			 if( ( x.getDocumentKindt().getKindid()==kind.getKindid() ) && (x.getDocumentClassification().getClassificationid()==classi.getClassificationid())   ) {
	    				 key.add(x);
	    			 }	 
	    			 
	    		 }
	    		 
	    		 if(kind==null && format!=null && classi!=null) {
	    			 if( ( x.getDocumentClassification().getClassificationid()==classi.getClassificationid() ) && (x.getDocumentFormat().getFormatid()==format.getFormatid())   ) {
	    				 key.add(x);
	    			 }	    			
	    		 }
	    		 
	    		 
	    		 //2 null	    		 
	    		 if(kind==null && format==null && classi!=null) {
	    			 if( ( x.getDocumentClassification().getClassificationid()==classi.getClassificationid())   ) {
	    				 key.add(x);
	    			 }	    			
	    		 }	    			
	    		 if(kind==null && format!=null && classi==null) {
	    			 
	    			 if( ( x.getDocumentFormat().getFormatid()==format.getFormatid())   ) {
	    				 key.add(x);
	    			 }	   	
	    			 	    			
	    		 }
	    		 
	    		 if(kind!=null && format==null && classi==null) {
	    			 if( ( x.getDocumentKindt().getKindid()==kind.getKindid())   ) {
	    				 key.add(x);
	    			 }   			
	    		 }	
	    		 
	    		 
	    		 //kanena null
	    		 if(kind!=null && format!=null && classi!=null) {
	    			 if( ( x.getDocumentClassification().getClassificationid()==classi.getClassificationid() )
	    					 && (x.getDocumentFormat().getFormatid()==format.getFormatid()) && x.getDocumentKindt().getKindid()==kind.getKindid()    ) {
	    				 key.add(x);
	    			 }		  
	    		 }
	
	    	}
	    	
	    }
	    result=key;
	    	    
		kind=null;
		format=null;
		classi=null;
		
		
	
		
		
	}

	
//////////////////////////////////////////////////////////////////////////

public List<DocumentGeneral> getDocList() {
return docList;
}

public void setDocList(List<DocumentGeneral> docList) {
this.docList = docList;
}

public DocumentGeneral getSelectedDoc() {
return selectedDoc;
}

public void setSelectedDoc(DocumentGeneral selectedDoc) {
this.selectedDoc = selectedDoc;
}


public List<Pdf> getPdflist() {
	return pdflist;
}

public void setPdflist(List<Pdf> pdflist) {
	this.pdflist = pdflist;
}

public Pdf getSelectedpdf() {
	return selectedpdf;
}

public void setSelectedpdf(Pdf selectedpdf) {
	this.selectedpdf = selectedpdf;
}


public StreamedContent getStreamedContent() {
	return streamedContent;
}


public void setStreamedContent(StreamedContent streamedContent) {
	this.streamedContent = streamedContent;
}


public List<DocumentKindt> getKindlist() {
	return kindlist;
}

public void setKindlist(List<DocumentKindt> kindlist) {
	this.kindlist = kindlist;
}

public List<DocumentFormat> getFormatlist() {
	return formatlist;
}

public void setFormatlist(List<DocumentFormat> formatlist) {
	this.formatlist = formatlist;
}

public List<DocumentClassification> getClasslist() {
	return classlist;
}


public void setClasslist(List<DocumentClassification> classlist) {
	this.classlist = classlist;
}



public List<DocumentPeriod> getPeriodlist() {
	return periodlist;
}


public void setPeriodlist(List<DocumentPeriod> periodlist) {
	this.periodlist = periodlist;
}


public List<DocumentLanguage> getLanguageslist() {
	return languageslist;
}


public void setLanguageslist(List<DocumentLanguage> languageslist) {
	this.languageslist = languageslist;
}


public List<DocumentSubject> getSubjectslist() {
	return subjectslist;
}


public void setSubjectslist(List<DocumentSubject> subjectslist) {
	this.subjectslist = subjectslist;
}

public List<DocumentLanguage> getSelectlanguage() {
	return selectlanguage;
}



public void setSelectlanguage(List<DocumentLanguage> selectlanguage) {
	this.selectlanguage = selectlanguage;
}

public List<DocumentSubject> getSelectsubject() {
	return selectsubject;
}



public void setSelectsubject(List<DocumentSubject> selectsubject) {
	this.selectsubject = selectsubject;
}
public List<DocumentPeriod> getSelectperiod() {
	return selectperiod;
}



public void setSelectperiod(List<DocumentPeriod> selectperiod) {
	this.selectperiod = selectperiod;
}



public String getSelectedsearch() {
	return selectedsearch;
}



public void setSelectedsearch(String selectedsearch) {
	this.selectedsearch = selectedsearch;
}

public List<DocumentGeneral> getDocssearch() {
	return docssearch;
}



public void setDocssearch(List<DocumentGeneral> docssearch) {
	this.docssearch = docssearch;
}



public Controller getMyController() {
	return myController;
}



public void setMyController(Controller myController) {
	this.myController = myController;
}



public List<DocumentGeneral> getSearchlist() {
	return searchlist;
}



public void setSearchlist(List<DocumentGeneral> searchlist) {
	this.searchlist = searchlist;
}



public List<DocumentGeneral> getResult() {
	return result;
}



public void setResult(List<DocumentGeneral> result) {
	this.result = result;
}



public List<Pdf> getPdfedit() {
	return pdfedit;
}



public void setPdfedit(List<Pdf> pdfedit) {
	this.pdfedit = pdfedit;
}



public DocumentKindt getKind() {
	return kind;
}



public void setKind(DocumentKindt kind) {
	this.kind = kind;
}



public DocumentFormat getFormat() {
	return format;
}



public void setFormat(DocumentFormat format) {
	this.format = format;
}



public DocumentClassification getClassi() {
	return classi;
}



public void setClassi(DocumentClassification classi) {
	this.classi = classi;
}



public DocumentGeneral getDocument() {
	return document;
}



public void setDocument(DocumentGeneral document) {
	this.document = document;
}


}