package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the document_subject database table.
 * 
 */
@Entity
@Table(name="document_subject")
@NamedQuery(name="DocumentSubject.findAll", query="SELECT d FROM DocumentSubject d")
public class DocumentSubject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int subjectid;

	@Column(length=120)
	private String subjectdescription;

	//bi-directional many-to-many association to DocumentGeneral
	@ManyToMany(mappedBy="documentSubjects")
	private List<DocumentGeneral> documentGenerals;

	public DocumentSubject() {
	}

	public int getSubjectid() {
		return this.subjectid;
	}

	public void setSubjectid(int subjectid) {
		this.subjectid = subjectid;
	}

	public String getSubjectdescription() {
		return this.subjectdescription;
	}

	public void setSubjectdescription(String subjectdescription) {
		this.subjectdescription = subjectdescription;
	}

	public List<DocumentGeneral> getDocumentGenerals() {
		return this.documentGenerals;
	}

	public void setDocumentGenerals(List<DocumentGeneral> documentGenerals) {
		this.documentGenerals = documentGenerals;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentSubject other = (DocumentSubject) obj;
		if (subjectid != other.subjectid)
			return false;
		return true;
	}

}