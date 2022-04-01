package entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the chart database table.
 * 
 */
@Entity
@Table(name="chart")
@NamedQuery(name="Chart.findAll", query="SELECT c FROM Chart c")
public class Chart implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	//bi-directional many-to-one association to DocumentGeneral
	@ManyToOne
	@JoinColumn(name="id_doc")
	private DocumentGeneral documentGeneral;

	public Chart() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DocumentGeneral getDocumentGeneral() {
		return this.documentGeneral;
	}

	public void setDocumentGeneral(DocumentGeneral documentGeneral) {
		this.documentGeneral = documentGeneral;
	}

}