package com.fp.xmol.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the tbanksoal database table.
 * 
 */
@Entity
@Table(name = "tbanksoal")
@NamedQuery(name = "Tbanksoal.findAll", query = "SELECT t FROM Tbanksoal t")
public class Tbanksoal implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer tbanksoalpk;
	private String deskripsi;
	private Integer durasi;
	private Integer examsoal;
	private Integer jumlahsoal;
	private String kodesoal;
	private String linkid;
	private Date lastupdated;
	private Integer passingscore;
	private String updatedby;
	private Mdepartment mdepartment;
	private List<Texam> texams;
	private List<Tquest> tquests;

	public Tbanksoal() {
	}

	@Id
	@SequenceGenerator(name = "TBANKSOAL_TBANKSOALPK_GENERATOR", sequenceName = "TBANKSOAL_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBANKSOAL_TBANKSOALPK_GENERATOR")
	@Column(unique = true, nullable = false)
	public Integer getTbanksoalpk() {
		return this.tbanksoalpk;
	}

	public void setTbanksoalpk(Integer tbanksoalpk) {
		this.tbanksoalpk = tbanksoalpk;
	}

	@Column(length = 100)
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getDeskripsi() {
		return this.deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public Integer getDurasi() {
		return durasi;
	}

	public void setDurasi(Integer durasi) {
		this.durasi = durasi;
	}

		
	public Integer getExamsoal() {
		return examsoal;
	}

	public void setExamsoal(Integer examsoal) {
		this.examsoal = examsoal;
	}

	public Integer getJumlahsoal() {
		return this.jumlahsoal;
	}

	public void setJumlahsoal(Integer jumlahsoal) {
		this.jumlahsoal = jumlahsoal;
	}

	@Column(length = 10)
	@Type(type = "com.fp.utils.usertype.TrimUpperCaseUserType")
	public String getKodesoal() {
		return this.kodesoal;
	}

	public void setKodesoal(String kodesoal) {
		this.kodesoal = kodesoal;
	}

	@Type(type = "com.fp.utils.usertype.TrimUserType")	
	public String getLinkid() {
		return linkid;
	}

	public void setLinkid(String linkid) {
		this.linkid = linkid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastupdated() {
		return this.lastupdated;
	}

	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}

	public Integer getPassingscore() {
		return this.passingscore;
	}

	public void setPassingscore(Integer passingscore) {
		this.passingscore = passingscore;
	}

	@Column(length = 15)
	public String getUpdatedby() {
		return this.updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}

	// bi-directional many-to-one association to Mdepartment
	@ManyToOne
	@JoinColumn(name = "mdepartmentfk")
	public Mdepartment getMdepartment() {
		return this.mdepartment;
	}

	public void setMdepartment(Mdepartment mdepartment) {
		this.mdepartment = mdepartment;
	}

	// bi-directional many-to-one association to Texam
	@OneToMany(mappedBy = "tbanksoal")
	public List<Texam> getTexams() {
		return this.texams;
	}

	public void setTexams(List<Texam> texams) {
		this.texams = texams;
	}

	public Texam addTexam(Texam texam) {
		getTexams().add(texam);
		texam.setTbanksoal(this);

		return texam;
	}

	public Texam removeTexam(Texam texam) {
		getTexams().remove(texam);
		texam.setTbanksoal(null);

		return texam;
	}

	// bi-directional many-to-one association to Tquest
	@OneToMany(mappedBy = "tbanksoal")
	public List<Tquest> getTquests() {
		return this.tquests;
	}

	public void setTquests(List<Tquest> tquests) {
		this.tquests = tquests;
	}

	public Tquest addTquest(Tquest tquest) {
		getTquests().add(tquest);
		tquest.setTbanksoal(this);

		return tquest;
	}

	public Tquest removeTquest(Tquest tquest) {
		getTquests().remove(tquest);
		tquest.setTbanksoal(null);

		return tquest;
	}

}