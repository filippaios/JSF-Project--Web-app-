package entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the document_format database table.
 * 
 */
@Entity
@Table(name="document_format")
@NamedQuery(name="DocumentFormat.findAll", query="SELECT d FROM DocumentFormat d")
public class DocumentFormat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int formatid;

	@Column(length=50)
	private String formatdescription;

	public DocumentFormat() {
	}

	public int getFormatid() {
		return this.formatid;
	}

	public void setFormatid(int formatid) {
		this.formatid = formatid;
	}

	public String getFormatdescription() {
		return this.formatdescription;
	}

	public void setFormatdescription(String formatdescription) {
		this.formatdescription = formatdescription;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentFormat other = (DocumentFormat) obj;
		if (formatid != other.formatid)
			return false;
		return true;
	}

}