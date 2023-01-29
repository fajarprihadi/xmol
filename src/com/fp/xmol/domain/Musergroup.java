package com.fp.xmol.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the musergroup database table.
 * 
 */
@Entity
@Table(name="musergroup")
@NamedQuery(name="Musergroup.findAll", query="SELECT m FROM Musergroup m")
public class Musergroup implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer musergrouppk;
	private Timestamp lastupdated;
	private String status;
	private String updatedby;
	private String usergroupcode;
	private String usergroupdesc;
	private String usergroupname;

	public Musergroup() {
	}


	@Id
	@SequenceGenerator(name="MUSERGROUP_MUSERGROUPPK_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MUSERGROUP_MUSERGROUPPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getMusergrouppk() {
		return this.musergrouppk;
	}

	public void setMusergrouppk(Integer musergrouppk) {
		this.musergrouppk = musergrouppk;
	}


	public Timestamp getLastupdated() {
		return this.lastupdated;
	}

	public void setLastupdated(Timestamp lastupdated) {
		this.lastupdated = lastupdated;
	}


	@Column(length=1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Column(length=15)
	public String getUpdatedby() {
		return this.updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}


	@Column(length=20)
	public String getUsergroupcode() {
		return this.usergroupcode;
	}

	public void setUsergroupcode(String usergroupcode) {
		this.usergroupcode = usergroupcode;
	}


	@Column(length=100)
	public String getUsergroupdesc() {
		return this.usergroupdesc;
	}

	public void setUsergroupdesc(String usergroupdesc) {
		this.usergroupdesc = usergroupdesc;
	}


	@Column(length=40)
	public String getUsergroupname() {
		return this.usergroupname;
	}

	public void setUsergroupname(String usergroupname) {
		this.usergroupname = usergroupname;
	}

}