package com.protostar.prostudy.gf.entity;

import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.protostar.prostudy.entity.Address;

@Entity
public class PartnerSchoolEntity {

	@Id 
	@Index     
	private Long id;  
/*	@Index  
	private ExamDetail examDetail;*/
	@Index  
	private ContactDetail contactDetail;
	
	/*private BookSummary bookSummary;	
	private List<PaymentDetail> paymentDetail;*/
	
	@Index  
	private Address address;
	@Index  
	private Long instituteID ;
	
	private String schoolName;
	private String instName;
	private String formNumber;
	private String category;
	private String primaryContact;
	@Index
	private String autoGenerated;
	private String govRegisterno;
	
	  
	private List<ExamDetail> examDetailList;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getInstituteID() {
		return instituteID;
	}
	public void setInstituteID(Long instituteID) {
		this.instituteID = instituteID;
	}
	
	
	public String getFormNumber() {
		return formNumber;
	}
	public void setFormNumber(String formNumber) {
		this.formNumber = formNumber;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPrimaryContact() {
		return primaryContact;
	}
	public void setPrimaryContact(String primaryContact) {
		this.primaryContact = primaryContact;
	}
	
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	/*public ExamDetail getExamDetail() {
		return examDetail;
	}
	public void setExamDetail(ExamDetail examDetail) {
		this.examDetail = examDetail;
	}*/
	public ContactDetail getContactDetail() {
		return contactDetail;
	}
	public void setContactDetail(ContactDetail contactDetail) {
		this.contactDetail = contactDetail;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	/*public BookSummary getBookSummary() {
		return bookSummary;
	}
	public void setBookSummary(BookSummary bookSummary) {
		this.bookSummary = bookSummary;
	}*/
	public String getAutoGenerated() {
		return autoGenerated;
	}
	public void setAutoGenerated(String autoGenerated) {
		this.autoGenerated = autoGenerated;
	}
	public String getGovRegisterno() {
		return govRegisterno;
	}
	public void setGovRegisterno(String govRegisterno) {
		this.govRegisterno = govRegisterno;
	}
	/*public List<PaymentDetail> getPaymentDetail() {
		return paymentDetail;
	}
	public void setPaymentDetail(List<PaymentDetail> paymentDetail) {
		this.paymentDetail = paymentDetail;
	}*/
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}

	public List<ExamDetail> getExamDetailList() {
		return examDetailList;
	}

	public void setExamDetailList(List<ExamDetail> examDetailList) {
		this.examDetailList = examDetailList;
	}


}// end of PartnerSchoolEntity