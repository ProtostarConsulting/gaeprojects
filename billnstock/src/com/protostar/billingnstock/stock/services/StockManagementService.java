package com.protostar.billingnstock.stock.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.TxnType;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.purchase.entities.BudgetEntity;
import com.protostar.billingnstock.purchase.entities.LineItemCategory;
import com.protostar.billingnstock.purchase.entities.LineItemEntity;
import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
import com.protostar.billingnstock.purchase.entities.RequisitionEntity;
import com.protostar.billingnstock.purchase.entities.SupplierEntity;
import com.protostar.billingnstock.stock.entities.StockItemBrand;
import com.protostar.billingnstock.stock.entities.StockItemEntity;
import com.protostar.billingnstock.stock.entities.StockItemInstanceEntity;
import com.protostar.billingnstock.stock.entities.StockItemOrderType;
import com.protostar.billingnstock.stock.entities.StockItemProductTypeEntity;
import com.protostar.billingnstock.stock.entities.StockItemTxnEntity;
import com.protostar.billingnstock.stock.entities.StockItemTxnEntity.StockTxnType;
import com.protostar.billingnstock.stock.entities.StockItemTypeCategory;
import com.protostar.billingnstock.stock.entities.StockItemTypeEntity;
import com.protostar.billingnstock.stock.entities.StockItemTypeFilterWrapper;
import com.protostar.billingnstock.stock.entities.StockItemUnit;
import com.protostar.billingnstock.stock.entities.StockItemsReceiptEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity.ShipmentType;
import com.protostar.billingnstock.stock.entities.StockLineItem;
import com.protostar.billingnstock.stock.entities.StockSettingsEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.protostar.billingnstock.warehouse.entities.WarehouseEntity;
import com.protostar.billnstock.service.BaseService;
import com.protostar.billnstock.until.data.Constants.DocumentStatus;
import com.protostar.billnstock.until.data.EmailHandler;
import com.protostar.billnstock.until.data.EntityPagingInfo;
import com.protostar.billnstock.until.data.EntityUtil;

@Api(name = "stockService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.stock.services", ownerName = "com.protostar.billingnstock.stock.services", packagePath = ""))
public class StockManagementService extends BaseService {

	private final Logger logger = Logger.getLogger(StockManagementService.class
			.getName());

	@ApiMethod(name = "addStockItemType", path = "addStockItemType")
	public StockItemTypeEntity addStockItemType(
			StockItemTypeEntity stockItemTypeEntity) {
		ofy().save().entity(stockItemTypeEntity).now();
		return stockItemTypeEntity;
	}

	@ApiMethod(name = "addStockItemProductBrand", path = "addStockItemProductBrand")
	public StockItemBrand addStockItemProductBrand(StockItemBrand productBrand) {
		ofy().save().entity(productBrand).now();
		return productBrand;
	}

	@ApiMethod(name = "addStockItemProductType", path = "addStockItemProductType")
	public StockItemProductTypeEntity addStockItemProductType(
			StockItemProductTypeEntity productTypeEntity) {
		ofy().save().entity(productTypeEntity).now();
		return productTypeEntity;
	}

	@ApiMethod(name = "addStockItemUnit", path = "addStockItemUnit")
	public StockItemUnit addStockItemUnit(StockItemUnit stockItemUnit) {
		ofy().save().entity(stockItemUnit).now();
		return stockItemUnit;
	}

