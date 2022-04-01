package controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import org.apache.commons.codec.binary.Base64;

import beans.Searchbean;
import entities.Chart;
import entities.DocumentClassification;
import entities.DocumentFormat;
import entities.DocumentGeneral;
import entities.DocumentKindt;
import entities.DocumentLanguage;
import entities.DocumentPeriod;
import entities.DocumentSubject;
import entities.User;
import solr.IndexFactory;
import solr.IndexFile;
import solr.IndexedFileLocation;
import solr.SolrIndex;
import entities.Pdf;

@Stateless
public class Controller {

	@Resource(name = "ExamsDevDS")
	private DataSource ds;

	@PersistenceContext(unitName = "DisProject")
	private EntityManager em;
///////////////////////////////////////////////////////////////////////

//LOGIN
	public User findUser(String username) {
		Query q = em.createQuery("SELECT u FROM User u where u.username=:username ");
		q.setParameter("username", username);
		User user = (User) q.getSingleResult();
		return user;
	}

	public boolean checkUser(String username, String password) {
		boolean pass = false;
		Query q = em.createQuery("SELECT u FROM User u where u.username=:username ");
		q.setParameter("username", username);
		User user = (User) q.getSingleResult();
		if (user != null) {
			String salt = user.getPassword().substring(user.getPassword().lastIndexOf(":") + 1);
			String hashedPwd = encrypt(password, salt);
			if (user.getPassword().equals(hashedPwd + ":" + salt)) {
				pass = true;
			}
		}
		return pass;
	}
	
	public boolean checkUsername(String username) {
		boolean pass = true;
		Query q = em.createQuery("SELECT u FROM User u where u.username=:username ");
		q.setParameter("username", username);
		try {
			User user = (User) q.getSingleResult();
		}catch (Exception e){
			pass = false;
		}
		
		return pass;
	}

	public void savePwd(User user, String password) {
		if (!password.isEmpty()) {
			SecureRandom random = new SecureRandom();
			byte bytes[] = new byte[8];
			random.nextBytes(bytes);
			String salt = Base64.encodeBase64String(bytes);
			user.setPassword(encrypt(password, salt) + ":" + salt);
		}
		em.merge(user);
	}

