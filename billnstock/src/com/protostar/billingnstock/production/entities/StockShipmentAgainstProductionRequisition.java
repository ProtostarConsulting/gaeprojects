package com.protostar.billingnstock.production.entities;

import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;

public class StockShipmentAgainstProductionRequisition extends StockItemsShipmentEntity {

	@Override
	public void beforeSave() {
		// super.beforeSave();

		if (this.getStatus() == DocumentStatus.FINALIZED) {
			this.setStatusAlreadyFinalized(true);
		}

		if (getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(getBusiness()), Constants.PROD_STOCKISSUE_NO_COUNTER);
			setItemNumber(sequenceGenService.getNextSequenceNumber());
		}
	}
}
