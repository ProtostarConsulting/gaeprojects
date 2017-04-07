package com.protostar.billnstock.until.data;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.entity.BaseEntity;

public class EntityUtil {

	public static List<?> updateCreatedModifiedDate(
			@SuppressWarnings("rawtypes") List entityList) {
		if (entityList != null && entityList.size() > 0) {
			Date dateNow = new Date();
			for (Object obj : entityList) {
				BaseEntity entityObj = (BaseEntity) obj;
				if (entityObj.getId() == null) {
					entityObj.setCreatedDate(dateNow);
				} else {
					entityObj.setModifiedDate(dateNow);
				}
			}
		}
		return entityList;
	}

	public static BaseEntity updateCreatedModifiedDate(BaseEntity entityObj) {
		Date dateNow = new Date();
		if (entityObj.getId() == null) {
			entityObj.setCreatedDate(dateNow);
		} else {
			entityObj.setModifiedDate(dateNow);
		}
		return entityObj;
	}

	public static Key<BusinessEntity> getBusinessRawKey(BusinessEntity entityObj) {
		return Key.create(BusinessEntity.class, entityObj.getId());
	}

	public static Date getBeginingOfDay(Date date) {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(date);
		fromCalendar.set(Calendar.MILLISECOND, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		return fromCalendar.getTime();
	}

	public static Date getEndOfDay(Date date) {
		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(date);
		toCalendar.set(Calendar.MILLISECOND, 999);
		toCalendar.set(Calendar.SECOND, 59);
		toCalendar.set(Calendar.MINUTE, 59);
		toCalendar.set(Calendar.HOUR_OF_DAY, 23);
		return toCalendar.getTime();
	}
}