	private String encrypt(String password, String salt) {

		password = password.concat(salt);
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		return Base64.encodeBase64String(hash);
	}

////////////////////////////////////////////////////////////////////////////////
	// BUTTONS GREATE
	public void createUser(User selectedUser) {
		try {
			if (selectedUser != null) {
				em.persist(selectedUser);
				em.flush();
				savePwd(selectedUser, selectedUser.getPassword());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void createchart(Chart selectedchart) {
		try {
			if (selectedchart != null) {
				em.persist(selectedchart);
				em.flush();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

////////////////////////////////////////////////////////////////////////////
	// BUTTON SAVE
	public void saveUser(User selectedUser) {
		try {
			if (selectedUser != null) {
				em.merge(selectedUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveDoc(DocumentGeneral selecteddoc, List<Pdf> pdflist) throws IOException {
		try {
			if (selecteddoc != null) {
				em.merge(selecteddoc);
				List<IndexFile> solrindexlist = new ArrayList<>();
				for (int i = 0; i < pdflist.size(); i++) {
					Pdf arxeio = pdflist.get(i);
					arxeio.setDocumentGeneral(selecteddoc);
					em.persist(arxeio);

					// ΕΔΩ ΘΑ ΓΙΝΕΙ ΤΟ INDEXING TOU SOLR

					String uuid = arxeio.getUuid();

					String fileName = System.getProperty("jboss.server.data.dir");
					File dir = new File(fileName + "//pdf");
					File file = new File(dir, uuid);
					InputStream targetStream = new FileInputStream(file);

					IndexFile solrfile = new IndexFile();
					solrfile.setFilename(arxeio.getName());
					solrfile.setId(arxeio.getId() + "");

					solrfile.setFileStream(targetStream);
					solrfile.setFileSize(targetStream.available());
					solrfile.setIllustratedFilename(fileName);

					solrindexlist.add(solrfile);
				}
				//////////////////////////////
				IndexFactory.getInstance().indexFiles(solrindexlist);
				/////////////////////////////

				// SolrIndex solrindexing = new SolrIndex();
				// solrindexing.indexFiles(solrindexlist);

				em.flush();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//////////////////////////////////////////////////////////////////////

	// BUTTON DELETE

	public void deletePdf(Pdf selectedpdf) {
		try {
			if (selectedpdf != null) {
				Query query9 = em.createQuery("DELETE FROM Pdf  WHERE id=" + selectedpdf.getId());
				query9.executeUpdate();

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Επιτυχής Διαγραφή Εγγράφου"));
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteUser(User selectedUser) {
		try {
			if (selectedUser != null) {
				if (selectedUser.getDiavatmisiUser().equals("1")) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Προσοχη!", "Δεν Δύναται να γίνει Διαγραφή Διαχειριστή "));
				} else {
					Query query3 = em.createQuery("DELETE FROM User  WHERE idusers=" + selectedUser.getIdusers());
					query3.executeUpdate();
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Επιτυχής Διαγραφή"));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteDoc(DocumentGeneral selectedDoc) {
		List<Pdf> pdflist = getallPdflist();
		List<Pdf> pdfdelete = new ArrayList<>();
		//selectedDoc=Find(selectedDoc.getId());
		for (Pdf x : pdflist) {
			if (selectedDoc.getId() == x.getDocumentGeneral().getId()) {
				SolrIndex mySolr = new SolrIndex();
				
				mySolr.delete(x.getId()+"");
				
				pdfdelete.add(x);
				Query query5 = em.createQuery("DELETE FROM Pdf  WHERE id_doc=" + x.getDocumentGeneral().getId());
				query5.executeUpdate();
				
			}
		}
		try {
			if (selectedDoc != null) {
				Query query4 = em.createQuery("DELETE FROM DocumentGeneral  WHERE id=" + selectedDoc.getId());
				query4.executeUpdate();
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Επιτυχής Διαγραφή Εγγράφου"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//////////////////////////////////////////////////////////////////

	// LISTES
	public List<User> getAllUsers() {
		List<User> userList = new ArrayList<User>();
		try {
			Query q = em.createQuery("Select u FROM User u ");
			q.setMaxResults(1000);
			userList = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

//////////////////////// GIA CONVERTER
	public List<DocumentLanguage> getlanguageslist() {
		List<DocumentLanguage> Languagelist = new ArrayList<DocumentLanguage>();
		try {
			Query q = em.createQuery("Select d FROM DocumentLanguage d ");
			q.setMaxResults(1000);
			Languagelist = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Languagelist;
	}

	public List<DocumentSubject> getsubjectlist() {
		List<DocumentSubject> DocumentSubject = new ArrayList<DocumentSubject>();
		try {
			Query q = em.createQuery("Select s FROM DocumentSubject s ");
			q.setMaxResults(1000);
			DocumentSubject = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DocumentSubject;
	}

	public List<DocumentPeriod> getperiodlist() {
		List<DocumentPeriod> DocumentPeriod = new ArrayList<DocumentPeriod>();
		try {
			Query q = em.createQuery("Select d FROM DocumentPeriod d ");
			q.setMaxResults(1000);
			DocumentPeriod = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DocumentPeriod;
	}

	public List<DocumentClassification> getclassificationlist() {
		List<DocumentClassification> DocumentClassification = new ArrayList<DocumentClassification>();
		try {
			Query q = em.createQuery("Select d FROM DocumentClassification d ");
			q.setMaxResults(1000);
			DocumentClassification = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DocumentClassification;
	}

	public List<DocumentFormat> getformatlist() {
		List<DocumentFormat> DocumentFormat = new ArrayList<DocumentFormat>();
		try {
			Query q = em.createQuery("Select d FROM DocumentFormat d ");
			q.setMaxResults(1000);
			DocumentFormat = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DocumentFormat;
	}

	public List<DocumentKindt> getkindlist() {
		List<DocumentKindt> DocumentKindt = new ArrayList<DocumentKindt>();
		try {
			Query q = em.createQuery("Select d FROM DocumentKindt d ");
			q.setMaxResults(1000);
			DocumentKindt = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DocumentKindt;
	}

	public List<Pdf> getallPdflist() {
		List<Pdf> Pdf = new ArrayList<Pdf>();
		try {
			Query q = em.createQuery("Select p FROM Pdf p ");
			q.setMaxResults(1000);
			Pdf = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Pdf;
	}

	public List<Pdf> getallPdffiler(int id) {
		List<Pdf> Pdf = new ArrayList<Pdf>();
		try {
			Query q = em.createQuery("Select p FROM Pdf p where p.documentGeneral.id=:id ");
			q.setParameter("id", id);
			Pdf = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Pdf;
	}
	
	public DocumentGeneral getsearchdoc(int id) {
		DocumentGeneral tempdoc = new DocumentGeneral();
		try {
			Query q = em.createQuery("Select d FROM DocumentGeneral d  where d.id=:id");
			q.setParameter("id", id);
			tempdoc = (DocumentGeneral) q.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempdoc;
	}

	public List<DocumentGeneral> getAllDocs() {
		List<DocumentGeneral> docList = new ArrayList<DocumentGeneral>();
		try {
			Query q = em.createQuery("Select d FROM DocumentGeneral d ");
			q.setMaxResults(1000);
			docList = q.getResultList();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return docList;
	}
	
	
	
	
////////////////////////////////////////////////////////////////////////

	// NEW DOCUMENT-INSERT
	public void newDoc(DocumentGeneral document, List<Pdf> pdflist) throws IOException {
		em.persist(document);
		List<IndexFile> solrindexlist = new ArrayList<>();
		for (int i = 0; i < pdflist.size(); i++) {
			Pdf arxeio = pdflist.get(i);
			arxeio.setDocumentGeneral(document);
			em.persist(arxeio);

			// ΕΔΩ ΘΑ ΓΙΝΕΙ ΤΟ INDEXING TOU SOLR

			String uuid = arxeio.getUuid();

			String fileName = System.getProperty("jboss.server.data.dir");
			File dir = new File(fileName + "//pdf");
			File file = new File(dir, uuid);
			InputStream targetStream = new FileInputStream(file);

			IndexFile solrfile = new IndexFile();
			solrfile.setFilename(arxeio.getName());
			solrfile.setId(arxeio.getId() + "");

			solrfile.setFileStream(targetStream);
			solrfile.setFileSize(targetStream.available());
			solrfile.setIllustratedFilename(fileName);

			solrindexlist.add(solrfile);

		}
		//////////////////////////////
		IndexFactory.getInstance().indexFiles(solrindexlist);
		/////////////////////////////

		// SolrIndex solrindexing = new SolrIndex();
		// solrindexing.indexFiles(solrindexlist);

		em.flush();

		FacesContext context = FacesContext.getCurrentInstance();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Επιτυχής Καταχώρηση", ""));
	}
	
	

	/////////////////////////////////////////////////

	public DocumentGeneral Find(int id) {
		DocumentGeneral doc = em.find(DocumentGeneral.class, id);
		doc.getDocumentLanguages().size();
		doc.getDocumentPeriods().size();
		doc.getDocumentSubjects().size();
		return doc;
	}
	
	public DocumentGeneral Findiddoc(int id) {
		Pdf doc = em.find(Pdf.class, id);
		DocumentGeneral new_doc = doc.getDocumentGeneral();
		return new_doc;
	}

////////////////////////////////////////////////////////////////////////////
//CHARTS

	public HashMap getfiler() {
		HashMap doc = new LinkedHashMap<String, Integer>();
		try {
			Query q = em.createQuery(
					"Select p.documentKindt.kinddescription,count(p.documentKindt) FROM DocumentGeneral p Group By p.documentKindt Order by count(p.documentKindt) desc");
			q.setMaxResults(10);
			List<Object[]> list = q.getResultList();
			for (Object[] result : list) {
				doc.put(result[0].toString(), Integer.parseInt(result[1].toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public HashMap getfilerClass() {
		HashMap doc = new LinkedHashMap<String, Integer>();
		try {
			Query q = em.createQuery(
					"Select p.documentClassification.classificationdescription,count(p.documentClassification) FROM DocumentGeneral p Group By p.documentClassification Order by count(p.documentClassification) desc ");
			List<Object[]> list = q.getResultList();
			for (Object[] result : list) {
				doc.put(result[0].toString(), Integer.parseInt(result[1].toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public HashMap getfilerformat() {
		HashMap doc = new LinkedHashMap<String, Integer>();
		try {
			Query q = em.createQuery(
					"Select p.documentFormat.formatdescription,count(p.documentFormat) FROM DocumentGeneral p Group By p.documentFormat Order by count(p.documentFormat) desc ");
			List<Object[]> list = q.getResultList();
			for (Object[] result : list) {
				doc.put(result[0].toString(), Integer.parseInt(result[1].toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	
	public HashMap getsearchchart() {
		HashMap doc = new LinkedHashMap<String, Integer>();
		try {
			Query q = em.createQuery("SELECT SUBSTRING(d.title,25,55) , count(c.documentGeneral) FROM DocumentGeneral d ,Chart c where d.id = c.documentGeneral Group by SUBSTRING(d.title,25,55) Order by count(c.documentGeneral) desc  ");
			
			List<Object[]> list = q.getResultList();
			for (Object[] result : list) {
				doc.put(result[0].toString(), Integer.parseInt(result[1].toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
}
