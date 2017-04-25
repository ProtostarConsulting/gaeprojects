package com.protostar.billingnstock.production.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.production.entities.BomEntity;
import com.protostar.billingnstock.production.entities.MachineQCUnitMeasure;
import com.protostar.billingnstock.production.entities.ProductionMachineEntity;
import com.protostar.billingnstock.production.entities.ProductionPlanEntity;
import com.protostar.billingnstock.production.entities.ProductionRequisitionEntity;
import com.protostar.billingnstock.production.entities.ProductionSetting;
import com.protostar.billingnstock.production.entities.ProductionShipmentEntity;
import com.protostar.billingnstock.production.entities.QCMachineDailyRecordEntity;
import com.protostar.billingnstock.production.entities.QCMachineEntity;
import com.protostar.billingnstock.stock.entities.BomLineItemCategory;
import com.protostar.billingnstock.stock.services.StockManagementService;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "productionService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.production.services", ownerName = "com.protostar.billingnstock.production.services", packagePath = ""))
public class ProductionService {

	@ApiMethod(name = "addBomEntity", path = "addBomEntity")
	public BomEntity addBomEntity(BomEntity bomEntity) {
		ofy().save().entity(bomEntity).now();
		return bomEntity;
	}

	@ApiMethod(name = "getEmptyProductionRequisition", path = "getEmptyProductionRequisition")
	public ProductionRequisitionEntity getEmptyProductionRequisition(BomEntity bom) {

		ProductionRequisitionEntity prodReq = new ProductionRequisitionEntity();

		prodReq.setBomEntity(bom);
		prodReq.setProductQty(1);
		prodReq.setDeliveryDateTime(new Date());

		List<BomLineItemCategory> bomCatList = bom.getCatList();
		prodReq.setCatList(bomCatList);

		return prodReq;
	}

	@ApiMethod(name = "addRequisition", path = "addRequisition")
	public ProductionRequisitionEntity addRequisition(ProductionRequisitionEntity requisitionEntity) {
		ofy().save().entity(requisitionEntity).now();
		return requisitionEntity;
	}

	@ApiMethod(name = "addProductionShipmentEntity", path = "addProductionShipmentEntity")
	public ProductionShipmentEntity addProductionShipmentEntity(ProductionShipmentEntity documentEntity) {
		StockManagementService stockManagementService = new StockManagementService();
		return stockManagementService.addProductionShipmentEntity(documentEntity);
	}

