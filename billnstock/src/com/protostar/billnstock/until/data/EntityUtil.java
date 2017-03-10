package com.protostar.billnstock.until.data;

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
}
