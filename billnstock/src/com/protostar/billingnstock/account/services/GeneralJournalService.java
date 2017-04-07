package com.protostar.billingnstock.account.services;

/*Not required. Don't use*/
import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.account.entities.GeneralEntryEntity;
import com.protostar.billingnstock.account.entities.GeneralJournalEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "generalJournalService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.services", ownerName = "com.protostar.billingnstock.services", packagePath = ""))
public class GeneralJournalService {
	
	
	
	@ApiMethod(name = "addJournal")
	public void addJournal(GeneralJournalEntity journalEntity) {		
		ofy().save().entity(journalEntity).now();
	}
	
	@ApiMethod(name = "getAllJournalList")
	public List<GeneralJournalEntity> getAllJournalList() {
		return ofy().load().type(GeneralJournalEntity.class).list();
	}
	
	
	
	

}
