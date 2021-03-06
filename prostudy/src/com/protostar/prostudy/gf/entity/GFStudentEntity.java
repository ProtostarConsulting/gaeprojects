package com.protostar.prostudy.gf.entity;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class GFStudentEntity {

	@Id
	private Long id;
	private String fName;
	private String mName;
	private String lName;
	private String standard;
	private String mediumOfAnswer;
	private String gender;
	@Index
	private long instituteID;
	private String prn;
	private String role;
	
	Ref<PartnerSchoolEntity> schoolName;
	
	
	public long getInstituteID() {
		return instituteID;
	}
	public void setInstituteID(long instituteID) {
		this.instituteID = instituteID;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPrn() {
		return prn;
	}
	public void setPrn(String prn) {
		this.prn = prn;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}	
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getMediumOfAnswer() {
		return mediumOfAnswer;
	}
	public void setMediumOfAnswer(String mediumOfAnswer) {
		this.mediumOfAnswer = mediumOfAnswer;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public PartnerSchoolEntity getSchoolName() {
		return schoolName.get();
	}
	public void setSchoolName(PartnerSchoolEntity schoolName) {
		this.schoolName = Ref.create(schoolName);
	}

	
	
}
