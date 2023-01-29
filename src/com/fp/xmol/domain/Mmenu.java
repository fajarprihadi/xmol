package com.fp.xmol.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the mmenu database table.
 * 
 */
@Entity
@Table(name="mmenu")
@NamedQuery(name="Mmenu.findAll", query="SELECT m FROM Mmenu m")
public class Mmenu implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer mmenupk;
	private String menugroup;
	private String menugroupicon;
	private String menuicon;
	private String menuname;
	private Integer menuorderno;
	private String menuparamname;
	private String menuparamvalue;
	private String menupath;
	private String menusubgroup;
	private String menusubgroupicon;

	public Mmenu() {
	}


	@Id
	@SequenceGenerator(name="MMENU_MMENUPK_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MMENU_MMENUPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getMmenupk() {
		return this.mmenupk;
	}

	public void setMmenupk(Integer mmenupk) {
		this.mmenupk = mmenupk;
	}


	@Column(length=40)
	public String getMenugroup() {
		return this.menugroup;
	}

	public void setMenugroup(String menugroup) {
		this.menugroup = menugroup;
	}


	@Column(length=50)
	public String getMenugroupicon() {
		return this.menugroupicon;
	}

	public void setMenugroupicon(String menugroupicon) {
		this.menugroupicon = menugroupicon;
	}


	@Column(length=50)
	public String getMenuicon() {
		return this.menuicon;
	}

	public void setMenuicon(String menuicon) {
		this.menuicon = menuicon;
	}


	@Column(length=100)
	public String getMenuname() {
		return this.menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}


	public Integer getMenuorderno() {
		return this.menuorderno;
	}

	public void setMenuorderno(Integer menuorderno) {
		this.menuorderno = menuorderno;
	}


	@Column(length=10)
	public String getMenuparamname() {
		return this.menuparamname;
	}

	public void setMenuparamname(String menuparamname) {
		this.menuparamname = menuparamname;
	}


	@Column(length=30)
	public String getMenuparamvalue() {
		return this.menuparamvalue;
	}

	public void setMenuparamvalue(String menuparamvalue) {
		this.menuparamvalue = menuparamvalue;
	}


	@Column(length=100)
	public String getMenupath() {
		return this.menupath;
	}

	public void setMenupath(String menupath) {
		this.menupath = menupath;
	}


	@Column(length=40)
	public String getMenusubgroup() {
		return this.menusubgroup;
	}

	public void setMenusubgroup(String menusubgroup) {
		this.menusubgroup = menusubgroup;
	}


	@Column(length=50)
	public String getMenusubgroupicon() {
		return this.menusubgroupicon;
	}

	public void setMenusubgroupicon(String menusubgroupicon) {
		this.menusubgroupicon = menusubgroupicon;
	}

}