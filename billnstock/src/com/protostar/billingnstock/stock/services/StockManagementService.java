package com.protostar.billingnstock.stock.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.purchase.entities.BudgetEntity;
import com.protostar.billingnstock.purchase.entities.LineItemCategory;
import com.protostar.billingnstock.purchase.entities.LineItemEntity;
import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
import com.protostar.billingnstock.purchase.entities.RequisitionEntity;
import com.protostar.billingnstock.purchase.entities.SupplierEntity;
import com.protostar.billingnstock.stock.entities.StockItemEntity;
import com.protostar.billingnstock.stock.entities.StockItemInstanceEntity;
import com.protostar.billingnstock.stock.entities.StockItemTxnEntity;
import com.protostar.billingnstock.stock.entities.StockItemTypeEntity;
import com.protostar.billingnstock.stock.entities.StockItemsReceiptEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity.ShipmentType;
import com.protostar.billingnstock.stock.entities.StockLineItem;
import com.protostar.billingnstock.stock.entities.StockSettingsEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;
import com.protostar.billnstock.service.BaseService;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EntityPagingInfo;

@Api(name = "stockService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.stock.services", ownerName = "com.protostar.billingnstock.stock.services", packagePath = ""))
public class StockManagementService extends BaseService {

	private final Logger logger = Logger.getLogger(StockManagementService.class.getName());

	@ApiMethod(name = "addStockItemType", path = "addStockItemType")
	public StockItemTypeEntity addStockItemType(StockItemTypeEntity stockItemTypeEntity) {
		ofy().save().entity(stockItemTypeEntity).now();
		return stockItemTypeEntity;
	}

	@ApiMethod(name = "addStockItem", path = "addStockItem")
	public StockItemEntity addStockItem(StockItemEntity stockItemEntity) {
		ofy().save().entity(stockItemEntity).now();
		return stockItemEntity;
	}

	@ApiMethod(name = "addStockReceipt", path = "addStockReceipt")
	public StockItemsReceiptEntity addStockReceipt(StockItemsReceiptEntity stockItemsReceipt) {
		logger.info("Inside addStockReceipt...");
		if (stockItemsReceipt.getStatus() == DocumentStatus.FINALIZED && stockItemsReceipt.isStatusAlreadyFinalized()) {
			throw new RuntimeException("Save not allowed. StockItemsReceiptEntity is already FINALIZED: "
					+ this.getClass().getSimpleName() + " Finalized entity can't be altered.");
		}

		List<StockLineItem> productLineItemList = stockItemsReceipt.getProductLineItemList();

		for (StockLineItem stockLineItem : productLineItemList) {
			StockItemEntity stockItem = getOrCreateWarehouseStockItem(stockItemsReceipt.getWarehouse(), stockLineItem);
			stockLineItem.setStockItem(stockItem);
			if (stockLineItem.getStockItem().getStockItemType().isMaintainStockBySerialNumber()) {
				List<StockItemInstanceEntity> stockItemInstanceList = stockLineItem.getStockItemInstanceList();
				for (StockItemInstanceEntity stockItemInstanceEntity : stockItemInstanceList) {
					stockItemInstanceEntity.setBusiness(stockItem.getBusiness());
					stockItemInstanceEntity.setStockItemId(stockItem.getId().toString());
					stockItemInstanceEntity.setStockReceiptNumber(stockItemsReceipt.getItemNumber());
				}
			}

		}

		if (stockItemsReceipt.getStatus() == DocumentStatus.FINALIZED) {
			// Perform stock adjustment only when this entity is finalized and
			// not in DRAFT status.
			List<StockItemTxnEntity> stockItemTxnList = new ArrayList<StockItemTxnEntity>();
			List<StockItemInstanceEntity> stockItemInstanceToUpdateList = new ArrayList<StockItemInstanceEntity>();

			for (StockLineItem stockLineItem : productLineItemList) {
				// This needed so that StockItemService.adjustStockItems adds
				// the
				// stock items
				int qtyDiff = stockLineItem.getQty() - stockLineItem.getStockMaintainedQty();
				if (qtyDiff != 0) {
					StockItemTxnEntity txnEntity = new StockItemTxnEntity();
					stockItemTxnList.add(txnEntity);
					txnEntity.setBusiness(stockItemsReceipt.getBusiness());
					txnEntity.setStockItem(stockLineItem.getStockItem());
					txnEntity.setQty(Math.abs(qtyDiff));
					txnEntity.setPrice(stockLineItem.getPrice());

					if (qtyDiff > 0) {
						txnEntity.setTxnType(StockItemTxnEntity.StockTxnType.CR);
					} else {
						txnEntity.setTxnType(StockItemTxnEntity.StockTxnType.DR);
					}

					stockLineItem.setStockMaintainedQty(stockLineItem.getQty());
				}
				if (stockLineItem.getStockItem().getStockItemType().isMaintainStockBySerialNumber()) {
					stockItemInstanceToUpdateList.addAll(stockLineItem.getStockItemInstanceList());
				}

			}
			if (stockItemTxnList.size() > 0) {
				addStockItemTxnList(stockItemTxnList, stockItemInstanceToUpdateList);
			}
		}

		ofy().save().entity(stockItemsReceipt).now();

		// if entity is saved directly as finalized, need to update item id
		if (stockItemsReceipt.getStatus() == DocumentStatus.FINALIZED) {
			updateRefEntityIdsIfMissing(stockItemsReceipt, productLineItemList);
		}

		return stockItemsReceipt;
	}

