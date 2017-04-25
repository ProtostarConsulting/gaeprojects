package com.protostar.billingnstock.production.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.protostar.billingnstock.purchase.entities.LineItemEntity;
import com.protostar.billingnstock.stock.entities.BomLineItemCategory;
import com.protostar.billnstock.entity.BaseEntity;

@Cache
@Entity
public class ProductionRequisitionEntity extends BaseEntity {
	@Index
	private Ref<BomEntity> bomEntity;
	private int productQty;	
	private Date deliveryDate;
	private Date deliverytime;

	private List<BomLineItemCategory> catList = new ArrayList<BomLineItemCategory>();
	
	public List<BomLineItemCategory> getCatList() {
		return catList;
	}
	public BomEntity getBomEntity() {
		return bomEntity == null ? null : bomEntity.get();
	}
	public void setBomEntity(BomEntity bomEntity) {
		if (bomEntity != null)
			this.bomEntity = Ref.create(bomEntity);
		
	}
	public int getProductQty() {
		return productQty;
	}
	public void setProductQty(int productQty) {
		this.productQty = productQty;
	}
	public void setCatList(List<BomLineItemCategory> catList) {
		this.catList = catList;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public Date getDeliverytime() {
		return deliverytime;
	}
	public void setDeliverytime(Date deliverytime) {
		this.deliverytime = deliverytime;
	}	

}
