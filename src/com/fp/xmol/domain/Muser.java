package com.fp.xmol.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.util.Date;


/**
 * The persistent class for the muser database table.
 * 
 */
@Entity
@Table(name="muser")
@NamedQuery(name="Muser.findAll", query="SELECT m FROM Muser m")
public class Muser implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer muserpk;
	private String email;
	private Date lastlogin;
	private Date lastupdated;
	private String password;
	private String updatedby;
	private String userid;
	private String username;

	public Muser() {
	}


	@Id
	@SequenceGenerator(name="MUSER_MUSERPK_GENERATOR", sequenceName = "MUSER_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MUSER_MUSERPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getMuserpk() {
		return this.muserpk;
	}

	public void setMuserpk(Integer muserpk) {
		this.muserpk = muserpk;
	}


	@Column(length=100)
	@Type(type = "com.fp.utils.usertype.TrimLowerCaseUserType")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastlogin() {
		return this.lastlogin;
	}

	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}


	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastupdated() {
		return this.lastupdated;
	}

	public void setLastupdated(Date lastupdated) {
		this.lastupdated = lastupdated;
	}


	@Column(length=70)
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	@Column(length=15)
	public String getUpdatedby() {
		return this.updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}


	@Column(length=15)
	@Type(type = "com.fp.utils.usertype.TrimUpperCaseUserType")
	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}


	@Column(length=40)
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}