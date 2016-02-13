package com.protostar.prostudy.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.prostudy.entity.AttendanceEntity;
import com.protostar.prostudy.entity.StudentEntity;

@Api(name = "attendanceService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.prostudy.service", ownerName = "com.protostar.prostudy.service", packagePath = ""))
public class AttendanceService {

	@ApiMethod(name = "addAttendance")
	public void addAttendance(AttendanceEntity attendance) {
		Key<AttendanceEntity> now = ofy().save().entity(attendance).now();
	}
	
	@ApiMethod(name = "getAttendance")
	public List<AttendanceEntity> getAttendance() {
		return ofy().load().type(AttendanceEntity.class).list();
	}
	
	@ApiMethod(name = "getAttendanceByInstitute")
	 public List<AttendanceEntity> getAttendanceByInstitute(@Named("instituteID") Long instituteID) {
		System.out.println("inside getAttendanceByInstitute");
	  List<AttendanceEntity> attendanceList = ofy().load().type(AttendanceEntity.class).filter("instituteID", instituteID).list();
	  return attendanceList;
	  
	 }
	
	/*@ApiMethod(name = "getByClass")
	 public List<AttendanceEntity> getByClass(@Named("standard") String standard) {
		System.out.println("inside getByClass");
	  List<AttendanceEntity> attendanceList = ofy().load().type(AttendanceEntity.class).filter("standard", standard).list();
	  return attendanceList;
	  
	 }*/
	

}