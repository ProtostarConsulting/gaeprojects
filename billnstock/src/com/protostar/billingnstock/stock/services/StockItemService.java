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
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;
import com.protostar.billingnstock.invoice.entities.StockItemsReceiptEntity;
import com.protostar.billingnstock.invoice.entities.StockItemsShipmentEntity;
import com.protostar.billingnstock.invoice.entities.StockLineItem;
import com.protostar.billingnstock.purchase.entities.SupplierEntity;
import com.protostar.billingnstock.stock.entities.StockItemEntity;
import com.protostar.billingnstock.stock.entities.StockItemTxnEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.EntityUtil;
import com.protostar.billnstock.until.data.SequenceGeneratorShardedService;

@Api(name = "stockService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.stock.services", ownerName = "com.protostar.billingnstock.stock.services", packagePath = ""))
public class StockItemService {

	private final Logger logger = Logger.getLogger(StockItemService.class
			.getName());

	@ApiMethod(name = "addStock", path = "addStock")
	public StockItemEntity addStock(StockItemEntity stockItemEntity) {
		if (stockItemEntity.getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(stockItemEntity.getBusiness()),
					Constants.STOCKITEM_NO_COUNTER);
			stockItemEntity.setStockItemNumber(sequenceGenService
					.getNextSequenceNumber());
		}

