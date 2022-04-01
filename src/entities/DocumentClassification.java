package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the document_classification database table.
 * 
 */
@Entity
@Table(name="document_classification")
@NamedQuery(name="DocumentClassification.findAll", query="SELECT d FROM DocumentClassification d")
public class DocumentClassification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int classificationid;

	@Column(length=90)
	private String classificationdescription;

	//bi-directional many-to-one association to DocumentGeneral
	@OneToMany(mappedBy="documentClassification")
	private List<DocumentGeneral> documentGenerals;

	public DocumentClassification() {
	}

	public int getClassificationid() {
		return this.classificationid;
	}

	public void setClassificationid(int classificationid) {
		this.classificationid = classificationid;
	}

	public String getClassificationdescription() {
		return this.classificationdescription;
	}

	public void setClassificationdescription(String classificationdescription) {
		this.classificationdescription = classificationdescription;
	}

	public List<DocumentGeneral> getDocumentGenerals() {
		return this.documentGenerals;
	}

	public void setDocumentGenerals(List<DocumentGeneral> documentGenerals) {
		this.documentGenerals = documentGenerals;
	}

	public DocumentGeneral addDocumentGeneral(DocumentGeneral documentGeneral) {
		getDocumentGenerals().add(documentGeneral);
		documentGeneral.setDocumentClassification(this);

		return documentGeneral;
	}

	public DocumentGeneral removeDocumentGeneral(DocumentGeneral documentGeneral) {
		getDocumentGenerals().remove(documentGeneral);
		documentGeneral.setDocumentClassification(null);

		return documentGeneral;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentClassification other = (DocumentClassification) obj;
		if (classificationid != other.classificationid)
			return false;
		return true;
	}

}