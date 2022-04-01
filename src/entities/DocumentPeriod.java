package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the document_period database table.
 * 
 */
@Entity
@Table(name="document_period")
@NamedQuery(name="DocumentPeriod.findAll", query="SELECT d FROM DocumentPeriod d")
public class DocumentPeriod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int periodid;

	@Column(length=70)
	private String perioddescription;

	//bi-directional many-to-many association to DocumentGeneral
	@ManyToMany(mappedBy="documentPeriods")
	private List<DocumentGeneral> documentGenerals;

	public DocumentPeriod() {
	}

	public int getPeriodid() {
		return this.periodid;
	}

	public void setPeriodid(int periodid) {
		this.periodid = periodid;
	}

	public String getPerioddescription() {
		return this.perioddescription;
	}

	public void setPerioddescription(String perioddescription) {
		this.perioddescription = perioddescription;
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
		DocumentPeriod other = (DocumentPeriod) obj;
		if (periodid != other.periodid)
			return false;
		return true;
	}

}