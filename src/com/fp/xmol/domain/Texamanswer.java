package com.fp.xmol.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Type;


/**
 * The persistent class for the texamanswer database table.
 * 
 */
@Entity
@Table(name="texamanswer")
@NamedQuery(name="Texamanswer.findAll", query="SELECT t FROM Texamanswer t")
public class Texamanswer implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer texamanswerpk;
	private byte[] answerimg;
	private String answerno;
	private String answertext;
	private String isright;
	private Texamquest texamquest;

	public Texamanswer() {
	}


	@Id
	@SequenceGenerator(name="TEXAMANSWER_TEXAMANSWERPK_GENERATOR", sequenceName = "TEXAMANSWER_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TEXAMANSWER_TEXAMANSWERPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getTexamanswerpk() {
		return this.texamanswerpk;
	}

	public void setTexamanswerpk(Integer texamanswerpk) {
		this.texamanswerpk = texamanswerpk;
	}


	public String getAnswerno() {
		return this.answerno;
	}

	public void setAnswerno(String answerno) {
		this.answerno = answerno;
	}	
	
	public byte[] getAnswerimg() {
		return answerimg;
	}


	public void setAnswerimg(byte[] answerimg) {
		this.answerimg = answerimg;
	}


	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getAnswertext() {
		return answertext;
	}


	public void setAnswertext(String answertext) {
		this.answertext = answertext;
	}


	public String getIsright() {
		return isright;
	}


	public void setIsright(String isright) {
		this.isright = isright;
	}

	//bi-directional many-to-one association to Texamquest
	@ManyToOne
	@JoinColumn(name="texamquestfk", nullable=false)
	public Texamquest getTexamquest() {
		return this.texamquest;
	}

	public void setTexamquest(Texamquest texamquest) {
		this.texamquest = texamquest;
	}

}