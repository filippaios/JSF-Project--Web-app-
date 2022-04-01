package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the document_language database table.
 * 
 */
@Entity
@Table(name="document_language")
@NamedQuery(name="DocumentLanguage.findAll", query="SELECT d FROM DocumentLanguage d")
public class DocumentLanguage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int languageid;

	@Column(length=50)
	private String languagedescription;

	//bi-directional many-to-many association to DocumentGeneral
	@ManyToMany(mappedBy="documentLanguages")
	private List<DocumentGeneral> documentGenerals;

	public DocumentLanguage() {
	}

	public int getLanguageid() {
		return this.languageid;
	}

	public void setLanguageid(int languageid) {
		this.languageid = languageid;
	}

	public String getLanguagedescription() {
		return this.languagedescription;
	}

	public void setLanguagedescription(String languagedescription) {
		this.languagedescription = languagedescription;
	}

	public List<DocumentGeneral> getDocumentGenerals() {
		return this.documentGenerals;
	}

	public void setDocumentGenerals(List<DocumentGeneral> documentGenerals) {
		this.documentGenerals = documentGenerals;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + languageid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentLanguage other = (DocumentLanguage) obj;
		if (languageid != other.languageid)
			return false;
		return true;
	}

}