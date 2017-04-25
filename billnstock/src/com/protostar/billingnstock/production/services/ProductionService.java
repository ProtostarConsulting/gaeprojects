package com.protostar.billingnstock.production.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.production.entities.BomEntity;
import com.protostar.billingnstock.production.entities.ProductionMachineEntity;
import com.protostar.billingnstock.production.entities.ProductionRequisitionEntity;
import com.protostar.billingnstock.production.entities.QCMachineDailyRecordEntity;
import com.protostar.billingnstock.production.entities.QCMachineEntity;
import com.protostar.billingnstock.stock.entities.BomLineItemCategory;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "productionService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.production.services", ownerName = "com.protostar.billingnstock.production.services", packagePath = ""))
public class ProductionService {

	@ApiMethod(name = "addBomEntity", path = "addBomEntity")
	public void addBomEntity(BomEntity addProduction) {

		ofy().save().entity(addProduction).now();
		System.out.println("saved");

	}

	@ApiMethod(name = "getEmptyProductionRequisition", path = "getEmptyProductionRequisition")
	public ProductionRequisitionEntity getEmptyProductionRequisition(
			BomEntity bom) {

		ProductionRequisitionEntity productionRequisitionEntity = new ProductionRequisitionEntity();

		productionRequisitionEntity.setBomEntity(bom);
		productionRequisitionEntity.setProductQty(1);

		List<BomLineItemCategory> bomCatList = bom.getCatList();
		productionRequisitionEntity.setCatList(bomCatList);

		return productionRequisitionEntity;
	}

	@ApiMethod(name = "addRequisition", path = "addRequisition")
	public void addRequisition(ProductionRequisitionEntity addRequisition) {

		ofy().save().entity(addRequisition).now();
		System.out.println("saved");

	}

	@ApiMethod(name = "listBomEntity", path = "listBomEntity")
	public List<BomEntity> listProduction(@Named("bid") Long busId) {

		List<BomEntity> bomEntityList = ofy().load().type(BomEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return bomEntityList;

	}

	
	
	@ApiMethod(name = "listProductionRequisitionEntity", path = "listProductionRequisitionEntity")
	public List<ProductionRequisitionEntity> listProductionRequisitionEntity(@Named("bid") Long busId) {

		List<ProductionRequisitionEntity>productionRequisitionEntityList = ofy().load().type(ProductionRequisitionEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return productionRequisitionEntityList;

	}

	
	@ApiMethod(name = "getRequisitionEntityByID", path = "getRequisitionEntityByID")
	public ProductionRequisitionEntity getRequisitionEntityByID(@Named("bid") Long busId,
			@Named("proId") Long proId) {
	
		ProductionRequisitionEntity productionRequisitionEntity = 	ofy()
				.load()
				.key(Key.create(Key.create(BusinessEntity.class, busId),
						ProductionRequisitionEntity.class, proId)).now();
	
		return productionRequisitionEntity;
	}
	
	@ApiMethod(name = "listBomEntityByID", path = "listBomEntityByID")
	public BomEntity listBomEntityByID(@Named("bid") Long busId,
			@Named("id") Long bomId) {

		BomEntity bomEntity = ofy()
				.load()
				.key(Key.create(Key.create(BusinessEntity.class, busId),
						BomEntity.class, bomId)).now();
		
		
		return bomEntity;
	}

	@ApiMethod(name = "addMachine", path = "addMachine")
	public ProductionMachineEntity addMachine(ProductionMachineEntity machine) {
		ofy().save().entity(machine).now();
		return machine;
	}

	@ApiMethod(name = "getMachineList", path = "getMachineList")
	public List<ProductionMachineEntity> getMachineList(
			@Named("busId") Long busId) {
		List<ProductionMachineEntity> machineList = ofy().load()
				.type(ProductionMachineEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return machineList;
	}

	@ApiMethod(name = "addQCMachine", path = "addQCMachine")
	public QCMachineEntity addQCMachine(QCMachineEntity machine) {
		ofy().save().entity(machine).now();
		return machine;
	}

	@ApiMethod(name = "addQCMachineRecord", path = "addQCMachineRecord")
	public QCMachineDailyRecordEntity addQCMachineRecord(
			QCMachineDailyRecordEntity qcMachineRecord) {
		ofy().save().entity(qcMachineRecord).now();
		return qcMachineRecord;
	}

	@ApiMethod(name = "getQCMachineList", path = "getQCMachineList")
	public List<QCMachineEntity> getQCMachineList(@Named("busId") Long busId) {
		List<QCMachineEntity> machineList = ofy().load()
				.type(QCMachineEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return machineList;
	}

	@ApiMethod(name = "getQCMachineById", path = "getQCMachineById")
	public QCMachineEntity getQCMachineById(@Named("busId") Long busId,
			@Named("id") Long id) {

		List<QCMachineEntity> list = ofy()
				.load()
				.type(QCMachineEntity.class)
				.filterKey(
						Key.create(Key.create(BusinessEntity.class, busId),
								QCMachineEntity.class, id)).list();
		QCMachineEntity foundMachine = list.size() > 0 ? list.get(0) : null;
		return foundMachine;
	}

	@ApiMethod(name = "getQCMachineDailyRecordEntity", path = "getQCMachineDailyRecordEntity")
	public QCMachineDailyRecordEntity getQCMachineDailyRecordEntity(
			@Named("qcmachineId") Long qcmachineId, @Named("busId") Long busId,
			@Named("date") Long recordDate) {

		QCMachineEntity machineQc = getQCMachineById(busId, qcmachineId);
		Date tempRecordDateObj = new Date(recordDate);

		// quer in DS by fiter

		

		List<QCMachineDailyRecordEntity> foundedDailyRecord = ofy().load()
				.type(QCMachineDailyRecordEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("recordDate", tempRecordDateObj)
				.filter("machineQc", machineQc).list();

		QCMachineDailyRecordEntity qcMachineDailyRecord = null;

		if (foundedDailyRecord.size() > 0) {
			qcMachineDailyRecord = foundedDailyRecord.get(0);
		} else {
			ProductionUtil prodUtill = new ProductionUtil();
			qcMachineDailyRecord = prodUtill
					.createNewQCMachineDailyRecordEntity(machineQc,
							tempRecordDateObj);
		}


		// if(found) return
		// else temp v = ProductionUtil.createNewQCMachineDailyRecordEntity()



		return qcMachineDailyRecord;
	}
	
	@ApiMethod(name = "getQCMachineRecordList", path = "getQCMachineRecordList")
	public List<QCMachineDailyRecordEntity> getQCMachineRecordList(@Named("busId") Long busId) {
		List<QCMachineDailyRecordEntity> qcMachineRecordList =  ofy().load().type(QCMachineDailyRecordEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		
		return qcMachineRecordList;
	}

}
