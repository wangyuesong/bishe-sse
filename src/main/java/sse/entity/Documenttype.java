package sse.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the documenttype database table.
 * 
 */
@Entity
@Table(name="DOCUMENT_TYPE")
@NamedQuery(name="Documenttype.findAll", query="SELECT d FROM Documenttype d")
public class Documenttype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="TYPE_NAME", nullable=false, length=45)
	private String typeName;

	//bi-directional many-to-one association to Document
	@OneToMany(mappedBy="documenttype")
	private List<Document> documents;

	public Documenttype() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<Document> getDocuments() {
		return this.documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public Document addDocument(Document document) {
		getDocuments().add(document);
		document.setDocumenttype(this);

		return document;
	}

	public Document removeDocument(Document document) {
		getDocuments().remove(document);
		document.setDocumenttype(null);

		return document;
	}

}