	private void updateRefEntityIdsIfMissing(Object toUpdateEntity, List<StockLineItem> productLineItemList) {

		List<StockItemInstanceEntity> stockItemInstanceToUpdateList = new ArrayList<StockItemInstanceEntity>();

		for (StockLineItem stockLineItem : productLineItemList) {
			if (stockLineItem.getStockItem().getStockItemType().isMaintainStockBySerialNumber()) {
				List<StockItemInstanceEntity> stockItemInstanceList = stockLineItem.getStockItemInstanceList();
				for (StockItemInstanceEntity stockItemInstanceEntity : stockItemInstanceList) {
					if (stockItemInstanceEntity.getStockReceiptNumber() == 0
							&& stockItemInstanceEntity.getStockShipmentNumber() == 0
							&& stockItemInstanceEntity.getInvoiceNumber() == 0) {
						stockItemInstanceToUpdateList.add(stockItemInstanceEntity);

						if (toUpdateEntity instanceof StockItemsReceiptEntity) {
							stockItemInstanceEntity
									.setStockReceiptNumber(((StockItemsReceiptEntity) toUpdateEntity).getItemNumber());
						} else if (toUpdateEntity instanceof StockItemsShipmentEntity) {
							stockItemInstanceEntity.setStockShipmentNumber(
									((StockItemsShipmentEntity) toUpdateEntity).getItemNumber());
						} else if (toUpdateEntity instanceof InvoiceEntity) {
							stockItemInstanceEntity.setInvoiceNumber(((InvoiceEntity) toUpdateEntity).getItemNumber());
						}
					}
				}
			}

		}
	}

	private StockItemEntity getOrCreateWarehouseStockItem(WarehouseEntity warehouse, StockLineItem stockLineItem) {
		List<StockItemEntity> filteredStocks = ofy().load().type(StockItemEntity.class)
				.ancestor(warehouse.getBusiness()).filter("warehouse", warehouse)
				.filter("stockItemType", stockLineItem.getStockItem().getStockItemType()).list();
		if (filteredStocks == null || filteredStocks.size() > 0) {
			return filteredStocks.get(0);
		}

		StockItemEntity stockItemEntity = new StockItemEntity();
		stockItemEntity.setBusiness(warehouse.getBusiness());
		stockItemEntity.setWarehouse(warehouse);
		stockItemEntity.setStockItemType(stockLineItem.getStockItem().getStockItemType());
		stockItemEntity.setCost(stockLineItem.getStockItem().getCost());
		stockItemEntity.setMovingAvgCost(stockLineItem.getStockItem().getMovingAvgCost());

		return addStockItem(stockItemEntity);

	}

