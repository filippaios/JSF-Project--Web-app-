package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the document_kindt database table.
 * 
 */
@Entity
@Table(name="document_kindt")
@NamedQuery(name="DocumentKindt.findAll", query="SELECT d FROM DocumentKindt d")
public class DocumentKindt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int kindid;

	@Column(length=90)
	private String kinddescription;

	//bi-directional many-to-one association to DocumentGeneral
	@OneToMany(mappedBy="documentKindt")
	private List<DocumentGeneral> documentGenerals;

	public DocumentKindt() {
	}

	public int getKindid() {
		return this.kindid;
	}

	public void setKindid(int kindid) {
		this.kindid = kindid;
	}

	public String getKinddescription() {
		return this.kinddescription;
	}

	public void setKinddescription(String kinddescription) {
		this.kinddescription = kinddescription;
	}

	public List<DocumentGeneral> getDocumentGenerals() {
		return this.documentGenerals;
	}

	public void setDocumentGenerals(List<DocumentGeneral> documentGenerals) {
		this.documentGenerals = documentGenerals;
	}

	public DocumentGeneral addDocumentGeneral(DocumentGeneral documentGeneral) {
		getDocumentGenerals().add(documentGeneral);
		documentGeneral.setDocumentKindt(this);

		return documentGeneral;
	}

	public DocumentGeneral removeDocumentGeneral(DocumentGeneral documentGeneral) {
		getDocumentGenerals().remove(documentGeneral);
		documentGeneral.setDocumentKindt(null);

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
		DocumentKindt other = (DocumentKindt) obj;
		if (kindid != other.kindid)
			return false;
		return true;
	}
}