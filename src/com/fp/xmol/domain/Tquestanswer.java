package com.fp.xmol.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Type;


/**
 * The persistent class for the tquestanswer database table.
 * 
 */
@Entity
@Table(name="tquestanswer")
@NamedQuery(name="Tquestanswer.findAll", query="SELECT t FROM Tquestanswer t")
public class Tquestanswer implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer tquestanswerpk;
	private byte[] answerimg;
	private String answerno;
	private String answertext;
	private String isright;
	private Date lastupdated;
	private String updatedby;
	private Tquest tquest;

	public Tquestanswer() {
	}


	@Id
	@SequenceGenerator(name="TQUESTANSWER_TQUESTANSWERPK_GENERATOR", sequenceName = "TQUESTANSWER_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TQUESTANSWER_TQUESTANSWERPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getTquestanswerpk() {
		return this.tquestanswerpk;
	}

	public void setTquestanswerpk(Integer tquestanswerpk) {
		this.tquestanswerpk = tquestanswerpk;
	}


	public byte[] getAnswerimg() {
		return this.answerimg;
	}

	public void setAnswerimg(byte[] answerimg) {
		this.answerimg = answerimg;
	}


	public String getAnswerno() {
		return this.answerno;
	}

	public void setAnswerno(String answerno) {
		this.answerno = answerno;
	}


	@Column(length=250)
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getAnswertext() {
		return this.answertext;
	}

	public void setAnswertext(String answertext) {
		this.answertext = answertext;
	}


	@Column(length=1)
	public String getIsright() {
		return this.isright;
	}

	public void setIsright(String isright) {
		this.isright = isright;
	}


	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastupdated() {
		return this.lastupdated;
	}

	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}


	@Column(length=15)
	public String getUpdatedby() {
		return this.updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}


	//bi-directional many-to-one association to Tquest
	@ManyToOne
	@JoinColumn(name="tquestfk", nullable=false)
	public Tquest getTquest() {
		return this.tquest;
	}

	public void setTquest(Tquest tquest) {
		this.tquest = tquest;
	}

}