	@ApiMethod(name = "getStockItemUnits", path = "getStockItemUnits")
	public List<StockItemUnit> getStockItemUnits(@Named("busId") Long busId) {

		List<StockItemUnit> stockItemUnits = ofy().load()
				.type(StockItemUnit.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return stockItemUnits;
	}

	@ApiMethod(name = "getStockItemBrands", path = "getStockItemBrands")
	public List<StockItemBrand> getStockItemBrands(@Named("busId") Long busId) {
		List<StockItemBrand> stockItemBrands = ofy().load()
				.type(StockItemBrand.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return stockItemBrands;
	}

	@ApiMethod(name = "getStockItemProductTypes", path = "getStockItemProductTypes")
	public List<StockItemProductTypeEntity> getStockItemProductTypes(
			@Named("busId") Long busId) {

		List<StockItemProductTypeEntity> productTypes = ofy().load()
				.type(StockItemProductTypeEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return productTypes;
	}

	@ApiMethod(name = "addStockItemOrderType", path = "addStockItemOrderType")
	public StockItemOrderType addStockItemOrderType(
			StockItemOrderType stockItemOrderType) {
		ofy().save().entity(stockItemOrderType).now();
		return stockItemOrderType;
	}

	@ApiMethod(name = "getStockItemOrderTypes", path = "getStockItemOrderTypes")
	public List<StockItemOrderType> getStockItemOrderTypes(
			@Named("busId") Long busId) {

		List<StockItemOrderType> stockItemOrderTypes = ofy().load()
				.type(StockItemOrderType.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return stockItemOrderTypes;
	}

	@ApiMethod(name = "addStockItemTypeCategory", path = "addStockItemTypeCategory")
	public StockItemTypeCategory addStockItemTypeCategory(
			StockItemTypeCategory stockItemTypeCategory) {
		ofy().save().entity(stockItemTypeCategory).now();
		return stockItemTypeCategory;
	}

	@ApiMethod(name = "getStockItemTypeCategories", path = "getStockItemTypeCategories")
	public List<StockItemTypeCategory> getStockItemTypeCategories(
			@Named("busId") Long busId) {

		List<StockItemTypeCategory> stockItemTypeCategories = ofy().load()
				.type(StockItemTypeCategory.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return stockItemTypeCategories;
	}

	@ApiMethod(name = "addStockItem", path = "addStockItem")
	public StockItemEntity addStockItem(StockItemEntity stockItemEntity) {
		ofy().save().entity(stockItemEntity).now();
		return stockItemEntity;
	}

	@ApiMethod(name = "addStockReceipt", path = "addStockReceipt")
	public StockItemsReceiptEntity addStockReceipt(
			StockItemsReceiptEntity documentEntity) throws MessagingException,
			IOException {
		logger.info("Inside addStockReceipt...");
		if (documentEntity.getStatus() == DocumentStatus.FINALIZED
				&& documentEntity.isStatusAlreadyFinalized()) {
			throw new RuntimeException(
					"Save not allowed. StockItemsReceiptEntity is already FINALIZED: "
							+ this.getClass().getSimpleName()
							+ " Finalized entity can't be altered.");
		}

		documentEntity = ofy().execute(TxnType.REQUIRED,
				new Work<StockItemsReceiptEntity>() {
					private StockItemsReceiptEntity documentEntity;

					private Work<StockItemsReceiptEntity> init(
							StockItemsReceiptEntity documentEntity) {
						this.documentEntity = documentEntity;
						return this;
					}

					public StockItemsReceiptEntity run() {
						List<StockLineItem> productLineItemList = documentEntity
								.getProductLineItemList();

						for (StockLineItem stockLineItem : productLineItemList) {
							StockItemEntity stockItem = getOrCreateWarehouseStockItem(
									documentEntity.getWarehouse(),
									stockLineItem);
							stockLineItem.setStockItem(stockItem);
							if (stockLineItem.getStockItem().getStockItemType()
									.isMaintainStockBySerialNumber()) {
								List<StockItemInstanceEntity> stockItemInstanceList = stockLineItem
										.getStockItemInstanceList();
								for (StockItemInstanceEntity stockItemInstanceEntity : stockItemInstanceList) {
									stockItemInstanceEntity
											.setBusiness(stockItem
													.getBusiness());
									stockItemInstanceEntity
											.setStockItemId(stockItem.getId()
													.toString());
									stockItemInstanceEntity
											.setStockReceiptNumber(documentEntity
													.getItemNumber());
								}
							}

						}

						if (documentEntity.getStatus() == DocumentStatus.FINALIZED) {
							// Perform stock adjustment only when this entity is
							// finalized and
							// not in DRAFT status.
							List<StockItemTxnEntity> stockItemTxnList = new ArrayList<StockItemTxnEntity>();
							List<StockItemInstanceEntity> stockItemInstanceToUpdateList = new ArrayList<StockItemInstanceEntity>();

							for (StockLineItem stockLineItem : productLineItemList) {
								// This needed so that
								// StockItemService.adjustStockItems
								// adds
								// the
								// stock items
								int qtyDiff = stockLineItem.getQty()
										- stockLineItem.getStockMaintainedQty();
								if (qtyDiff != 0) {
									StockItemTxnEntity txnEntity = new StockItemTxnEntity();
									stockItemTxnList.add(txnEntity);
									txnEntity.setBusiness(documentEntity
											.getBusiness());
									txnEntity.setStockItem(stockLineItem
											.getStockItem());
									txnEntity.setQty(Math.abs(qtyDiff));
									txnEntity.setPrice(stockLineItem.getPrice());
									txnEntity
											.setStockReceiptNumber(documentEntity
													.getItemNumber());

									if (qtyDiff > 0) {
										txnEntity
												.setTxnType(StockItemTxnEntity.StockTxnType.CR);
									} else {
										txnEntity
												.setTxnType(StockItemTxnEntity.StockTxnType.DR);
									}

									stockLineItem
											.setStockMaintainedQty(stockLineItem
													.getQty());
								}
								if (stockLineItem.getStockItem()
										.getStockItemType()
										.isMaintainStockBySerialNumber()) {
									stockItemInstanceToUpdateList.addAll(stockLineItem
											.getStockItemInstanceList());
								}

							}
							if (stockItemTxnList.size() > 0) {
								addStockItemTxnList(stockItemTxnList,
										stockItemInstanceToUpdateList);
							}
						}

						// if entity is saved directly as finalized, need to
						// update item
						// id
						if (documentEntity.getStatus() == DocumentStatus.FINALIZED) {
							updateRefEntityIdsIfMissing(documentEntity,
									productLineItemList);
						}
						ofy().save().entity(documentEntity).now();
						return documentEntity;
					}
				}.init(documentEntity));

		if (documentEntity.getStatus() != DocumentStatus.DRAFT) {
			new EmailHandler().sendStockReceiptEmail(documentEntity);
		}

		return documentEntity;
	}

	private void updateRefEntityIdsIfMissing(Object toUpdateEntity,
			List<StockLineItem> productLineItemList) {

		List<StockItemInstanceEntity> stockItemInstanceToUpdateList = new ArrayList<StockItemInstanceEntity>();

		for (StockLineItem stockLineItem : productLineItemList) {
			if (stockLineItem.getStockItem().getStockItemType()
					.isMaintainStockBySerialNumber()) {
				List<StockItemInstanceEntity> stockItemInstanceList = stockLineItem
						.getStockItemInstanceList();
				for (StockItemInstanceEntity stockItemInstanceEntity : stockItemInstanceList) {
					if (stockItemInstanceEntity.getStockReceiptNumber() == 0
							&& stockItemInstanceEntity.getStockShipmentNumber() == 0
							&& stockItemInstanceEntity.getInvoiceNumber() == 0) {
						stockItemInstanceToUpdateList
								.add(stockItemInstanceEntity);

						if (toUpdateEntity instanceof StockItemsReceiptEntity) {
							stockItemInstanceEntity
									.setStockReceiptNumber(((StockItemsReceiptEntity) toUpdateEntity)
											.getItemNumber());
						} else if (toUpdateEntity instanceof StockItemsShipmentEntity) {
							stockItemInstanceEntity
									.setStockShipmentNumber(((StockItemsShipmentEntity) toUpdateEntity)
											.getItemNumber());
						} else if (toUpdateEntity instanceof InvoiceEntity) {
							stockItemInstanceEntity
									.setInvoiceNumber(((InvoiceEntity) toUpdateEntity)
											.getItemNumber());
						}
					}
				}
			}

		}
	}

	private StockItemEntity getOrCreateWarehouseStockItem(
			WarehouseEntity warehouse, StockLineItem stockLineItem) {
		List<StockItemEntity> filteredStocks = ofy()
				.load()
				.type(StockItemEntity.class)
				.ancestor(warehouse.getBusiness())
				.filter("warehouse", warehouse)
				.filter("stockItemType",
						stockLineItem.getStockItem().getStockItemType()).list();
		if (filteredStocks == null || filteredStocks.size() > 0) {
			return filteredStocks.get(0);
		}

		StockItemEntity stockItemEntity = new StockItemEntity();
		stockItemEntity.setBusiness(warehouse.getBusiness());
		stockItemEntity.setWarehouse(warehouse);
		stockItemEntity.setStockItemType(stockLineItem.getStockItem()
				.getStockItemType());
		stockItemEntity.setCost(stockLineItem.getStockItem().getCost());
		stockItemEntity.setMovingAvgCost(stockLineItem.getStockItem()
				.getMovingAvgCost());

		return addStockItem(stockItemEntity);

	}

	@ApiMethod(name = "addStockShipment", path = "addStockShipment")
	public StockItemsShipmentEntity addStockShipment(
			StockItemsShipmentEntity documentEntity) throws MessagingException,
			IOException {
		if (documentEntity.getStatus() == DocumentStatus.FINALIZED
				&& documentEntity.isStatusAlreadyFinalized()) {
			throw new RuntimeException(
					"Save not allowed. StockItemsShipmentEntity is already FINALIZED: "
							+ this.getClass().getSimpleName()
							+ " Finalized entity can't be altered.");
		}

		documentEntity = ofy().execute(TxnType.REQUIRED,
				new Work<StockItemsShipmentEntity>() {
					private StockItemsShipmentEntity documentEntity;

					private Work<StockItemsShipmentEntity> init(
							StockItemsShipmentEntity documentEntity) {
						this.documentEntity = documentEntity;
						return this;
					}

					public StockItemsShipmentEntity run() {
						List<StockLineItem> productLineItemList = documentEntity
								.getProductLineItemList();

						if (documentEntity.getStatus() == DocumentStatus.FINALIZED) {
							List<StockLineItem> stockLineItemsToProcess = new ArrayList<StockLineItem>();
							if (documentEntity.getShipmentType() != null
									&& documentEntity.getShipmentType() == ShipmentType.TO_OTHER_WAREHOUSE
									&& documentEntity.getToWH() != null) {

								for (StockLineItem stockLineItem : productLineItemList) {
									stockLineItemsToProcess.add(stockLineItem);
									StockLineItem copy = StockLineItem
											.getCopy(stockLineItem);
									StockItemEntity stockItem = getOrCreateWarehouseStockItem(
											documentEntity.getToWH(), copy);
									copy.setStockItem(stockItem);
									// So that credits in adjustStockItems fn
									copy.setStockMaintainedQty(copy.getQty() * 2);
									stockLineItemsToProcess.add(copy);

									if (stockLineItem.getStockItem()
											.getStockItemType()
											.isMaintainStockBySerialNumber()) {
										List<StockItemInstanceEntity> stockItemInstanceList = stockLineItem
												.getStockItemInstanceList();
										for (StockItemInstanceEntity stockItemInstanceEntity : stockItemInstanceList) {
											stockItemInstanceEntity
													.setBusiness(stockItem
															.getBusiness());
											stockItemInstanceEntity
													.setStockItemId(stockItem
															.getId().toString());
											stockItemInstanceEntity
													.setStockShipmentNumber(documentEntity
															.getItemNumber());
										}
									}
									// So that credits in adjustStockItems fn
									copy.setStockMaintainedQty(stockLineItem
											.getQty() * 2);
									stockLineItemsToProcess.add(copy);
								}
							} else {
								stockLineItemsToProcess = documentEntity
										.getProductLineItemList();
							}

							// Process stock items
							StockManagementService.adjustStockItems(
									documentEntity.getBusiness(),
									stockLineItemsToProcess,
									documentEntity.getItemNumber(), 0);
						} // enf of FINALIZED if

						ofy().save().entity(documentEntity).now();
						return documentEntity;
					}
				}.init(documentEntity));

		if (documentEntity.getStatus() != DocumentStatus.DRAFT) {
			new EmailHandler().sendStockShipmentEmail(documentEntity);
		}

		return documentEntity;
	}

	@ApiMethod(name = "getStockItemTypes", path = "getStockItemTypes")
	public List<StockItemTypeEntity> getStockItemTypes(
			@Named("busId") Long busId) {
		List<StockItemTypeEntity> typeList = ofy().load()
				.type(StockItemTypeEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return typeList;
	}

	@ApiMethod(name = "filterStockItemsByWarehouse", path = "filterStockItemsByWarehouse")
	public List<StockItemEntity> filterStockItemsByWarehouse(
			WarehouseEntity warehouse) {
		// System.out.println("getAllStock#busId:" + busId);
		List<StockItemEntity> filteredStocks = ofy().load()
				.type(StockItemEntity.class).ancestor(warehouse.getBusiness())
				.filter("warehouse", warehouse).list();

		return filteredStocks;
	}

	@ApiMethod(name = "filterStockItemsByBrand", path = "filterStockItemsByBrand")
	public List<StockItemEntity> filterStockItemsByBrand(StockItemBrand brand) {
		List<StockItemTypeEntity> filteredStockItemTypes = ofy().load()
				.type(StockItemTypeEntity.class).ancestor(brand.getBusiness())
				.filter("brand", brand).list();

		List<StockItemEntity> filteredStocks = ofy().load()
				.type(StockItemEntity.class).ancestor(brand.getBusiness())
				.filter("stockItemType IN", filteredStockItemTypes).list();

		return filteredStocks;
	}

	@ApiMethod(name = "filterStockItemsByBrandAndWH", path = "filterStockItemsByBrandAndWH")
	public List<StockItemEntity> filterStockItemsByBrandAndWH(
			StockItemTypeFilterWrapper fitlerWrapper) {
		List<StockItemTypeEntity> filteredStockItemTypes = ofy().load()
				.type(StockItemTypeEntity.class)
				.ancestor(fitlerWrapper.getBrand().getBusiness())
				.filter("brand", fitlerWrapper.getBrand()).list();

		List<StockItemEntity> filteredStocks = new ArrayList<StockItemEntity>();
		if (filteredStockItemTypes.size() > 0) {
			filteredStocks = ofy().load().type(StockItemEntity.class)
					.ancestor(fitlerWrapper.getBrand().getBusiness())
					.filter("warehouse", fitlerWrapper.getWarehouse())
					.filter("stockItemType IN", filteredStockItemTypes).list();
		}
		return filteredStocks;
	}

	@ApiMethod(name = "filterStockItemsByProductType", path = "filterStockItemsByProductType")
	public List<StockItemEntity> filterStockItemsByProductType(
			StockItemProductTypeEntity productType) {
		List<StockItemTypeEntity> filteredStockItemTypes = ofy().load()
				.type(StockItemTypeEntity.class)
				.ancestor(productType.getBusiness())
				.filter("productType", productType).list();

		List<StockItemEntity> filteredStocks = new ArrayList<StockItemEntity>();
		if (filteredStockItemTypes.size() > 0) {

			filteredStocks = ofy().load().type(StockItemEntity.class)
					.ancestor(productType.getBusiness())
					.filter("stockItemType IN", filteredStockItemTypes).list();
		}
		return filteredStocks;
	}

	@ApiMethod(name = "filterStockItemsByProductTypeAndWH", path = "filterStockItemsByProductTypeAndWH")
	public List<StockItemEntity> filterStockItemsByProductTypeAndWH(
			StockItemTypeFilterWrapper fitlerWrapper) {
		List<StockItemTypeEntity> filteredStockItemTypes = ofy().load()
				.type(StockItemTypeEntity.class)
				.ancestor(fitlerWrapper.getProductType().getBusiness())
				.filter("productType", fitlerWrapper.getProductType()).list();

		List<StockItemEntity> filteredStocks = new ArrayList<StockItemEntity>();
		if (filteredStockItemTypes.size() > 0) {
			filteredStocks = ofy().load().type(StockItemEntity.class)
					.ancestor(fitlerWrapper.getProductType().getBusiness())
					.filter("warehouse", fitlerWrapper.getWarehouse())
					.filter("stockItemType IN", filteredStockItemTypes).list();
		}
		return filteredStocks;
	}

	@ApiMethod(name = "filterStockItemsByBrandAndProductTypeAndWH", path = "filterStockItemsByBrandAndProductTypeAndWH")
	public List<StockItemEntity> filterStockItemsByBrandAndProductTypeAndWH(
			@Named("busId") Long busId, StockItemTypeFilterWrapper fitlerWrapper) {

		UserService userService = new UserService();
		BusinessEntity business = userService.getBusinessById(busId);

		Query<StockItemTypeEntity> ancestorQuery = ofy().load()
				.type(StockItemTypeEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId));

		Query<StockItemEntity> ancestorQuery2 = ofy().load()
				.type(StockItemEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId));

		List<StockItemEntity> stockItems = new ArrayList<StockItemEntity>();

		List<StockItemTypeEntity> filteredStockItemTypes = new ArrayList<StockItemTypeEntity>();

		if (fitlerWrapper.getProductType() != null)

			ancestorQuery = ancestorQuery.filter("productType",
					fitlerWrapper.getProductType());
		filteredStockItemTypes = ancestorQuery.list();

		if (fitlerWrapper.getBrand() != null)
			ancestorQuery = ancestorQuery.filter("brand",
					fitlerWrapper.getBrand());
		filteredStockItemTypes = ancestorQuery.list();

		if (fitlerWrapper.getWarehouse() != null
				&& fitlerWrapper.getProductType() == null
				&& fitlerWrapper.getBrand() == null) {
			ancestorQuery2 = ancestorQuery2.filter("warehouse",
					fitlerWrapper.getWarehouse());
			stockItems = ancestorQuery2.list();
			return stockItems;
		}

		if (filteredStockItemTypes != null && filteredStockItemTypes.size() > 0
				&& fitlerWrapper.getWarehouse() == null) {
			stockItems = ofy().load().type(StockItemEntity.class)
					.ancestor(business)
					.filter("stockItemType IN", filteredStockItemTypes).list();
		}

		if (filteredStockItemTypes != null && filteredStockItemTypes.size() > 0
				&& fitlerWrapper.getWarehouse() != null) {
			stockItems = ofy().load().type(StockItemEntity.class)
					.ancestor(business)
					.filter("warehouse", fitlerWrapper.getWarehouse())
					.filter("stockItemType IN", filteredStockItemTypes).list();
		}
		return stockItems;
	}

	@ApiMethod(name = "filterStockItemsByCategory", path = "filterStockItemsByCategory")
	public List<StockItemEntity> filterStockItemsByCategory(
			StockItemTypeCategory category) {
		List<StockItemTypeEntity> filteredStockItemTypes = ofy().load()
				.type(StockItemTypeEntity.class)
				.ancestor(category.getBusiness())
				.filter("categoryList IN", category).list();

		List<StockItemEntity> filteredStocks = ofy().load()
				.type(StockItemEntity.class).ancestor(category.getBusiness())
				.filter("stockItemType IN", filteredStockItemTypes).list();

		return filteredStocks;
	}

	@ApiMethod(name = "filterStockItemsByCategoryAndWH", path = "filterStockItemsByCategoryAndWH")
	public List<StockItemEntity> filterStockItemsByCategoryAndWH(
			StockItemTypeFilterWrapper fitlerWrapper) {
		List<StockItemTypeEntity> filteredStockItemTypes = ofy().load()
				.type(StockItemTypeEntity.class)
				.ancestor(fitlerWrapper.getCategory().getBusiness())
				.filter("categoryList IN", fitlerWrapper.getCategory()).list();

		List<StockItemEntity> filteredStocks = ofy().load()
				.type(StockItemEntity.class)
				.ancestor(fitlerWrapper.getCategory().getBusiness())
				.filter("warehouse", fitlerWrapper.getWarehouse())
				.filter("stockItemType IN", filteredStockItemTypes).list();

		return filteredStocks;
	}

	@ApiMethod(name = "filterStockItemsByCategoryForProduct", path = "filterStockItemsByCategoryForProduct")
	public List<StockItemTypeEntity> filterStockItemsByCategoryForproduct(
			StockItemTypeCategory category) {

		List<StockItemTypeEntity> filteredStocks = ofy().load()
				.type(StockItemTypeEntity.class)
				.ancestor(category.getBusiness())
				.filter("categoryList", category).list();
		System.out.println("filteredStocks.size" + filteredStocks.size());
		return filteredStocks;
	}

	@ApiMethod(name = "filterStockItemsByCategories", path = "filterStockItemsByCategories")
	public List<StockItemEntity> filterStockItemsByCategories(
			StockItemTypeFilterWrapper fitlerWrapper) {
		if (fitlerWrapper == null)
			return new ArrayList<StockItemEntity>();

		List<StockItemTypeCategory> categoryList = fitlerWrapper
				.getCategoryList();

		if (categoryList == null || categoryList.isEmpty())
			return new ArrayList<StockItemEntity>();

		List<StockItemTypeEntity> allTypesList = new ArrayList<StockItemTypeEntity>();

		for (StockItemTypeCategory category : categoryList) {
			List<StockItemTypeEntity> filteredStockItemTypes = ofy().load()
					.type(StockItemTypeEntity.class)
					.ancestor(category.getBusiness())
					.filter("categoryList IN", category).list();

			if (filteredStockItemTypes != null
					&& !filteredStockItemTypes.isEmpty()) {
				allTypesList.addAll(filteredStockItemTypes);
			}
		}

		List<StockItemEntity> filteredStocks = ofy().load()
				.type(StockItemEntity.class)
				.ancestor(categoryList.get(0).getBusiness())
				.filter("stockItemType IN", allTypesList).list();

		return filteredStocks;
	}

	@ApiMethod(name = "getAllStockItems", path = "getAllStockItems")
	public List<StockItemEntity> getAllStockItems(@Named("busId") Long busId) {
		// System.out.println("getAllStock#busId:" + busId);
		List<StockItemEntity> filteredStocks = ofy().load()
				.type(StockItemEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return filteredStocks;
	}

	@ApiMethod(name = "getStockReceiptList", path = "getStockReceiptList")
	public List<StockItemsReceiptEntity> getStockReceiptList(
			@Named("busId") Long busId) {
		// System.out.println("getAllStock#busId:" + busId);
		List<StockItemsReceiptEntity> stockItemsShipmentList = ofy().load()
				.type(StockItemsReceiptEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return stockItemsShipmentList;
	}

	@ApiMethod(name = "getStarredStockReceipts", path = "getStarredStockReceipts")
	public List<StockItemsReceiptEntity> getStarredStockReceipts(
			@Named("busId") Long busId) {
		List<StockItemsReceiptEntity> starredStockReceipts = ofy().load()
				.type(StockItemsReceiptEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("starred", true).list();
		return starredStockReceipts;
	}

	@ApiMethod(name = "getStockReceiptByID", path = "getStockReceiptByID")
	public StockItemsReceiptEntity getStockReceiptByID(
			@Named("busId") Long busId, @Named("id") Long stRecptID) {

		List<StockItemsReceiptEntity> list = ofy()
				.load()
				.type(StockItemsReceiptEntity.class)
				.filterKey(
						Key.create(Key.create(BusinessEntity.class, busId),
								StockItemsReceiptEntity.class, stRecptID))
				.list();

		StockItemsReceiptEntity foundStockReceipt = list.size() > 0 ? list
				.get(0) : null;
		return foundStockReceipt;

	}

	@ApiMethod(name = "getStockReceiptByItemNumber", path = "getStockReceiptByItemNumber")
	public StockItemsReceiptEntity getStockReceiptByItemNumber(
			@Named("itemNumber") int itemNumber) {
		Object entityByItemNumber = super.getEntityByItemNumber(
				StockItemsReceiptEntity.class, itemNumber);
		if (entityByItemNumber != null)
			return (StockItemsReceiptEntity) entityByItemNumber;
		else
			return null;
	}

	@ApiMethod(name = "getStockShipmentList", path = "getStockShipmentList")
	public List<StockItemsShipmentEntity> getStockShipmentList(
			@Named("busId") Long busId) {
		// System.out.println("getAllStock#busId:" + busId);
		List<StockItemsShipmentEntity> stockItemsShipmentList = ofy().load()
				.type(StockItemsShipmentEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return stockItemsShipmentList;
	}

	@ApiMethod(name = "getStarredStockShipments", path = "getStarredStockShipments")
	public List<StockItemsShipmentEntity> getStarredStockShipments(
			@Named("busId") Long busId) {
		List<StockItemsShipmentEntity> starredStockShipments = ofy().load()
				.type(StockItemsShipmentEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("starred", true).list();
		return starredStockShipments;
	}

	@ApiMethod(name = "getStockShipmentByID", path = "getStockShipmentByID")
	public StockItemsShipmentEntity getStockShipmentByID(
			@Named("busId") Long busId, @Named("id") Long stShipId) {

		List<StockItemsShipmentEntity> list = ofy()
				.load()
				.type(StockItemsShipmentEntity.class)
				.filterKey(
						Key.create(Key.create(BusinessEntity.class, busId),
								StockItemsShipmentEntity.class, stShipId))
				.list();

		StockItemsShipmentEntity foundStockReceipt = list.size() > 0 ? list
				.get(0) : null;
		return foundStockReceipt;

	}

	@ApiMethod(name = "getStockShipmentByItemNumber", path = "getStockShipmentByItemNumber")
	public StockItemsShipmentEntity getStockShipmentByItemNumber(
			@Named("itemNumber") int itemNumber) {
		Object entityByItemNumber = super.getEntityByItemNumber(
				StockItemsShipmentEntity.class, itemNumber);
		if (entityByItemNumber != null)
			return (StockItemsShipmentEntity) entityByItemNumber;
		else
			return null;
	}

	@ApiMethod(name = "getStockItemInstancesList", path = "getStockItemInstancesList", httpMethod = HttpMethod.POST)
	public List<StockItemInstanceEntity> getStockItemInstancesList(
			StockItemEntity stockItem) {
		// System.out.println("getAllStock#busId:" + busId);
		List<StockItemInstanceEntity> list = ofy()
				.load()
				.type(StockItemInstanceEntity.class)
				.ancestor(
						Key.create(BusinessEntity.class, stockItem
								.getBusiness().getId()))
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
	public List<StockItemEntity> getReportByThreshold(
			@Named("busId") Long bussinessId) {
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

	public static void adjustStockItems(BusinessEntity business,
			List<StockLineItem> productLineItemList, int shipmentItemNumber,
			int invoiceItemNumber) {
		/* For Reduce the Stock Quantity */
		List<StockItemTxnEntity> stockItemTxnList = new ArrayList<StockItemTxnEntity>();
		List<StockItemInstanceEntity> stockItemInstanceToUpdateList = new ArrayList<StockItemInstanceEntity>();

		for (StockLineItem invoiceLineItem : productLineItemList) {
			int qtyDiff = invoiceLineItem.getQty()
					- invoiceLineItem.getStockMaintainedQty();
			if (invoiceLineItem.getStockItem().isMaintainStock()
					&& qtyDiff != 0) {

				StockItemTxnEntity txnEntity = new StockItemTxnEntity();
				txnEntity.setBusiness(business);
				txnEntity.setStockItem(invoiceLineItem.getStockItem());
				txnEntity.setQty(Math.abs(qtyDiff));
				txnEntity.setStockShipmentNumber(shipmentItemNumber);
				txnEntity.setInvoiceNumber(invoiceItemNumber);
				if (qtyDiff > 0) {
					txnEntity.setTxnType(StockItemTxnEntity.StockTxnType.DR);
					txnEntity.setPrice(invoiceLineItem.getPrice());
				} else {
					txnEntity.setTxnType(StockItemTxnEntity.StockTxnType.CR);
					txnEntity.setPrice(invoiceLineItem.getCost());
				}
				invoiceLineItem.setStockMaintainedQty(Math.abs(invoiceLineItem
						.getQty()));
				stockItemTxnList.add(txnEntity);
				if (invoiceLineItem.getStockItem().getStockItemType()
						.isMaintainStockBySerialNumber()
						&& invoiceLineItem.getStockItemInstanceList() != null) {
					stockItemInstanceToUpdateList.addAll(invoiceLineItem
							.getStockItemInstanceList());
				}
			}
		}
		if (stockItemTxnList.size() > 0) {
			addStockItemTxnList(stockItemTxnList, stockItemInstanceToUpdateList);
		}
	}

	private static void addStockItemTxnList(
			List<StockItemTxnEntity> stockItemTxnList,
			List<StockItemInstanceEntity> stockItemInstanceToUpdateList) {
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
		if (stockItemInstanceToUpdateList.size() > 0)
			ofy().save().entities(stockItemInstanceToUpdateList);
	}

	@ApiMethod(name = "getStockItemTxnList", path = "getStockItemTxnList")
	public List<StockItemTxnEntity> getStockItemTxnList(
			@Named("busId") Long busId, @Named("fromDate") Date fromDate,
			@Named("txnType") String txnType) {

		Query<StockItemTxnEntity> filterQuery = ofy().load()
				.type(StockItemTxnEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("createdDate >=", fromDate);

		if (txnType != null && !txnType.trim().isEmpty())
			filterQuery = filterQuery.filter("txnType", txnType);

		return filterQuery.list();
	}

	@ApiMethod(name = "getCRStockTxnByStockItem", path = "getCRStockTxnByStockItem", httpMethod = HttpMethod.POST)
	public List<StockItemTxnEntity> getCRStockTxnByStockItem(
			StockItemEntity stockItem, @Named("busId") long busId,
			@Named("fromDate") long fromDate, @Named("toDate") long toDate) {

		Date date1 = new Date(fromDate);
		Date date2 = new Date(toDate);

		Date beginningDate = EntityUtil.getBeginingOfDay(date1);
		Date endDate = EntityUtil.getEndOfDay(date2);

		List<StockItemTxnEntity> stockTxns = ofy().load()
				.type(StockItemTxnEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("stockItem", stockItem)
				.filter("txnType ==", StockTxnType.CR)
				.filter("createdDate >=", beginningDate)
				.filter("createdDate <=", endDate).list();

		return stockTxns;
	}

	@ApiMethod(name = "getDRStockTxnByStockItem", path = "getDRStockTxnByStockItem", httpMethod = HttpMethod.POST)
	public List<StockItemTxnEntity> getDRStockTxnByStockItem(
			StockItemEntity stockItem, @Named("busId") long busId,
			@Named("fromDate") long fromDate, @Named("toDate") long toDate) {

		Date date1 = new Date(fromDate);
		Date date2 = new Date(toDate);

		Date beginningDate = EntityUtil.getBeginingOfDay(date1);
		Date endDate = EntityUtil.getEndOfDay(date2);

		List<StockItemTxnEntity> stockTxns = ofy()
				.load()
				.type(StockItemTxnEntity.class)
				.ancestor(
						Key.create(BusinessEntity.class, stockItem
								.getBusiness().getId()))
				.filter("stockItem", stockItem)
				.filter("txnType ==", StockTxnType.DR)
				.filter("createdDate >=", beginningDate)
				.filter("createdDate <=", endDate).list();

		return stockTxns;
	}

	@ApiMethod(name = "addSupplier", path = "addSupplier")
	public SupplierEntity addSupplier(SupplierEntity supplierEntity) {
		ofy().save().entity(supplierEntity).now();
		return supplierEntity;
	}

	@ApiMethod(name = "getAllSuppliersByBusiness", path = "getAllSuppliersByBusiness")
	public List<SupplierEntity> getAllSuppliersByBusiness(
			@Named("busId") Long busId) {

		List<SupplierEntity> filteredSuppliers = ofy()
				.load()
				.type(SupplierEntity.class)
				.filter("business",
						Ref.create(Key.create(BusinessEntity.class, busId)))
				.list();

		return filteredSuppliers;
	}

	@ApiMethod(name = "addPurchaseOrder", path = "addPurchaseOrder")
	public PurchaseOrderEntity addPurchaseOrder(
			PurchaseOrderEntity purchaseOrderEntity) throws MessagingException,
			IOException {

		if (purchaseOrderEntity.getStatus() == DocumentStatus.FINALIZED
				&& purchaseOrderEntity.isStatusAlreadyFinalized()) {
			throw new RuntimeException(
					"Save not allowed. Purchase Order is already FINALIZED: "
							+ this.getClass().getSimpleName()
							+ " Finalized entity can't be altered.");
		}
		List<StockLineItem> productLineItemList = purchaseOrderEntity
				.getProductLineItemList();

		for (StockLineItem stockLineItem : productLineItemList) {
			StockItemEntity stockItem = getOrCreateWarehouseStockItem(
					purchaseOrderEntity.getWarehouse(), stockLineItem);
			stockLineItem.setStockItem(stockItem);
		}

		ofy().save().entity(purchaseOrderEntity).now();
		if (purchaseOrderEntity.getStatus() == DocumentStatus.FINALIZED
				&& purchaseOrderEntity.getBudget() != null
				&& purchaseOrderEntity.getBudgetLineItem() != null) {
			// Reduce Budget Balance
			BudgetEntity budget = purchaseOrderEntity.getBudget();
			List<LineItemCategory> categoryList = budget.getCategoryList();
			for (LineItemCategory lineItemCat : categoryList) {
				if (lineItemCat.getCategoryName().equalsIgnoreCase(
						purchaseOrderEntity.getBudgetLineItemCategory()
								.getCategoryName().trim())) {
					List<LineItemEntity> items = lineItemCat.getItems();
					for (LineItemEntity lineItemEntity : items) {
						if (lineItemEntity.getItemName().equalsIgnoreCase(
								purchaseOrderEntity.getBudgetLineItem()
										.getItemName().trim())) {
							lineItemEntity
									.setCurrentBudgetBalance(lineItemEntity
											.getCurrentBudgetBalance()
											- purchaseOrderEntity
													.getFinalTotal());
							// save async
							ofy().save().entity(budget);
							break;
						}
					}
				}
			}

		}
		if (purchaseOrderEntity.getStatus() != DocumentStatus.DRAFT) {
			new EmailHandler().sendPurchaseOrderEmail(purchaseOrderEntity);
		}
		return purchaseOrderEntity;
	}

	@ApiMethod(name = "getStarredPurchaseOrders", path = "getStarredPurchaseOrders")
	public List<PurchaseOrderEntity> getStarredPurchaseOrders(
			@Named("busId") Long busId) {
		List<PurchaseOrderEntity> starredPOs = ofy().load()
				.type(PurchaseOrderEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("starred", true).list();
		return starredPOs;
	}

	@ApiMethod(name = "addRequisition", path = "addRequisition")
	public RequisitionEntity addRequisition(RequisitionEntity requisitionEntity) {

		if (requisitionEntity.getStatus() == DocumentStatus.FINALIZED
				&& requisitionEntity.isStatusAlreadyFinalized()) {
			throw new RuntimeException(
					"Save not allowed. Requisition entity is already FINALIZED: "
							+ this.getClass().getSimpleName()
							+ " Finalized entity can't be altered.");
		}
		ofy().save().entity(requisitionEntity).now();
		if (requisitionEntity.getStatus() != DocumentStatus.DRAFT) {
			new EmailHandler().sendRequisitionEmail(requisitionEntity);
		}
		return requisitionEntity;

	}

	@ApiMethod(name = "getStarredRequisitions", path = "getStarredRequisitions")
	public List<RequisitionEntity> getStarredRequisitions(
			@Named("busId") Long busId) {
		List<RequisitionEntity> starredRequisitions = ofy().load()
				.type(RequisitionEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("starred", true).list();
		return starredRequisitions;
	}

	@ApiMethod(name = "fetchRequisitionListByPaging", path = "fetchRequisitionListByPaging")
	public EntityPagingInfo fetchRequisitionListByPaging(
			@Named("id") Long busId, @Named("status") String status,
			EntityPagingInfo pagingInfo) {
		if (status != null && !status.isEmpty()) {
			DocumentStatus statusType = DocumentStatus.valueOf(status
					.toUpperCase(Locale.ENGLISH));
			return super.fetchEntityListByPaging(busId,
					RequisitionEntity.class, pagingInfo, statusType);
		} else {
			return super.fetchEntityListByPaging(busId,
					RequisitionEntity.class, pagingInfo);
		}
	}

	@ApiMethod(name = "addBudget", path = "addBudget")
	public BudgetEntity addBudget(BudgetEntity budgetEntity) {
		if (budgetEntity.getStatus() == DocumentStatus.FINALIZED) {
			throw new RuntimeException(
					"Save not allowed. Budget entity has already been finalized."
							+ this.getClass().getSimpleName()
							+ "Finalized entity can't be altered.");
		}

		ofy().save().entity(budgetEntity).now();
		return budgetEntity;
	}

	@ApiMethod(name = "fetchBudgetListByPaging", path = "fetchBudgetListByPaging")
	public EntityPagingInfo fetchBudgetListByPaging(@Named("id") Long busId,
			@Named("status") String status, EntityPagingInfo pagingInfo) {
		if (status != null && !status.isEmpty()) {
			DocumentStatus statusType = DocumentStatus.valueOf(status
					.toUpperCase(Locale.ENGLISH));
			return super.fetchEntityListByPaging(busId, BudgetEntity.class,
					pagingInfo, statusType);
		} else {
			return super.fetchEntityListByPaging(busId, BudgetEntity.class,
					pagingInfo);
		}
	}

	@ApiMethod(name = "getAllPurchaseOrder", path = "getAllPurchaseOrder")
	public List<PurchaseOrderEntity> getAllPurchaseOrder(@Named("id") Long busId) {
		List<PurchaseOrderEntity> filteredPO = ofy().load()
				.type(PurchaseOrderEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredPO;
	}

	@ApiMethod(name = "fetchPOListByPaging", path = "fetchPOListByPaging")
	public EntityPagingInfo fetchPOListByPaging(@Named("id") Long busId,
			@Named("status") String status, EntityPagingInfo pagingInfo) {
		if (status != null && !status.isEmpty()) {
			DocumentStatus statusType = DocumentStatus.valueOf(status
					.toUpperCase(Locale.ENGLISH));
			return super.fetchEntityListByPaging(busId,
					PurchaseOrderEntity.class, pagingInfo, statusType);
		} else {
			return super.fetchEntityListByPaging(busId,
					PurchaseOrderEntity.class, pagingInfo);
		}
	}

	@ApiMethod(name = "fetchReceiptListByPaging", path = "fetchReceiptListByPaging")
	public EntityPagingInfo fetchReceiptListByPaging(@Named("id") Long busId,
			@Named("status") String status, EntityPagingInfo pagingInfo) {
		if (status != null && !status.isEmpty()) {
			DocumentStatus statusType = DocumentStatus.valueOf(status
					.toUpperCase(Locale.ENGLISH));
			return super.fetchEntityListByPaging(busId,
					StockItemsReceiptEntity.class, pagingInfo, statusType);
		} else {
			return super.fetchEntityListByPaging(busId,
					StockItemsReceiptEntity.class, pagingInfo);
		}
	}

	@ApiMethod(name = "fetchShipmentListByPaging", path = "fetchShipmentListByPaging")
	public EntityPagingInfo fetchShipmentListByPaging(@Named("id") Long busId,
			@Named("status") String status, EntityPagingInfo pagingInfo) {
		if (status != null && !status.isEmpty()) {
			DocumentStatus statusType = DocumentStatus.valueOf(status
					.toUpperCase(Locale.ENGLISH));
			return super.fetchEntityListByPaging(busId,
					StockItemsShipmentEntity.class, pagingInfo, statusType);
		} else {
			return super.fetchEntityListByPaging(busId,
					StockItemsShipmentEntity.class, pagingInfo);
		}
	}

	@ApiMethod(name = "getPOByItemNumber", path = "getPOByItemNumber")
	public PurchaseOrderEntity getPOByItemNumber(
			@Named("itemNumber") int itemNumber) {
		Object entityByItemNumber = super.getEntityByItemNumber(
				PurchaseOrderEntity.class, itemNumber);
		if (entityByItemNumber != null)
			return (PurchaseOrderEntity) entityByItemNumber;
		else
			return null;
	}

	@ApiMethod(name = "getRequisitionByID", path = "getRequisitionByID")
	public RequisitionEntity getRequisitionByID(@Named("busId") Long busId,
			@Named("id") Long id) {
		RequisitionEntity now = ofy()
				.load()
				.key(Key.create(Key.create(BusinessEntity.class, busId),
						RequisitionEntity.class, id)).now();
		if (now == null)
			throw new RuntimeException(
					"RequisitionEntity with not found with id:" + id);
		else
			return now;
	}

	@ApiMethod(name = "getRequisitionByItemNumber", path = "getRequisitionByItemNumber")
	public RequisitionEntity getRequisitionByItemNumber(
			@Named("itemNumber") int itemNumber) {
		Object entityByItemNumber = super.getEntityByItemNumber(
				RequisitionEntity.class, itemNumber);
		if (entityByItemNumber != null)
			return (RequisitionEntity) entityByItemNumber;
		else
			return null;
	}

	@ApiMethod(name = "getPOByID", path = "getPOByID")
	public PurchaseOrderEntity getPOByID(@Named("busId") Long busId,
			@Named("id") Long poId) {
		PurchaseOrderEntity now = ofy()
				.load()
				.key(Key.create(Key.create(BusinessEntity.class, busId),
						PurchaseOrderEntity.class, poId)).now();
		if (now == null)
			throw new RuntimeException(
					"PurchaseOrderEntity with not found with id:" + poId);
		else
			return now;
	}

	@ApiMethod(name = "addStockSettings", path = "addStockSettings")
	public StockSettingsEntity addStockSettings(
			StockSettingsEntity settingsEntity) {
		ofy().save().entity(settingsEntity).now();
		return settingsEntity;

	}

	@ApiMethod(name = "getStockSettingsByBiz", path = "getStockSettingsByBiz")
	public StockSettingsEntity getStockSettingsByBiz(@Named("id") Long busId) {
		StockSettingsEntity stockSett = ofy().load()
				.type(StockSettingsEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).first()
				.now();
		return stockSett;

	}

}// end of StockServices
