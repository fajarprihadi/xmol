package com.fp.xmol.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.util.List;


/**
 * The persistent class for the mdepartment database table.
 * 
 */
@Entity
@Table(name="mdepartment")
@NamedQuery(name="Mdepartment.findAll", query="SELECT m FROM Mdepartment m")
public class Mdepartment implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer mdepartmentpk;
	private String deptcode;
	private String deptname;
	private String picemail;
	private String pichp;
	private String picname;
	private List<Mkandidat> mkandidats;

	public Mdepartment() {
	}


	@Id
	@SequenceGenerator(name="MDEPARTMENT_MDEPARTMENTPK_GENERATOR", sequenceName = "MDEPARTMENT_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MDEPARTMENT_MDEPARTMENTPK_GENERATOR")
	@Column(unique=true, nullable=false)
	public Integer getMdepartmentpk() {
		return this.mdepartmentpk;
	}

	public void setMdepartmentpk(Integer mdepartmentpk) {
		this.mdepartmentpk = mdepartmentpk;
	}


	@Column(length=10)
	@Type(type = "com.fp.utils.usertype.TrimUpperCaseUserType")
	public String getDeptcode() {
		return this.deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}


	@Column(length=70)
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getDeptname() {
		return this.deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}


	@Column(length=100)
	@Type(type = "com.fp.utils.usertype.TrimLowerCaseUserType")
	public String getPicemail() {
		return this.picemail;
	}

	public void setPicemail(String picemail) {
		this.picemail = picemail;
	}


	@Column(length=20)
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getPichp() {
		return this.pichp;
	}

	public void setPichp(String pichp) {
		this.pichp = pichp;
	}


	@Column(length=40)
	@Type(type = "com.fp.utils.usertype.TrimUserType")
	public String getPicname() {
		return this.picname;
	}

	public void setPicname(String picname) {
		this.picname = picname;
	}


	//bi-directional many-to-one association to Mkandidat
	@OneToMany(mappedBy="mdepartment")
	public List<Mkandidat> getMkandidats() {
		return this.mkandidats;
	}

	public void setMkandidats(List<Mkandidat> mkandidats) {
		this.mkandidats = mkandidats;
	}

	public Mkandidat addMkandidat(Mkandidat mkandidat) {
		getMkandidats().add(mkandidat);
		mkandidat.setMdepartment(this);

		return mkandidat;
	}

	public Mkandidat removeMkandidat(Mkandidat mkandidat) {
		getMkandidats().remove(mkandidat);
		mkandidat.setMdepartment(null);

		return mkandidat;
	}

}