package com.fp.xmol.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.util.Date;


/**
 * The persistent class for the texammemo database table.
 * 
 */
@Entity
@Table(name="texammemo")
@NamedQuery(name="Texammemo.findAll", query="SELECT t FROM Texammemo t")
public class Texammemo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer texammemopk;
	private String memo;
	private String memoby;
	private Date memotime;
	private String memounit;
	private Texam texam;

	public Texammemo() {
	}


	@Id
	@SequenceGenerator(name="TEXAMMEMO_TEXAMMEMOPK_GENERATOR", sequenceName = "TEXAM_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TEXAMMEMO_TEXAMMEMOPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getTexammemopk() {
		return this.texammemopk;
	}

	public void setTexammemopk(Integer texammemopk) {
		this.texammemopk = texammemopk;
	}


	@Column(length=250)
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}


	@Column(length=40)
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getMemoby() {
		return this.memoby;
	}

	public void setMemoby(String memoby) {
		this.memoby = memoby;
	}


	@Temporal(TemporalType.TIMESTAMP)
	public Date getMemotime() {
		return this.memotime;
	}

	public void setMemotime(Date memotime) {
		this.memotime = memotime;
	}


	@Column(length=10)
	@Type(type = "com.fp.utils.usertype.TrimUpperCaseUserType")
	public String getMemounit() {
		return this.memounit;
	}

	public void setMemounit(String memounit) {
		this.memounit = memounit;
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