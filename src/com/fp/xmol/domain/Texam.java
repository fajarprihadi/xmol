package com.fp.xmol.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.util.Date;


/**
 * The persistent class for the texam database table.
 * 
 */
@Entity
@Table(name="texam")
@NamedQuery(name="Texam.findAll", query="SELECT t FROM Texam t")
public class Texam implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer texampk;
	private String deptcode;	
	private String alamat;
	private String email;
	private String ispass;
	private String kota;
	private String nama;
	private String nohp;	
	private Date tgllahir;	
	private int jumlahsoal;
	private int passingscore;
	private int jumlahbenar;
	private int score;	
	private int durasi;
	private int totaldoc;
	private Date waktuakhirtest;
	private Date waktumulaitest;
	private int lamates;
	private Date tglinterview;
	private Date tglpsikotes;
	private Date tgljoin;	
	private String status;
	private String isdone;
	private String iswait;
	private Tbanksoal tbanksoal;

	public Texam() {
	}


	@Id
	@SequenceGenerator(name="TEXAM_TEXAMPK_GENERATOR", sequenceName = "TEXAM_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TEXAM_TEXAMPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getTexampk() {
		return this.texampk;
	}

	public void setTexampk(Integer texampk) {
		this.texampk = texampk;
	}


	@Column(length=10)
	@Type(type = "com.fp.utils.usertype.TrimUpperCaseUserType")
	public String getDeptcode() {
		return this.deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getAlamat() {
		return alamat;
	}


	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}


	@Type(type = "com.fp.utils.usertype.TrimLowerCaseUserType")
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getIspass() {
		return ispass;
	}


	public void setIspass(String ispass) {
		this.ispass = ispass;
	}


	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getKota() {
		return kota;
	}


	public void setKota(String kota) {
		this.kota = kota;
	}


	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getNama() {
		return nama;
	}


	public void setNama(String nama) {
		this.nama = nama;
	}


	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getNohp() {
		return nohp;
	}


	public void setNohp(String nohp) {
		this.nohp = nohp;
	}


	public Date getTgllahir() {
		return tgllahir;
	}


	public void setTgllahir(Date tgllahir) {
		this.tgllahir = tgllahir;
	}


	public int getJumlahsoal() {
		return this.jumlahsoal;
	}

	public void setJumlahsoal(int jumlahsoal) {
		this.jumlahsoal = jumlahsoal;
	}

	
	public int getJumlahbenar() {
		return jumlahbenar;
	}


	public void setJumlahbenar(int jumlahbenar) {
		this.jumlahbenar = jumlahbenar;
	}


	public int getPassingscore() {
		return this.passingscore;
	}

	public void setPassingscore(int passingscore) {
		this.passingscore = passingscore;
	}


	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}


	public int getDurasi() {
		return this.durasi;
	}

	public void setDurasi(int durasi) {
		this.durasi = durasi;
	}

	
	public int getTotaldoc() {
		return totaldoc;
	}


	public void setTotaldoc(int totaldoc) {
		this.totaldoc = totaldoc;
	}


	@Temporal(TemporalType.TIMESTAMP)
	public Date getWaktuakhirtest() {
		return this.waktuakhirtest;
	}

	public void setWaktuakhirtest(Date waktuakhirtest) {
		this.waktuakhirtest = waktuakhirtest;
	}


	@Temporal(TemporalType.TIMESTAMP)
	public Date getWaktumulaitest() {
		return this.waktumulaitest;
	}

	public void setWaktumulaitest(Date waktumulaitest) {
		this.waktumulaitest = waktumulaitest;
	}
		
	
	public int getLamates() {
		return lamates;
	}


	public void setLamates(int lamates) {
		this.lamates = lamates;
	}


	@Temporal(TemporalType.DATE)
	public Date getTglinterview() {
		return tglinterview;
	}


	public void setTglinterview(Date tglinterview) {
		this.tglinterview = tglinterview;
	}


	@Temporal(TemporalType.DATE)
	public Date getTglpsikotes() {
		return tglpsikotes;
	}


	public void setTglpsikotes(Date tglpsikotes) {
		this.tglpsikotes = tglpsikotes;
	}


	@Temporal(TemporalType.DATE)
	public Date getTgljoin() {
		return tgljoin;
	}


	public void setTgljoin(Date tgljoin) {
		this.tgljoin = tgljoin;
	}


	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getIsdone() {
		return isdone;
	}


	public void setIsdone(String isdone) {
		this.isdone = isdone;
	}

	
	public String getIswait() {
		return iswait;
	}


	public void setIswait(String iswait) {
		this.iswait = iswait;
	}


	//bi-directional many-to-one association to Tbanksoal
	@ManyToOne
	@JoinColumn(name="tbanksoalfk", nullable=false)
	public Tbanksoal getTbanksoal() {
		return this.tbanksoal;
	}

	public void setTbanksoal(Tbanksoal tbanksoal) {
		this.tbanksoal = tbanksoal;
	}

}