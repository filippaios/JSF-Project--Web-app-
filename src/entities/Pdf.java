package entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the pdf database table.
 * 
 */
@Entity
@Table(name="pdf")
@NamedQuery(name="Pdf.findAll", query="SELECT p FROM Pdf p")
public class Pdf implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=245)
	private String name;

	@Column(length=45)
	private String type;

	@Column(length=145)
	private String uuid;

	//bi-directional many-to-one association to DocumentGeneral
	@ManyToOne
	@JoinColumn(name="id_doc")
	private DocumentGeneral documentGeneral;

	public Pdf() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public DocumentGeneral getDocumentGeneral() {
		return this.documentGeneral;
	}

	public void setDocumentGeneral(DocumentGeneral documentGeneral) {
		this.documentGeneral = documentGeneral;
	}

}