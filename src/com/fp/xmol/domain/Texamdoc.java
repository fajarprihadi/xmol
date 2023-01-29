package com.fp.xmol.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.util.Date;


/**
 * The persistent class for the texamdoc database table.
 * 
 */
@Entity
@Table(name="texamdoc")
@NamedQuery(name="Texamdoc.findAll", query="SELECT t FROM Texamdoc t")
public class Texamdoc implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer texamdocpk;
	private String docfilename;
	private String docid;
	private String docname;
	private String uploadedby;
	private Date uploadtime;
	private Texam texam;

	public Texamdoc() {
	}


	@Id
	@SequenceGenerator(name="TEXAMDOC_TEXAMDOCPK_GENERATOR", sequenceName = "TEXAMDOC_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TEXAMDOC_TEXAMDOCPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getTexamdocpk() {
		return this.texamdocpk;
	}

	public void setTexamdocpk(Integer texamdocpk) {
		this.texamdocpk = texamdocpk;
	}


	@Column(length=100)
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getDocfilename() {
		return this.docfilename;
	}

	public void setDocfilename(String docfilename) {
		this.docfilename = docfilename;
	}


	@Column(length=20)
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getDocid() {
		return this.docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}


	@Column(length=20)
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getDocname() {
		return this.docname;
	}

	public void setDocname(String docname) {
		this.docname = docname;
	}


	@Column(length=15)
	public String getUploadedby() {
		return this.uploadedby;
	}

	public void setUploadedby(String uploadedby) {
		this.uploadedby = uploadedby;
	}


	@Temporal(TemporalType.TIMESTAMP)
	public Date getUploadtime() {
		return this.uploadtime;
	}

	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}


	//bi-directional many-to-one association to Texam
	@ManyToOne
	@JoinColumn(name="texamfk", nullable=false)
	public Texam getTexam() {
		return this.texam;
	}

	public void setTexam(Texam texam) {
		this.texam = texam;
	}

}