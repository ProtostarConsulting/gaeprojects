package com.protostar.billnstock.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityPagingInfo;

@SuppressWarnings("rawtypes")
public class BaseService {

	protected Object getEntityByItemNumber(Class c, int itemNumber) {
		Object e = null;
		@SuppressWarnings("unchecked")
		List list = ofy().load().type(c).filter("itemNumber", itemNumber).list();
		e = list.size() > 0 ? list.get(0) : null;
		return e;
	}

	@SuppressWarnings("unchecked")
	protected EntityPagingInfo fetchEntityListByPaging(Long businessId, Class c, EntityPagingInfo pagingInfo) {
		Query ancestorQuery = ofy().load().type(c).ancestor(Key.create(BusinessEntity.class, businessId))
				.order("-itemNumber");

		getPaggingInfo(pagingInfo, ancestorQuery);
		return pagingInfo;
	}

	@SuppressWarnings("unchecked")
	protected EntityPagingInfo fetchEntityListByPaging(Long businessId, Class c, EntityPagingInfo pagingInfo,
			DocumentStatus status) {
		Query ancestorQuery = ofy().load().type(c).ancestor(Key.create(BusinessEntity.class, businessId))
				.filter("status", status).order("-itemNumber");

		getPaggingInfo(pagingInfo, ancestorQuery);
		return pagingInfo;
	}

	private EntityPagingInfo getPaggingInfo(EntityPagingInfo pagingInfo, Query ancestorQuery) {
		int totalCount = ancestorQuery.count();

		if (pagingInfo.getWebSafeCursorString() != null)
			ancestorQuery = ancestorQuery.startAt(Cursor.fromWebSafeString(pagingInfo.getWebSafeCursorString()));

		QueryResultIterator iterator = ancestorQuery.limit(pagingInfo.getLimit()).iterator();
		List entityList = new ArrayList();
		while (iterator.hasNext()) {
			entityList.add(iterator.next());
		}

		Cursor cursor = iterator.getCursor();
		pagingInfo.setEntityList(entityList);
		pagingInfo.setWebSafeCursorString(cursor.toWebSafeString());
		pagingInfo.setTotalEntities(totalCount);
		return pagingInfo;
	}

}
