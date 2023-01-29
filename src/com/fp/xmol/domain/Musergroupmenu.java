package com.fp.xmol.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the musergroupmenu database table.
 * 
 */
@Entity
@Table(name="musergroupmenu")
@NamedQuery(name="Musergroupmenu.findAll", query="SELECT m FROM Musergroupmenu m")
public class Musergroupmenu implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer musergroupmenupk;
	private Integer mmenufk;
	private Integer musergroupfk;

	public Musergroupmenu() {
	}


	@Id
	@SequenceGenerator(name="MUSERGROUPMENU_MUSERGROUPMENUPK_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MUSERGROUPMENU_MUSERGROUPMENUPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getMusergroupmenupk() {
		return this.musergroupmenupk;
	}

	public void setMusergroupmenupk(Integer musergroupmenupk) {
		this.musergroupmenupk = musergroupmenupk;
	}


	@Column(nullable=false)
	public Integer getMmenufk() {
		return this.mmenufk;
	}

	public void setMmenufk(Integer mmenufk) {
		this.mmenufk = mmenufk;
	}


	@Column(nullable=false)
	public Integer getMusergroupfk() {
		return this.musergroupfk;
	}

	public void setMusergroupfk(Integer musergroupfk) {
		this.musergroupfk = musergroupfk;
	}

}