package beans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import com.google.common.io.Files;
import controllers.Controller;
import entities.DocumentClassification;
import entities.DocumentFormat;
import entities.DocumentGeneral;
import entities.DocumentKindt;
import entities.DocumentLanguage;
import entities.DocumentPeriod;
import entities.DocumentSubject;
import entities.Pdf;

@Named("Insertdoc")
@ViewScoped
public class Insertdoc implements Serializable {

	@EJB
	private Controller myController;
///////////////////////////////////////////////////////////////
	// check-boxes
	private List<DocumentLanguage> languageslist;
	private List<DocumentSubject> subjectslist;
	private List<DocumentPeriod> periodlist;
	private List<DocumentClassification> classlist;
	private List<DocumentFormat> formatlist;
	private List<DocumentKindt> kindlist;

///////////////////////////////////////////////////////////////////////////////////////

//insert-doc
	private DocumentGeneral document;

	private String title;
	private String ekdoths;
	private Date hmnia_ekdoshs;
	private String seira;
	private String fakelos;
//hmnia_scan thn ekastote pou ginetai kataxvrhsh

	private DocumentKindt kind = new DocumentKindt();
	private DocumentFormat format = new DocumentFormat();
	private DocumentClassification classi = new DocumentClassification();

	private List<DocumentPeriod> period = new ArrayList<>();
	private List<DocumentSubject> subject = new ArrayList<>();
	private List<DocumentLanguage> language = new ArrayList<>();

	private List<Pdf> pdflist;

////////////////////////////////////////////////////////////////////////
	public void greatDoc() throws IOException {
		Date date = new Date();
		document = new DocumentGeneral();

		document.setTitle(title);
		document.setPublisher(ekdoths);
		document.setPublicationdate(hmnia_ekdoshs);
		document.setScandate(date);
		document.setSeries(seira);
		document.setFolder(fakelos);

		document.setDocumentKindt(kind);
		document.setDocumentFormat(format);
		document.setDocumentClassification(classi);

		document.setDocumentLanguages(language);
		document.setDocumentSubjects(subject);
		document.setDocumentPeriods(period);

		myController.newDoc(document, pdflist);

		title = "";
		ekdoths = "";
		hmnia_ekdoshs = null;
		seira = "";
		fakelos = "";
		kind = null;
		format = null;
		classi = null;
		language = null;
		subject = null;
		period = null;

		pdflist.clear();

	}

/////////////////////////////////////////////////////////////////////////

	@PostConstruct
	public void init() {
		languageslist = new ArrayList<>();
		languageslist = myController.getlanguageslist();

		subjectslist = new ArrayList<>();
		subjectslist = myController.getsubjectlist();

		periodlist = new ArrayList<>();
		periodlist = myController.getperiodlist();

		classlist = new ArrayList<>();
		classlist = myController.getclassificationlist();

		formatlist = new ArrayList<>();
		formatlist = myController.getformatlist();

		kindlist = new ArrayList<>();
		kindlist = myController.getkindlist();

		pdflist = new ArrayList();

	}

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
		pdflist.add(arxeio);

		FacesMessage msg = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

////////////////////////////////////////////////////////////////////
	// Getter-Setter

	public Controller getMyController() {
		return myController;
	}

	public void setMyController(Controller myController) {
		this.myController = myController;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEkdoths() {
		return ekdoths;
	}

	public void setEkdoths(String ekdoths) {
		this.ekdoths = ekdoths;
	}

	public Date getHmnia_ekdoshs() {
		return hmnia_ekdoshs;
	}

	public void setHmnia_ekdoshs(Date hmnia_ekdoshs) {
		this.hmnia_ekdoshs = hmnia_ekdoshs;
	}

	public String getSeira() {
		return seira;
	}

	public void setSeira(String seira) {
		this.seira = seira;
	}

	public String getFakelos() {
		return fakelos;
	}

	public void setFakelos(String fakelos) {
		this.fakelos = fakelos;
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

	public List<DocumentPeriod> getPeriod() {
		return period;
	}

	public void setPeriod(List<DocumentPeriod> period) {
		this.period = period;
	}

	public List<DocumentSubject> getSubject() {
		return subject;
	}

	public void setSubject(List<DocumentSubject> subject) {
		this.subject = subject;
	}

	public List<DocumentLanguage> getLanguage() {
		return language;
	}

	public void setLanguage(List<DocumentLanguage> language) {
		this.language = language;
	}

	public List<DocumentSubject> getsubjectslist() {
		return subjectslist;
	}

	public void setsubjectslist(List<DocumentSubject> subjectslist) {
		this.subjectslist = subjectslist;
	}

	public List<DocumentLanguage> getlanguageslist() {
		return languageslist;
	}

	public void setlanguageslist(List<DocumentLanguage> languageslist) {
		this.languageslist = languageslist;
	}

	public DocumentGeneral getDocument() {
		return document;
	}

	public void setDocument(DocumentGeneral document) {
		this.document = document;
	}

	public List<DocumentPeriod> getPeriodlist() {
		return periodlist;
	}

	public void setPeriodlist(List<DocumentPeriod> periodlist) {
		this.periodlist = periodlist;
	}

	public List<DocumentClassification> getClasslist() {
		return classlist;
	}

	public void setClasslist(List<DocumentClassification> classlist) {
		this.classlist = classlist;
	}

	public List<DocumentFormat> getFormatlist() {
		return formatlist;
	}

	public void setFormatlist(List<DocumentFormat> formatlist) {
		this.formatlist = formatlist;
	}

	public List<DocumentKindt> getKindlist() {
		return kindlist;
	}

	public void setKindlist(List<DocumentKindt> kindlist) {
		this.kindlist = kindlist;
	}

}
