package com.fp.xmol.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the texamquest database table.
 * 
 */
@Entity
@Table(name="texamquest")
@NamedQuery(name="Texamquest.findAll", query="SELECT t FROM Texamquest t")
public class Texamquest implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer texamquestpk;
	private String hisanswer;
	private String isright;
	private byte[] questionimg;
	private String questiontext;	
	private String rightanswer;	
	private Texam texam;
	private List<Texamanswer> texamanswers;

	public Texamquest() {
	}


	@Id
	@SequenceGenerator(name="TEXAMQUEST_TEXAMQUESTPK_GENERATOR", sequenceName = "TEXAMQUEST_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TEXAMQUEST_TEXAMQUESTPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getTexamquestpk() {
		return this.texamquestpk;
	}

	public void setTexamquestpk(Integer texamquestpk) {
		this.texamquestpk = texamquestpk;
	}	


	public byte[] getQuestionimg() {
		return questionimg;
	}


	public void setQuestionimg(byte[] questionimg) {
		this.questionimg = questionimg;
	}


	public String getQuestiontext() {
		return questiontext;
	}


	public void setQuestiontext(String questiontext) {
		this.questiontext = questiontext;
	}


	@Column(length=1)
	public String getIsright() {
		return this.isright;
	}

	public void setIsright(String isright) {
		this.isright = isright;
	}
	
	
	public String getRightanswer() {
		return rightanswer;
	}


	public void setRightanswer(String rightanswer) {
		this.rightanswer = rightanswer;
	}


	public String getHisanswer() {
		return hisanswer;
	}


	public void setHisanswer(String hisanswer) {
		this.hisanswer = hisanswer;
	}
	
	
	@ManyToOne
	@JoinColumn(name="texamfk", nullable=false)
	public Texam getTexam() {
		return texam;
	}


	public void setTexam(Texam texam) {
		this.texam = texam;
	}


	//bi-directional many-to-one association to Texamanswer
	@OneToMany(mappedBy="texamquest")
	public List<Texamanswer> getTexamanswers() {
		return this.texamanswers;
	}

	public void setTexamanswers(List<Texamanswer> texamanswers) {
		this.texamanswers = texamanswers;
	}

	public Texamanswer addTexamanswer(Texamanswer texamanswer) {
		getTexamanswers().add(texamanswer);
		texamanswer.setTexamquest(this);

		return texamanswer;
	}

	public Texamanswer removeTexamanswer(Texamanswer texamanswer) {
		getTexamanswers().remove(texamanswer);
		texamanswer.setTexamquest(null);

		return texamanswer;
	}

}