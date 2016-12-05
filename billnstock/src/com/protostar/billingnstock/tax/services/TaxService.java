package com.protostar.billingnstock.tax.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "taxService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.tax.services", ownerName = "com.protostar.billingnstock.tax.services", packagePath = ""))
public class TaxService {
	@ApiMethod(name = "addTax")
	public void addTax(TaxEntity taxEntity) {
		if (taxEntity.getId() == null) {
			taxEntity.setCreatedDate(new Date());
		}
		Key<TaxEntity> now = ofy().save().entity(taxEntity).now();
	}

	@ApiMethod(name = "getAllTaxes")
	public List<TaxEntity> getAllTaxes(@Named("id") Long busId) {
		List<TaxEntity> filteredTax = ofy().load().type(TaxEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredTax;
	}

	@ApiMethod(name = "updateTax")
	public void updateTax(TaxEntity taxEntity) {
		taxEntity.setModifiedDate(new Date());
		Key<TaxEntity> now = ofy().save().entity(taxEntity).now();
	}

	@ApiMethod(name = "getTaxesByVisibility", path = "getTaxesByVisibility")
	public List<TaxEntity> getTaxesByVisibility(@Named("id") Long busId) {
		List<TaxEntity> filteredTax = ofy().load().type(TaxEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId))
				.filter("active", true).list();
		return filteredTax;
	}
}// end of TaxServices
