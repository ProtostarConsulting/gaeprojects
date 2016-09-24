package com.protostar.billnstock.until.data;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.googlecode.objectify.Work;

public class SequenceGeneratorService {

	private static final String COUNTER = "counter";

	public static enum SequenceKind {
		VoucherNumber, InvoiceNumber, AccountNumber
	};

	public Long getNextSequenceNumber(final SequenceKind kind, final Long bizId) {

		Long currentSeqNumber = ofy().transact(new Work<Long>() {
			public Long run() {

				final Key sequenceKindKey = new Entity(kind.toString(), bizId)
						.getKey();
				DatastoreService datastore = DatastoreServiceFactory
						.getDatastoreService();
				Entity seqEntity = null;				
				Transaction tx = datastore.beginTransaction();
				try {
					seqEntity = datastore.get(sequenceKindKey);
				} catch (EntityNotFoundException e) {
					// OK. I will create it
					seqEntity = new Entity(kind.toString(), bizId);
					seqEntity.setUnindexedProperty(COUNTER, 1L);
				}

				Long currentSeqNumber = Long.getLong(seqEntity.getProperty(
						COUNTER).toString());
				seqEntity.setUnindexedProperty(COUNTER, currentSeqNumber + 1L);
				datastore.put(seqEntity);
				tx.commit();
				return currentSeqNumber;
			}
		});

		return currentSeqNumber;
	}

}