package com.protostar.billingnstock.stock.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.protostar.billingnstock.stock.entities.StockItemEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "stockService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.stock.services", ownerName = "com.protostar.billingnstock.stock.services", packagePath = ""))
public class StockItemService {

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

		StockItemEntity stock = ofy().load().type(StockItemEntity.class).id(id)
				.now();

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

		// List<StockItemEntity> thresholdStocks =
		// ofy().load().type(StockItemEntity.class).filter("qty <="
		// thresholdValue).list();

		List<StockItemEntity> stocks = ofy().load().type(StockItemEntity.class)
				.list();
		List<StockItemEntity> filteredThresholdStocks = new ArrayList<StockItemEntity>();

		for (int i = 0; i < stocks.size(); i++) {
			if ((stocks.get(i).getBusiness().getId().equals(id))
					&& (stocks.get(i).getQty() <= stocks.get(i)
							.getThresholdValue())) {

				System.out.println("Got the record:" + stocks.get(i));
				filteredThresholdStocks.add(stocks.get(i));

			}
		}
		return filteredThresholdStocks;
	}

	@ApiMethod(name = "updateStock")
	public void updateStock(StockItemEntity stockItemEntity) {

		ofy().save().entity(stockItemEntity).now();
	}

	/*
	 * @ApiMethod(name = "addStock") public ServerMsg addStock(StockItemEntity
	 * stockItemEntity) { System.out.println("stockItemEntity:" +
	 * stockItemEntity); ServerMsg msgBean = new ServerMsg();
	 * 
	 * StockItemEntity stockItemEntity2 = new StockItemEntity();
	 * 
	 * if (stockItemEntity.getId() != null) {
	 * stockItemEntity2.setId(stockItemEntity.getId()); }
	 * 
	 * stockItemEntity2.setItemName(stockItemEntity.getItemName());
	 * stockItemEntity2.setCategory(stockItemEntity.getCategory());
	 * stockItemEntity2.setItemId(stockItemEntity.getItemId());
	 * stockItemEntity2.setQty(stockItemEntity.getQty());
	 * stockItemEntity2.setPrice(stockItemEntity.getPrice());
	 * stockItemEntity2.setThreshold_value(stockItemEntity
	 * .getThreshold_value());
	 * 
	 * EntityManager em = null;
	 * 
	 * try { em = EMF.get().createEntityManager(); em.persist(stockItemEntity);
	 * msgBean.setMsg("Stock Records Added successfully" + " " +
	 * stockItemEntity.getItemName()); } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } finally { em.close(); }
	 * 
	 * return msgBean;
	 * 
	 * }// end of addStockServices
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @ApiMethod(name = "getAllStock") public List<StockItemEntity>
	 * getAllStock() { System.out.println("In side getAllStock ");
	 * List<StockItemEntity> stockList = new ArrayList<StockItemEntity>();
	 * EntityManager em = null; try {
	 * 
	 * em = EMF.get().createEntityManager();
	 * 
	 * Query q = em.createQuery("select s from StockItemEntity s"); stockList =
	 * q.getResultList(); System.out.println("Got AllList: " +
	 * stockList.size());
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } finally { em.close(); }
	 * 
	 * return stockList;
	 * 
	 * }// end of getAllStockServices
	 * 
	 * @ApiMethod(name = "getStockByID") public StockItemEntity
	 * getStockByID(@Named("id") Long id) { StockItemEntity stockItemEntity =
	 * null; EntityManager em = null;
	 * 
	 * try { em = EMF.get().createEntityManager(); Query query = em
	 * .createNamedQuery("selct s from StockEntity s where s.id=" + id);
	 * List<StockItemEntity> resultList = query.getResultList(); if
	 * (resultList.size() > 0) { StockItemEntity customer = resultList.get(0);
	 * // stockItemEntity = CustomerInfoUtil.toCustomerInfo(customer); } } catch
	 * (Exception ex) { ex.getStackTrace(); } finally {
	 * 
	 * } return null;
	 * 
	 * }
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @ApiMethod(name="reportByThreshold") public List<StockItemEntity>
	 * reportByThreshold() { System.out.println("In side reportByThreshold " );
	 * List<StockItemEntity> stockList= new ArrayList<StockItemEntity>();
	 * EntityManager em= null; try {
	 * 
	 * em = EMF.get().createEntityManager();
	 * 
	 * Query q=
	 * em.createQuery("select s from StockItemEntity where threshold = totalstock "
	 * ); stockList=q.getResultList(); System.out.println("Got AllList: " +
	 * stockList.size() );
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } finally { em.close(); }
	 * 
	 * return stockList;
	 * 
	 * }//end of getAllStockServices
	 */
}// end of StockServices
