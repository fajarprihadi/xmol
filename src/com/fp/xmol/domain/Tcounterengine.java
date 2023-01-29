package com.fp.xmol.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tcounterengine database table.
 * 
 */
@Entity
@Table(name="tcounterengine")
@NamedQuery(name="Tcounterengine.findAll", query="SELECT t FROM Tcounterengine t")
public class Tcounterengine implements Serializable {
	private static final long serialVersionUID = 1L;
	private String countername;
	private Integer lastcounter;

	public Tcounterengine() {
	}


	@Column(length=30)
	@Id
	public String getCountername() {
		return this.countername;
	}

	public void setCountername(String countername) {
		this.countername = countername;
	}


	public Integer getLastcounter() {
		return this.lastcounter;
	}

	public void setLastcounter(Integer lastcounter) {
		this.lastcounter = lastcounter;
	}

}