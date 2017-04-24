package com.protostar.billingnstock.production.entities;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Cache
@Entity
public class ProductionShipmentEntity extends StockItemsShipmentEntity {
	@Index
	private Ref<BomEntity> bomEntity;

	@Override
	public void beforeSave() {
		// super.beforeSave();
		// Should not be called else will increase the parent counter, copping
		// needed before save logic here...

		if (getBusiness() == null) {
			throw new RuntimeException("Business entity is not set on: " + this.getClass().getSimpleName()
					+ " This is required field. Aborting save operation...");
		}
		/*
		 * Calendar cal = Calendar.getInstance(); Date today = cal.getTime();
		 * cal.add(Calendar.YEAR, -1); Date dummyDate = cal.getTime();
		 */
		if (getId() == null) {
			setCreatedDate(new Date());
			setModifiedDate(new Date());
		} else {
			setModifiedDate(new Date());
		}

		if (this.getStatus() == DocumentStatus.FINALIZED) {
			this.setStatusAlreadyFinalized(true);
		}

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.PROD_STOCKSHIPMENT_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}

}
