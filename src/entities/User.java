package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int idusers;

	@Column(name="diavatmisi_user", length=1)
	private String diavatmisiUser;

	@Column(length=45)
	private String email;

	@Column(length=45)
	private String lname;

	@Column(length=45)
	private String name;

	@Column(length=100)
	private String password;

	@Column(length=45)
	private String username;

	//bi-directional many-to-one association to DocumentGeneral
	@OneToMany(mappedBy="user")
	private List<DocumentGeneral> documentGenerals;

	public User() {
	}

	public int getIdusers() {
		return this.idusers;
	}

	public void setIdusers(int idusers) {
		this.idusers = idusers;
	}

	public String getDiavatmisiUser() {
		return this.diavatmisiUser;
	}

	public void setDiavatmisiUser(String diavatmisiUser) {
		this.diavatmisiUser = diavatmisiUser;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLname() {
		return this.lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<DocumentGeneral> getDocumentGenerals() {
		return this.documentGenerals;
	}

	public void setDocumentGenerals(List<DocumentGeneral> documentGenerals) {
		this.documentGenerals = documentGenerals;
	}

	public DocumentGeneral addDocumentGeneral(DocumentGeneral documentGeneral) {
		getDocumentGenerals().add(documentGeneral);
		documentGeneral.setUser(this);

		return documentGeneral;
	}

	public DocumentGeneral removeDocumentGeneral(DocumentGeneral documentGeneral) {
		getDocumentGenerals().remove(documentGeneral);
		documentGeneral.setUser(null);

		return documentGeneral;
	}
	
	public String diav(String x) {
		if(x.equals("0")) {
			return"Αναμένεται Έγγριση";
		}else if(x.equals("2")) {
			return"Απλός Χρήστης";
		}else{
			return"Διαχειριστής";
		}
	}

}