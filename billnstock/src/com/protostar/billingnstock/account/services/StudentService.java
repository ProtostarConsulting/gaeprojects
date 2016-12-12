package com.protostar.billingnstock.account.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.cmd.Loader;
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.account.entities.Student;

@Api(name = "studentService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.account.services", ownerName = "com.protostar.billingnstock.account.services", packagePath = ""))
public class StudentService {

	@ApiMethod(name = "addStudentInfo")
	public void addStudentInfo(Student stud) {
		 ofy().save().entity(stud).now();
		 
	}

	@ApiMethod(name = "getStudentList")
	public List<Student> getStudentList() {

		 List<Student> currentList = ofy().load().type(Student.class).list();
		
		return currentList;
		
	}
	
		
	
			
		@ApiMethod(name = "getStudentListByType",path="getStudentListByType")
		public List<Student> getStudentListByType(@Named("studentGroupType") String studColg) {
			return ofy().load().type(Student.class).filter("studCol",studColg).list();
		}
		
		@ApiMethod(name = "deleteStudentGrp")
		 public List<Student> deleteStudentGrp(@Named("id") Long id ) {

			  ofy().delete().type(Student.class).id(id).now();
			   List<Student> newList = ofy().load().type(Student.class).list();
			  
			  	return newList;

			 
			 }

}