	@ApiMethod(name = "addStockShipment", path = "addStockShipment")
	public StockItemsShipmentEntity addStockShipment(StockItemsShipmentEntity stockItemsShipment) {
		if (stockItemsShipment.getStatus() == DocumentStatus.FINALIZED
				&& stockItemsShipment.isStatusAlreadyFinalized()) {
			throw new RuntimeException("Save not allowed. StockItemsShipmentEntity is already FINALIZED: "
					+ this.getClass().getSimpleName() + " Finalized entity can't be altered.");
		}

		List<StockLineItem> productLineItemList = stockItemsShipment.getProductLineItemList();

		if (stockItemsShipment.getStatus() == DocumentStatus.FINALIZED) {
			List<StockLineItem> stockLineItemsToProcess = new ArrayList<StockLineItem>();
			if (stockItemsShipment.getShipmentType() != null
					&& stockItemsShipment.getShipmentType() == ShipmentType.TO_OTHER_WAREHOUSE
					&& stockItemsShipment.getToWH() != null) {

				for (StockLineItem stockLineItem : productLineItemList) {
					stockLineItemsToProcess.add(stockLineItem);
					StockLineItem copy = StockLineItem.getCopy(stockLineItem);
					StockItemEntity stockItem = getOrCreateWarehouseStockItem(stockItemsShipment.getToWH(), copy);
					copy.setStockItem(stockItem);
					// So that credits in adjustStockItems fn
					copy.setStockMaintainedQty(copy.getQty() * 2);
					stockLineItemsToProcess.add(copy);

					if (stockLineItem.getStockItem().getStockItemType().isMaintainStockBySerialNumber()) {
						List<StockItemInstanceEntity> stockItemInstanceList = stockLineItem.getStockItemInstanceList();
						for (StockItemInstanceEntity stockItemInstanceEntity : stockItemInstanceList) {
							stockItemInstanceEntity.setBusiness(stockItem.getBusiness());
							stockItemInstanceEntity.setStockItemId(stockItem.getId().toString());
							stockItemInstanceEntity.setStockShipmentNumber(stockItemsShipment.getItemNumber());
						}
					}
					// So that credits in adjustStockItems fn
					copy.setStockMaintainedQty(stockLineItem.getQty() * 2);
					stockLineItemsToProcess.add(copy);
				}
			} else {
				stockLineItemsToProcess = stockItemsShipment.getProductLineItemList();
			}

			// Process stock items
			StockManagementService.adjustStockItems(stockItemsShipment.getBusiness(), stockLineItemsToProcess);
		} // enf of FINALIZED if

		ofy().save().entity(stockItemsShipment).now();
		// if entity is saved directly as finalized, need to update item id
		if (stockItemsShipment.getStatus() == DocumentStatus.FINALIZED) {
			updateRefEntityIdsIfMissing(stockItemsShipment, productLineItemList);
		}
		return stockItemsShipment;
	}

