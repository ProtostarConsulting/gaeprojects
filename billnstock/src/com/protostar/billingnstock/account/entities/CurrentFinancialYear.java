package com.protostar.billingnstock.account.entities;

import java.util.Calendar;
import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.entity.BaseEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;

@Cache
@Entity
public class CurrentFinancialYear extends BaseEntity {

	private Date fromDate;
	private Date toDate;

	public CurrentFinancialYear() {

	}

	@Override
	public void beforeSave() {
		super.beforeSave();

		if (this.getFromDate() == null || this.getToDate() == null) {
			throw new RuntimeException(
					"From or to Date fields can not be null for: "
							+ this.getClass().getSimpleName()
							+ " This is required field. Aborting save operation...");
		}

		this.setFromDate(EntityUtil.getBeginingOfDay(this.getFromDate()));
		this.setToDate(EntityUtil.getEndOfDay(this.getToDate()));
	}

	public static Key<CurrentFinancialYear> getKey(long bizId) {
		return Key
				.create(Key.create(BusinessEntity.class, bizId),
						CurrentFinancialYear.class,
						Constants.CURRENT_FINANCIAL_YEAR_ID);
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
