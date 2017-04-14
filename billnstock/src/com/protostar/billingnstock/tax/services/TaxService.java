package com.protostar.billingnstock.tax.services;

import static com.googlecode.objectify.ObjectifyService.ofy;

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
	public TaxEntity addTax(TaxEntity taxEntity) {
		ofy().save().entity(taxEntity).now();
		return taxEntity;
	}

	@ApiMethod(name = "getAllTaxes")
	public List<TaxEntity> getAllTaxes(@Named("id") Long busId) {
		List<TaxEntity> filteredTax = ofy().load().type(TaxEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredTax;
	}

	@ApiMethod(name = "updateTax")
	public TaxEntity updateTax(TaxEntity taxEntity) {
		return addTax(taxEntity);
	}

	@ApiMethod(name = "getTaxByID", path = "getTaxByID")
	public TaxEntity getTaxByID(@Named("busId") Long busId, @Named("id") Long taxId) {

		List<TaxEntity> list = ofy().load().type(TaxEntity.class)
				.filterKey(Key.create(Key.create(BusinessEntity.class, busId), TaxEntity.class, taxId)).list();
		TaxEntity taxEntity = list.size() > 0 ? list.get(0) : null;
		return taxEntity;
	}

	@ApiMethod(name = "getTaxesByVisibility", path = "getTaxesByVisibility")
	public List<TaxEntity> getTaxesByVisibility(@Named("id") Long busId) {
		List<TaxEntity> filteredTax = ofy().load().type(TaxEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).filter("active", true).list();
		return filteredTax;
	}
}// end of TaxServices