	@ApiMethod(name = "getStockItemTypes", path = "getStockItemTypes")
	public List<StockItemTypeEntity> getStockItemTypes(@Named("busId") Long busId) {
		List<StockItemTypeEntity> typeList = ofy().load().type(StockItemTypeEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return typeList;
	}

	@ApiMethod(name = "filterStockItemsByWarehouse", path = "filterStockItemsByWarehouse")
	public List<StockItemEntity> filterStockItemsByWarehouse(WarehouseEntity warehouse) {
		// System.out.println("getAllStock#busId:" + busId);
		List<StockItemEntity> filteredStocks = ofy().load().type(StockItemEntity.class)
				.ancestor(warehouse.getBusiness()).filter("warehouse", warehouse).list();

		return filteredStocks;
	}

	@ApiMethod(name = "getAllStockItems", path = "getAllStockItems")
	public List<StockItemEntity> getAllStockItems(@Named("busId") Long busId) {
		// System.out.println("getAllStock#busId:" + busId);
		List<StockItemEntity> filteredStocks = ofy().load().type(StockItemEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return filteredStocks;
	}

	@ApiMethod(name = "getStockReceiptList", path = "getStockReceiptList")
	public List<StockItemsReceiptEntity> getStockReceiptList(@Named("busId") Long busId) {
		// System.out.println("getAllStock#busId:" + busId);
		List<StockItemsReceiptEntity> stockItemsShipmentList = ofy().load().type(StockItemsReceiptEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return stockItemsShipmentList;
	}

	@ApiMethod(name = "getStockReceiptByID", path = "getStockReceiptByID")
	public StockItemsReceiptEntity getStockReceiptByID(@Named("busId") Long busId, @Named("id") Long stRecptID) {

		List<StockItemsReceiptEntity> list = ofy().load().type(StockItemsReceiptEntity.class)
				.filterKey(
						Key.create(Key.create(BusinessEntity.class, busId), StockItemsReceiptEntity.class, stRecptID))
				.list();

		StockItemsReceiptEntity foundStockReceipt = list.size() > 0 ? list.get(0) : null;
		return foundStockReceipt;

	}

	@ApiMethod(name = "getStockShipmentList", path = "getStockShipmentList")
	public List<StockItemsShipmentEntity> getStockShipmentList(@Named("busId") Long busId) {
		// System.out.println("getAllStock#busId:" + busId);
		List<StockItemsShipmentEntity> stockItemsShipmentList = ofy().load().type(StockItemsShipmentEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return stockItemsShipmentList;
	}

	@ApiMethod(name = "getStockShipmentByID", path = "getStockShipmentByID")
	public StockItemsShipmentEntity getStockShipmentByID(@Named("busId") Long busId, @Named("id") Long stShipId) {

		List<StockItemsShipmentEntity> list = ofy().load().type(StockItemsShipmentEntity.class)
				.filterKey(
						Key.create(Key.create(BusinessEntity.class, busId), StockItemsShipmentEntity.class, stShipId))
				.list();

		StockItemsShipmentEntity foundStockReceipt = list.size() > 0 ? list.get(0) : null;
		return foundStockReceipt;

	}

	@ApiMethod(name = "getStockItemInstancesList", path = "getStockItemInstancesList", httpMethod = HttpMethod.POST)
	public List<StockItemInstanceEntity> getStockItemInstancesList(StockItemEntity stockItem) {
		// System.out.println("getAllStock#busId:" + busId);
		List<StockItemInstanceEntity> list = ofy().load().type(StockItemInstanceEntity.class)
				.ancestor(Key.create(BusinessEntity.class, stockItem.getBusiness().getId()))
				.filter("stockItemId", stockItem.getId().toString()).list();
		return list;
	}

	/*
	 * @ApiMethod(name = "saveStockItemInstancesList", path =
	 * "saveStockItemInstancesList", httpMethod = HttpMethod.POST) public void
	 * saveStockItemInstancesList(List<StockItemInstanceEntity> list) {
	 * ofy().save().entities(list); System.out.println("list.size:" +
	 * list.size()); }
	 */

	@ApiMethod(name = "getReportByThreshold", path = "getReportByThreshold")
	public List<StockItemEntity> getReportByThreshold(@Named("busId") Long bussinessId) {
		List<StockItemEntity> stocks = ofy().load().type(StockItemEntity.class)
				.ancestor(Key.create(BusinessEntity.class, bussinessId)).list();
		List<StockItemEntity> filteredThresholdStocks = new ArrayList<StockItemEntity>();

		for (int i = 0; i < stocks.size(); i++) {
			if (stocks.get(i).getQty() <= stocks.get(i).getThresholdValue()) {
				filteredThresholdStocks.add(stocks.get(i));
			}
		}
		logger.info("filteredThresholdStocks#size:" + filteredThresholdStocks.size());
		return filteredThresholdStocks;
	}

	public static void adjustStockItems(BusinessEntity business, List<StockLineItem> productLineItemList) {
		/* For Reduce the Stock Quantity */
		List<StockItemTxnEntity> stockItemTxnList = new ArrayList<StockItemTxnEntity>();
		List<StockItemInstanceEntity> stockItemInstanceToUpdateList = new ArrayList<StockItemInstanceEntity>();

		for (StockLineItem invoiceLineItem : productLineItemList) {
			int qtyDiff = invoiceLineItem.getQty() - invoiceLineItem.getStockMaintainedQty();
			if (invoiceLineItem.getStockItem().isMaintainStock() && qtyDiff != 0) {

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
				invoiceLineItem.setStockMaintainedQty(Math.abs(invoiceLineItem.getQty()));
				stockItemTxnList.add(txnEntity);
				if (invoiceLineItem.getStockItem().getStockItemType().isMaintainStockBySerialNumber()
						&& invoiceLineItem.getStockItemInstanceList() != null) {
					stockItemInstanceToUpdateList.addAll(invoiceLineItem.getStockItemInstanceList());
				}
			}

		}
		if (stockItemTxnList.size() > 0) {
			addStockItemTxnList(stockItemTxnList, stockItemInstanceToUpdateList);
		}
	}

	private static void addStockItemTxnList(List<StockItemTxnEntity> stockItemTxnList,
			List<StockItemInstanceEntity> stockItemInstanceToUpdateList) {
		List<StockItemEntity> stockItemToUpdateList = new ArrayList<StockItemEntity>();
		for (StockItemTxnEntity stockItemTxnEntity : stockItemTxnList) {
			StockItemEntity stockItemEntity = stockItemTxnEntity.getStockItem();
			if (stockItemTxnEntity.getTxnType() == StockItemTxnEntity.StockTxnType.DR) {
				stockItemEntity.setQty(stockItemEntity.getQty() - stockItemTxnEntity.getQty());
			} else {
				// This means stock addition/receipt
				// calculate moving avb. cost
				double movingAvgCost = (stockItemEntity.getQty() * stockItemEntity.getMovingAvgCost()
						+ stockItemTxnEntity.getQty() * stockItemTxnEntity.getPrice())
						/ (stockItemEntity.getQty() + stockItemTxnEntity.getQty());
				movingAvgCost = (double) Math.round(movingAvgCost * 100) / 100;
				stockItemEntity.setMovingAvgCost(movingAvgCost);
				stockItemEntity.setQty(stockItemEntity.getQty() + stockItemTxnEntity.getQty());
			}

			stockItemToUpdateList.add(stockItemEntity);
		}

		ofy().save().entities(stockItemTxnList);
		ofy().save().entities(stockItemToUpdateList);
		if (stockItemInstanceToUpdateList.size() > 0)
			ofy().save().entities(stockItemInstanceToUpdateList);
	}

	@ApiMethod(name = "getStockItemTxnList", path = "getStockItemTxnList")
	public List<StockItemTxnEntity> getStockItemTxnList(@Named("busId") Long busId, @Named("fromDate") Date fromDate,
			@Named("txnType") String txnType) {

		Query<StockItemTxnEntity> filterQuery = ofy().load().type(StockItemTxnEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).filter("createdDate >=", fromDate);

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
	public List<SupplierEntity> getAllSuppliersByBusiness(@Named("busId") Long busId) {

		List<SupplierEntity> filteredSuppliers = ofy().load().type(SupplierEntity.class)
				.filter("business", Ref.create(Key.create(BusinessEntity.class, busId))).list();

		return filteredSuppliers;
	}

	@ApiMethod(name = "addPurchaseOrder", path = "addPurchaseOrder")
	public PurchaseOrderEntity addPurchaseOrder(PurchaseOrderEntity purchaseOrderEntity) {

		if (purchaseOrderEntity.getStatus() == DocumentStatus.FINALIZED
				&& purchaseOrderEntity.isStatusAlreadyFinalized()) {
			throw new RuntimeException("Save not allowed. StockItemsReceiptEntity is already FINALIZED: "
					+ this.getClass().getSimpleName() + " Finalized entity can't be altered.");
		}
		List<StockLineItem> productLineItemList = purchaseOrderEntity.getProductLineItemList();

		for (StockLineItem stockLineItem : productLineItemList) {
			StockItemEntity stockItem = getOrCreateWarehouseStockItem(purchaseOrderEntity.getWarehouse(),
					stockLineItem);
			stockLineItem.setStockItem(stockItem);
		}

		ofy().save().entity(purchaseOrderEntity).now();
		if (purchaseOrderEntity.getStatus() == DocumentStatus.FINALIZED && purchaseOrderEntity.getBudget() != null
				&& purchaseOrderEntity.getBudgetLineItem() != null) {
			// Reduce Budget Balance
			BudgetEntity budget = purchaseOrderEntity.getBudget();
			List<LineItemCategory> categoryList = budget.getCategoryList();
			for (LineItemCategory lineItemCat : categoryList) {
				if (lineItemCat.getCategoryName()
						.equalsIgnoreCase(purchaseOrderEntity.getBudgetLineItemCategory().getCategoryName().trim())) {
					List<LineItemEntity> items = lineItemCat.getItems();
					for (LineItemEntity lineItemEntity : items) {
						if (lineItemEntity.getItemName()
								.equalsIgnoreCase(purchaseOrderEntity.getBudgetLineItem().getItemName().trim())) {
							lineItemEntity.setCurrentBudgetBalance(
									lineItemEntity.getCurrentBudgetBalance() - purchaseOrderEntity.getFinalTotal());
							// save async
							ofy().save().entity(budget);
							break;
						}
					}
				}

			}

		}
		return purchaseOrderEntity;
	}

	@ApiMethod(name = "addRequisition", path = "addRequisition")
	public RequisitionEntity addRequisition(RequisitionEntity requisitionEntity) {

		if (requisitionEntity.getStatus() == DocumentStatus.FINALIZED && requisitionEntity.isStatusAlreadyFinalized()) {
			throw new RuntimeException("Save not allowed. Requisition entity is already FINALIZED: "
					+ this.getClass().getSimpleName() + " Finalized entity can't be altered.");
		}
		ofy().save().entity(requisitionEntity).now();
		return requisitionEntity;

	}

	@ApiMethod(name = "fetchRequisitionListByPaging", path = "fetchRequisitionListByPaging")
	public EntityPagingInfo fetchRequisitionListByPaging(@Named("id") Long busId, @Named("status") String status,
			EntityPagingInfo pagingInfo) {
		if (status != null && !status.isEmpty()) {
			DocumentStatus statusType = DocumentStatus.valueOf(status.toUpperCase(Locale.ENGLISH));
			return super.fetchEntityListByPaging(busId, RequisitionEntity.class, pagingInfo, statusType);
		} else {
			return super.fetchEntityListByPaging(busId, RequisitionEntity.class, pagingInfo);
		}
	}

	@ApiMethod(name = "addBudget", path = "addBudget")
	public BudgetEntity addBudget(BudgetEntity budgetEntity) {
		if (budgetEntity.getStatus() == DocumentStatus.FINALIZED) {
			throw new RuntimeException("Save not allowed. Budget entity has already been finalized."
					+ this.getClass().getSimpleName() + "Finalized entity can't be altered.");
		}

		ofy().save().entity(budgetEntity).now();
		return budgetEntity;
	}

	@ApiMethod(name = "fetchBudgetListByPaging", path = "fetchBudgetListByPaging")
	public EntityPagingInfo fetchBudgetListByPaging(@Named("id") Long busId, @Named("status") String status,
			EntityPagingInfo pagingInfo) {
		if (status != null && !status.isEmpty()) {
			DocumentStatus statusType = DocumentStatus.valueOf(status.toUpperCase(Locale.ENGLISH));
			return super.fetchEntityListByPaging(busId, BudgetEntity.class, pagingInfo, statusType);
		} else {
			return super.fetchEntityListByPaging(busId, BudgetEntity.class, pagingInfo);
		}
	}

	@ApiMethod(name = "getAllPurchaseOrder", path = "getAllPurchaseOrder")
	public List<PurchaseOrderEntity> getAllPurchaseOrder(@Named("id") Long busId) {
		List<PurchaseOrderEntity> filteredPO = ofy().load().type(PurchaseOrderEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredPO;
	}

	@ApiMethod(name = "fetchPOListByPaging", path = "fetchPOListByPaging")
	public EntityPagingInfo fetchPOListByPaging(@Named("id") Long busId, @Named("status") String status,
			EntityPagingInfo pagingInfo) {
		if (status != null && !status.isEmpty()) {
			DocumentStatus statusType = DocumentStatus.valueOf(status.toUpperCase(Locale.ENGLISH));
			return super.fetchEntityListByPaging(busId, PurchaseOrderEntity.class, pagingInfo, statusType);
		} else {
			return super.fetchEntityListByPaging(busId, PurchaseOrderEntity.class, pagingInfo);
		}
	}

	@ApiMethod(name = "fetchReceiptListByPaging", path = "fetchReceiptListByPaging")
	public EntityPagingInfo fetchReceiptListByPaging(@Named("id") Long busId, @Named("status") String status,
			EntityPagingInfo pagingInfo) {
		if (status != null && !status.isEmpty()) {
			DocumentStatus statusType = DocumentStatus.valueOf(status.toUpperCase(Locale.ENGLISH));
			return super.fetchEntityListByPaging(busId, StockItemsReceiptEntity.class, pagingInfo, statusType);
		} else {
			return super.fetchEntityListByPaging(busId, StockItemsReceiptEntity.class, pagingInfo);
		}
	}

	@ApiMethod(name = "fetchShipmentListByPaging", path = "fetchShipmentListByPaging")
	public EntityPagingInfo fetchShipmentListByPaging(@Named("id") Long busId, @Named("status") String status,
			EntityPagingInfo pagingInfo) {
		if (status != null && !status.isEmpty()) {
			DocumentStatus statusType = DocumentStatus.valueOf(status.toUpperCase(Locale.ENGLISH));
			return super.fetchEntityListByPaging(busId, StockItemsShipmentEntity.class, pagingInfo, statusType);
		} else {
			return super.fetchEntityListByPaging(busId, StockItemsShipmentEntity.class, pagingInfo);
		}
	}

	@ApiMethod(name = "getPOByItemNumber", path = "getPOByItemNumber")
	public PurchaseOrderEntity getPOByItemNumber(@Named("itemNumber") int itemNumber) {
		Object entityByItemNumber = super.getEntityByItemNumber(PurchaseOrderEntity.class, itemNumber);
		if (entityByItemNumber != null)
			return (PurchaseOrderEntity) entityByItemNumber;
		else
			return null;
	}

	@ApiMethod(name = "getPOByID", path = "getPOByID")
	public PurchaseOrderEntity getPOByID(@Named("busId") Long busId, @Named("id") Long poId) {

		List<PurchaseOrderEntity> list = ofy().load().type(PurchaseOrderEntity.class)
				.filterKey(Key.create(Key.create(BusinessEntity.class, busId), PurchaseOrderEntity.class, poId)).list();
		PurchaseOrderEntity foundPO = list.size() > 0 ? list.get(0) : null;
		return foundPO;
	}

	@ApiMethod(name = "addStockSettings", path = "addStockSettings")
	public StockSettingsEntity addStockSettings(StockSettingsEntity settingsEntity) {
		ofy().save().entity(settingsEntity).now();
		return settingsEntity;
	}

	@ApiMethod(name = "getStockSettingsByBiz", path = "getStockSettingsByBiz")
	public StockSettingsEntity getStockSettingsByBiz(@Named("id") Long busId) {
		StockSettingsEntity filteredSettings = ofy().load().type(StockSettingsEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).first().now();

		return filteredSettings;

	}

}// end of StockServices
