package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the document_general database table.
 * 
 */
@Entity
@Table(name="document_general")
@NamedQuery(name="DocumentGeneral.findAll", query="SELECT d FROM DocumentGeneral d")
public class DocumentGeneral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Lob
	private String folder;

	@Temporal(TemporalType.TIMESTAMP)
	private Date publicationdate;

	@Lob
	private String publisher;

	@Temporal(TemporalType.TIMESTAMP)
	private Date scandate;

	@Column(length=100)
	private String series;

	@Lob
	private String title;

	//bi-directional many-to-one association to Chart
	@OneToMany(mappedBy="documentGeneral")
	private List<Chart> charts;

	//bi-directional many-to-one association to DocumentFormat
	@ManyToOne
	@JoinColumn(name="FORMATID")
	private DocumentFormat documentFormat;

	//bi-directional many-to-one association to DocumentClassification
	@ManyToOne
	@JoinColumn(name="CLASSIFICATIONID")
	private DocumentClassification documentClassification;

	//bi-directional many-to-one association to DocumentKindt
	@ManyToOne
	@JoinColumn(name="KINDID")
	private DocumentKindt documentKindt;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USERID")
	private User user;

	//bi-directional many-to-many association to DocumentLanguage
	@ManyToMany
	@JoinTable(
		name="document_generallanguagejt"
		, joinColumns={
			@JoinColumn(name="ID", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="LANGUAGEID", nullable=false)
			}
		)
	private List<DocumentLanguage> documentLanguages;

	//bi-directional many-to-many association to DocumentPeriod
	@ManyToMany
	@JoinTable(
		name="document_generalperiodjt"
		, joinColumns={
			@JoinColumn(name="ID", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="PERIODID", nullable=false)
			}
		)
	private List<DocumentPeriod> documentPeriods;

	//bi-directional many-to-many association to DocumentSubject
	@ManyToMany
	@JoinTable(
		name="document_generalsubjectjt"
		, joinColumns={
			@JoinColumn(name="ID", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="SUBJECTID", nullable=false)
			}
		)
	private List<DocumentSubject> documentSubjects;

	//bi-directional many-to-one association to Pdf
	@OneToMany(mappedBy="documentGeneral")
	private List<Pdf> pdfs;

	public DocumentGeneral() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFolder() {
		return this.folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public Date getPublicationdate() {
		return this.publicationdate;
	}

	public void setPublicationdate(Date publicationdate) {
		this.publicationdate = publicationdate;
	}

	public String getPublisher() {
		return this.publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Date getScandate() {
		return this.scandate;
	}

	public void setScandate(Date scandate) {
		this.scandate = scandate;
	}

	public String getSeries() {
		return this.series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Chart> getCharts() {
		return this.charts;
	}

	public void setCharts(List<Chart> charts) {
		this.charts = charts;
	}

	public Chart addChart(Chart chart) {
		getCharts().add(chart);
		chart.setDocumentGeneral(this);

		return chart;
	}

	public Chart removeChart(Chart chart) {
		getCharts().remove(chart);
		chart.setDocumentGeneral(null);

		return chart;
	}

	public DocumentFormat getDocumentFormat() {
		return this.documentFormat;
	}

	public void setDocumentFormat(DocumentFormat documentFormat) {
		this.documentFormat = documentFormat;
	}

	public DocumentClassification getDocumentClassification() {
		return this.documentClassification;
	}

	public void setDocumentClassification(DocumentClassification documentClassification) {
		this.documentClassification = documentClassification;
	}

	public DocumentKindt getDocumentKindt() {
		return this.documentKindt;
	}

	public void setDocumentKindt(DocumentKindt documentKindt) {
		this.documentKindt = documentKindt;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<DocumentLanguage> getDocumentLanguages() {
		return this.documentLanguages;
	}

	public void setDocumentLanguages(List<DocumentLanguage> documentLanguages) {
		this.documentLanguages = documentLanguages;
	}

	public List<DocumentPeriod> getDocumentPeriods() {
		return this.documentPeriods;
	}

	public void setDocumentPeriods(List<DocumentPeriod> documentPeriods) {
		this.documentPeriods = documentPeriods;
	}

	public List<DocumentSubject> getDocumentSubjects() {
		return this.documentSubjects;
	}

	public void setDocumentSubjects(List<DocumentSubject> documentSubjects) {
		this.documentSubjects = documentSubjects;
	}

	public List<Pdf> getPdfs() {
		return this.pdfs;
	}

	public void setPdfs(List<Pdf> pdfs) {
		this.pdfs = pdfs;
	}

	public Pdf addPdf(Pdf pdf) {
		getPdfs().add(pdf);
		pdf.setDocumentGeneral(this);

		return pdf;
	}

	public Pdf removePdf(Pdf pdf) {
		getPdfs().remove(pdf);
		pdf.setDocumentGeneral(null);

		return pdf;
	}

}