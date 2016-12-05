package com.protostar.billingnstock.stock.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.stock.entities.StockItemEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "stockService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.stock.services", ownerName = "com.protostar.billingnstock.stock.services", packagePath = ""))
public class StockItemService {

	private final Logger logger = Logger.getLogger(StockItemService.class
			.getName());

	@ApiMethod(name = "addStock")
	public void addStock(StockItemEntity stockItemEntity) {

		if (stockItemEntity.getId() == null) {
			stockItemEntity.setCreatedDate(new Date());
			// stockItemEntity.setModifiedDate(new Date());
		} else {
			stockItemEntity.setModifiedDate(new Date());
		}
		ofy().save().entity(stockItemEntity).now();
	}

	@ApiMethod(name = "getStockById")
	public StockItemEntity getStockById(@Named("id") Long id) {
		logger.info("getStockById#id:" + id);
		StockItemEntity stock = ofy().load().type(StockItemEntity.class).id(id)
				.now();
		logger.info("getStockById#stock:" + stock);
		return stock;
	}

	@ApiMethod(name = "getAllStock")
	public List<StockItemEntity> getAllStock(@Named("id") Long busId) {
		System.out.println("getAllStock#busId:" + busId);
		List<StockItemEntity> filteredStocks = ofy().load()
				.type(StockItemEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return filteredStocks;
	}

	@ApiMethod(name = "getReportByThreshold", path = "getReportByThreshold")
	public List<StockItemEntity> getReportByThreshold(@Named("id") Long id) {
		List<StockItemEntity> stocks = ofy().load().type(StockItemEntity.class)
				.ancestor(Key.create(BusinessEntity.class, id)).list();
		List<StockItemEntity> filteredThresholdStocks = new ArrayList<StockItemEntity>();

		for (int i = 0; i < stocks.size(); i++) {
			if (stocks.get(i).getQty() <= stocks.get(i).getThresholdValue()) {
				filteredThresholdStocks.add(stocks.get(i));
			}
		}
		logger.info("filteredThresholdStocks#size:" + filteredThresholdStocks.size());
		return filteredThresholdStocks;
	}

	@ApiMethod(name = "updateStock")
	public void updateStock(StockItemEntity stockItemEntity) {
		ofy().save().entity(stockItemEntity).now();
	}

}// end of StockServices
