package com.fp.xmol.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the mkandidat database table.
 * 
 */
@Entity
@Table(name="mkandidat")
@NamedQuery(name="Mkandidat.findAll", query="SELECT m FROM Mkandidat m")
public class Mkandidat implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer mkandidatpk;
	private String alamat;
	private String email;
	private String kota;
	private String nama;
	private String nohp;
	private Date tgllahir;
	private Mdepartment mdepartment;

	public Mkandidat() {
	}


	@Id
	@SequenceGenerator(name="MKANDIDAT_MKANDIDATPK_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MKANDIDAT_MKANDIDATPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getMkandidatpk() {
		return this.mkandidatpk;
	}

	public void setMkandidatpk(Integer mkandidatpk) {
		this.mkandidatpk = mkandidatpk;
	}


	@Column(length=200)
	public String getAlamat() {
		return this.alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}


	@Column(length=100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	@Column(length=70)
	public String getKota() {
		return this.kota;
	}

	public void setKota(String kota) {
		this.kota = kota;
	}


	@Column(length=40)
	public String getNama() {
		return this.nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}


	@Column(length=20)
	public String getNohp() {
		return this.nohp;
	}

	public void setNohp(String nohp) {
		this.nohp = nohp;
	}


	@Temporal(TemporalType.DATE)
	public Date getTgllahir() {
		return this.tgllahir;
	}

	public void setTgllahir(Date tgllahir) {
		this.tgllahir = tgllahir;
	}


	//bi-directional many-to-one association to Mdepartment
	@ManyToOne
	@JoinColumn(name="mdepartmentfk")
	public Mdepartment getMdepartment() {
		return this.mdepartment;
	}

	public void setMdepartment(Mdepartment mdepartment) {
		this.mdepartment = mdepartment;
	}

}