	@ApiMethod(name = "listBomEntity", path = "listBomEntity")
	public List<BomEntity> listBomEntity(@Named("busId") Long busId) {

		List<BomEntity> bomEntityList = ofy().load().type(BomEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return bomEntityList;

	}

	@ApiMethod(name = "addProdPlanEntity", path = "addProdPlanEntity")
	public ProductionPlanEntity addProdPlanEntity(ProductionPlanEntity prodPlanEntity) {
		ofy().save().entity(prodPlanEntity).now();
		return prodPlanEntity;
	}

	@ApiMethod(name = "getProductionPlanList", path = "getProductionPlanList")
	public List<ProductionPlanEntity> getProductionPlanList(@Named("busId") Long busId) {
		List<ProductionPlanEntity> planList = ofy().load().type(ProductionPlanEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return planList;
	}

	@ApiMethod(name = "listBomEntityByID", path = "listBomEntityByID")
	public BomEntity listBomEntityByID(@Named("busId") Long busId, @Named("id") Long bomId) {
		BomEntity bomEntity = ofy().load()
				.key(Key.create(Key.create(BusinessEntity.class, busId), BomEntity.class, bomId)).now();
		return bomEntity;
	}

	@ApiMethod(name = "addMachine", path = "addMachine")
	public ProductionMachineEntity addProductionMachine(ProductionMachineEntity machine) {
		ofy().save().entity(machine).now();
		return machine;
	}

	@ApiMethod(name = "getMachineList", path = "getMachineList")
	public List<ProductionMachineEntity> getProductionMachineList(@Named("busId") Long busId) {
		List<ProductionMachineEntity> machineList = ofy().load().type(ProductionMachineEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return machineList;
	}

	@ApiMethod(name = "addQCMachine", path = "addQCMachine")
	public QCMachineEntity addQCMachine(QCMachineEntity machine) {
		ofy().save().entity(machine).now();
		return machine;
	}

	@ApiMethod(name = "addQCMachineRecord", path = "addQCMachineRecord")
	public QCMachineDailyRecordEntity addQCMachineRecord(QCMachineDailyRecordEntity qcMachineRecord) {
		ofy().save().entity(qcMachineRecord).now();
		return qcMachineRecord;
	}

	@ApiMethod(name = "getQCMachineList", path = "getQCMachineList")
	public List<QCMachineEntity> getQCMachineList(@Named("busId") Long busId) {
		List<QCMachineEntity> machineList = ofy().load().type(QCMachineEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return machineList;
	}

	@ApiMethod(name = "getQCMachineById", path = "getQCMachineById")
	public QCMachineEntity getQCMachineById(@Named("busId") Long busId, @Named("id") Long id) {

		List<QCMachineEntity> list = ofy().load().type(QCMachineEntity.class)
				.filterKey(Key.create(Key.create(BusinessEntity.class, busId), QCMachineEntity.class, id)).list();
		QCMachineEntity foundMachine = list.size() > 0 ? list.get(0) : null;
		return foundMachine;
	}

	@ApiMethod(name = "getQCMachineDailyRecordEntity", path = "getQCMachineDailyRecordEntity")
	public QCMachineDailyRecordEntity getQCMachineDailyRecordEntity(@Named("qcmachineId") Long qcmachineId,
			@Named("busId") Long busId, @Named("date") Long recordDate) {

		QCMachineEntity machineQc = getQCMachineById(busId, qcmachineId);
		Date tempRecordDateObj = new Date(recordDate);
		List<QCMachineDailyRecordEntity> foundedDailyRecord = ofy().load().type(QCMachineDailyRecordEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).filter("recordDate", tempRecordDateObj)
				.filter("machineQc", machineQc).list();

		QCMachineDailyRecordEntity qcMachineDailyRecord = null;

		if (foundedDailyRecord.size() > 0) {
			qcMachineDailyRecord = foundedDailyRecord.get(0);
		} else {
			ProductionUtil prodUtill = new ProductionUtil();
			qcMachineDailyRecord = prodUtill.createNewQCMachineDailyRecordEntity(machineQc, tempRecordDateObj);
		}

		return qcMachineDailyRecord;
	}

	@ApiMethod(name = "getQCMachineRecordList", path = "getQCMachineRecordList")
	public List<QCMachineDailyRecordEntity> getQCMachineRecordList(@Named("busId") Long busId) {
		List<QCMachineDailyRecordEntity> qcMachineRecordList = ofy().load().type(QCMachineDailyRecordEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return qcMachineRecordList;
	}

	@ApiMethod(name = "getQCMachineRecordById", path = "getQCMachineRecordById")
	public QCMachineDailyRecordEntity getQCMachineRecordById(@Named("busId") Long busId, @Named("id") Long id) {

		List<QCMachineDailyRecordEntity> list = ofy().load().type(QCMachineDailyRecordEntity.class)
				.filterKey(Key.create(Key.create(BusinessEntity.class, busId), QCMachineDailyRecordEntity.class, id))
				.list();
		QCMachineDailyRecordEntity foundMachine = list.size() > 0 ? list.get(0) : null;
		return foundMachine;
	}

	@ApiMethod(name = "addQCMachineUnitMeasure", path = "addQCMachineUnitMeasure")
	public MachineQCUnitMeasure addQCMachineUnitMeasure(MachineQCUnitMeasure qcMachineUnit) {
		ofy().save().entity(qcMachineUnit).now();
		return qcMachineUnit;
	}

	@ApiMethod(name = "getMachineQCUnitsMeasureList", path = "getMachineQCUnitsMeasureList")
	public List<MachineQCUnitMeasure> getMachineQCUnitsMeasureList(@Named("busId") Long busId) {

		List<MachineQCUnitMeasure> qcMachineUnits = ofy().load().type(MachineQCUnitMeasure.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return qcMachineUnits;
	}

	@ApiMethod(name = "addProductionSettings", path = "addProductionSettings")
	public ProductionSetting addProductionSettings(ProductionSetting settingsEntity) {
		ofy().save().entity(settingsEntity).now();
		return settingsEntity;

	}

	@ApiMethod(name = "getProductionSettingsByBiz", path = "getProductionSettingsByBiz")
	public ProductionSetting getProductionSettingsByBiz(@Named("busId") Long busId) {
		ProductionSetting prodSett = ofy().load().type(ProductionSetting.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).first().now();
		return prodSett;

	}

	@ApiMethod(name = "listProductionRequisitionEntity", path = "listProductionRequisitionEntity")
	public List<ProductionRequisitionEntity> listProductionRequisitionEntity(@Named("busId") Long busId) {

		List<ProductionRequisitionEntity> productionRequisitionEntityList = ofy().load()
				.type(ProductionRequisitionEntity.class).ancestor(Key.create(BusinessEntity.class, busId)).list();
		return productionRequisitionEntityList;

	}

	@ApiMethod(name = "getRequisitionEntityByID", path = "getRequisitionEntityByID")
	public ProductionRequisitionEntity getRequisitionEntityByID(@Named("busId") Long busId,
			@Named("proId") Long proId) {

		ProductionRequisitionEntity productionRequisitionEntity = ofy().load()
				.key(Key.create(Key.create(BusinessEntity.class, busId), ProductionRequisitionEntity.class, proId))
				.now();

		return productionRequisitionEntity;
	}

}
