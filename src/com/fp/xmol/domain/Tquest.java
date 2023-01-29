package com.fp.xmol.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the tquest database table.
 * 
 */
@Entity
@Table(name="tquest")
@NamedQuery(name="Tquest.findAll", query="SELECT t FROM Tquest t")
public class Tquest implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer tquestpk;
	private Date lastupdated;
	private byte[] questionimg;
	private String questiontext;
	private String rightanswer;
	private String updatedby;
	private Tbanksoal tbanksoal;
	private List<Tquestanswer> tquestanswers;

	public Tquest() {
	}


	@Id
	@SequenceGenerator(name="TQUEST_TQUESTPK_GENERATOR", sequenceName = "TQUEST_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TQUEST_TQUESTPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getTquestpk() {
		return this.tquestpk;
	}

	public void setTquestpk(Integer tquestpk) {
		this.tquestpk = tquestpk;
	}


	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastupdated() {
		return this.lastupdated;
	}

	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}


	public byte[] getQuestionimg() {
		return this.questionimg;
	}

	public void setQuestionimg(byte[] questionimg) {
		this.questionimg = questionimg;
	}


	@Column(length=2147483647)
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getQuestiontext() {
		return this.questiontext;
	}

	public void setQuestiontext(String questiontext) {
		this.questiontext = questiontext;
	}


	public String getRightanswer() {
		return rightanswer;
	}


	public void setRightanswer(String rightanswer) {
		this.rightanswer = rightanswer;
	}


	@Column(length=15)
	public String getUpdatedby() {
		return this.updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
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


	//bi-directional many-to-one association to Tquestanswer
	@OneToMany(mappedBy="tquest")
	public List<Tquestanswer> getTquestanswers() {
		return this.tquestanswers;
	}

	public void setTquestanswers(List<Tquestanswer> tquestanswers) {
		this.tquestanswers = tquestanswers;
	}

	public Tquestanswer addTquestanswer(Tquestanswer tquestanswer) {
		getTquestanswers().add(tquestanswer);
		tquestanswer.setTquest(this);

		return tquestanswer;
	}

	public Tquestanswer removeTquestanswer(Tquestanswer tquestanswer) {
		getTquestanswers().remove(tquestanswer);
		tquestanswer.setTquest(null);

		return tquestanswer;
	}

}