		ofy().save().entity(stockItemEntity).now();
		return stockItemEntity;
	}

	@ApiMethod(name = "addStockReceipt", path = "addStockReceipt")
	public void addStockReceipt(StockItemsReceiptEntity stockItemsReceipt) {
		if (stockItemsReceipt.getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(stockItemsReceipt
							.getBusiness()), Constants.STOCKRECEIPT_NO_COUNTER);
			stockItemsReceipt.setReceiptNumber(sequenceGenService
					.getNextSequenceNumber());
		}
		List<StockLineItem> productLineItemList = stockItemsReceipt
				.getProductLineItemList();
		for (StockLineItem stockLineItem : productLineItemList) {
			// This needed so that StockItemService.adjustStockItems adds the
			// stock items
			stockLineItem.setStockMaintainedQty(stockLineItem.getQty() * 2);
		}
		StockItemService.adjustStockItems(stockItemsReceipt.getBusiness(),
				productLineItemList);

		ofy().save().entity(stockItemsReceipt).now();
		// update stock items here...
	}

	@ApiMethod(name = "addStockShipment", path = "addStockShipment")
	public void addStockShipment(StockItemsShipmentEntity stockItemsShipment) {
		if (stockItemsShipment.getId() == null) {
			SequenceGeneratorShardedService sequenceGenService = new SequenceGeneratorShardedService(
					EntityUtil.getBusinessRawKey(stockItemsShipment
							.getBusiness()), Constants.STOCKRECEIPT_NO_COUNTER);
			stockItemsShipment.setShipmentNumber(sequenceGenService
					.getNextSequenceNumber());
		}
		StockItemService.adjustStockItems(stockItemsShipment.getBusiness(),
				stockItemsShipment.getProductLineItemList());
		ofy().save().entity(stockItemsShipment).now();
	}

	@ApiMethod(name = "getStockById")
	public StockItemEntity getStockById(@Named("id") Long id) {
		logger.info("getStockById#id:" + id);
		StockItemEntity stock = ofy().load().type(StockItemEntity.class).id(id)
				.now();
		logger.info("getStockById#stock:" + stock);
		return stock;
	}

	@ApiMethod(name = "getAllStock", path = "getAllStock")
	public List<StockItemEntity> getAllStock(@Named("id") Long busId) {
		System.out.println("getAllStock#busId:" + busId);
		List<StockItemEntity> filteredStocks = ofy().load()
				.type(StockItemEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return filteredStocks;
	}

	@ApiMethod(name = "getStockReceiptList", path = "getStockReceiptList")
	public List<StockItemsReceiptEntity> getStockReceiptList(
			@Named("id") Long busId) {
		System.out.println("getAllStock#busId:" + busId);
		List<StockItemsReceiptEntity> stockItemsShipmentList = ofy().load()
				.type(StockItemsReceiptEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return stockItemsShipmentList;
	}

	@ApiMethod(name = "getStockShipmentList", path = "getStockShipmentList")
	public List<StockItemsShipmentEntity> getStockShipmentList(
			@Named("id") Long busId) {
		// System.out.println("getAllStock#busId:" + busId);
		List<StockItemsShipmentEntity> stockItemsShipmentList = ofy().load()
				.type(StockItemsShipmentEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return stockItemsShipmentList;
	}

	@ApiMethod(name = "getReportByThreshold", path = "getReportByThreshold")
	public List<StockItemEntity> getReportByThreshold(
			@Named("id") Long bussinessId) {
		List<StockItemEntity> stocks = ofy().load().type(StockItemEntity.class)
				.ancestor(Key.create(BusinessEntity.class, bussinessId)).list();
		List<StockItemEntity> filteredThresholdStocks = new ArrayList<StockItemEntity>();

		for (int i = 0; i < stocks.size(); i++) {
			if (stocks.get(i).getQty() <= stocks.get(i).getThresholdValue()) {
				filteredThresholdStocks.add(stocks.get(i));
			}
		}
		logger.info("filteredThresholdStocks#size:"
				+ filteredThresholdStocks.size());
		return filteredThresholdStocks;
	}

	@ApiMethod(name = "updateStock")
	public void updateStock(StockItemEntity stockItemEntity) {
		ofy().save().entity(stockItemEntity).now();
	}

	public static void adjustStockItems(BusinessEntity business,
			List<StockLineItem> productLineItemList) {
		/* For Reduce the Stock Quantity */

		List<StockItemTxnEntity> stockItemTxnList = new ArrayList<StockItemTxnEntity>();

		for (StockLineItem invoiceLineItem : productLineItemList) {
			int qtyDiff = invoiceLineItem.getQty()
					- invoiceLineItem.getStockMaintainedQty();
			if (invoiceLineItem.getStockItem().isMaintainStock()
					&& qtyDiff != 0) {

				StockItemTxnEntity txnEntity = new StockItemTxnEntity();
				txnEntity.setBusiness(business);
				txnEntity.setStockItem(invoiceLineItem.getStockItem());
				txnEntity.setQty(Math.abs(qtyDiff));
				if (qtyDiff > 0) {
					txnEntity.setTxnType(StockItemTxnEntity.StockTxnType.DR);
					txnEntity.setPrice(invoiceLineItem.getPrice());
				} else {
					txnEntity.setTxnType(StockItemTxnEntity.StockTxnType.CR);
					txnEntity.setPrice(invoiceLineItem.getCost());
				}
				invoiceLineItem.setStockMaintainedQty(invoiceLineItem.getQty());
				stockItemTxnList.add(txnEntity);
			}

		}
		if (stockItemTxnList.size() > 0) {
			addStockItemTxnList(stockItemTxnList);
		}
	}

	private static void addStockItemTxnList(
			List<StockItemTxnEntity> stockItemTxnList) {
		List<StockItemEntity> stockItemToUpdateList = new ArrayList<StockItemEntity>();
		for (StockItemTxnEntity stockItemTxnEntity : stockItemTxnList) {
			StockItemEntity stockItemEntity = stockItemTxnEntity.getStockItem();
			if (stockItemTxnEntity.getTxnType() == StockItemTxnEntity.StockTxnType.DR) {
				stockItemEntity.setQty(stockItemEntity.getQty()
						- stockItemTxnEntity.getQty());
			} else {
				// This means stock addition/receipt
				// calculate moving avb. cost
				double movingAvgCost = (stockItemEntity.getQty()
						* stockItemEntity.getMovingAvgCost() + stockItemTxnEntity
						.getQty() * stockItemTxnEntity.getPrice())
						/ (stockItemEntity.getQty() + stockItemTxnEntity
								.getQty());
				movingAvgCost = (double) Math.round(movingAvgCost * 100) / 100;
				stockItemEntity.setMovingAvgCost(movingAvgCost);
				stockItemEntity.setQty(stockItemEntity.getQty()
						+ stockItemTxnEntity.getQty());
			}
			stockItemToUpdateList.add(stockItemEntity);
		}

		ofy().save().entities(stockItemTxnList);
		ofy().save().entities(stockItemToUpdateList);
	}

	@ApiMethod(name = "getStockItemTxnList", path = "getStockItemTxnList")
	public List<StockItemTxnEntity> getStockItemTxnList(
			@Named("id") Long busId, @Named("fromDate") Date fromDate,
			@Named("txnType") String txnType) {

		Query<StockItemTxnEntity> filterQuery = ofy().load()
				.type(StockItemTxnEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("createdDate >=", fromDate);

		if (txnType != null && !txnType.trim().isEmpty())
			filterQuery = filterQuery.filter("txnType", txnType);

		return filterQuery.list();
	}

	@ApiMethod(name = "addSupplier", path = "addSupplier")
	public SupplierEntity addSupplier(SupplierEntity supplierEntity) {
		ofy().save().entity(supplierEntity).now();
		return supplierEntity;
	}

	@ApiMethod(name = "getAllSuppliersByBusiness", path = "getAllSuppliersByBusiness")
	public List<SupplierEntity> getAllSuppliersByBusiness(
			@Named("id") Long busId) {

		List<SupplierEntity> filteredSuppliers = ofy()
				.load()
				.type(SupplierEntity.class)
				.filter("business",
						Ref.create(Key.create(BusinessEntity.class, busId)))
				.list();

		return filteredSuppliers;
	}

	@ApiMethod(name = "getSupplierByID", path = "getSupplierByID")
	public SupplierEntity getPOByID(@Named("id") Long id) {
		SupplierEntity supplierByID = ofy().load().type(SupplierEntity.class)
				.id(id).now();

		return supplierByID;
	}

}// end of StockServices
