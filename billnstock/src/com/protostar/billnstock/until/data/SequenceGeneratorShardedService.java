package com.protostar.billnstock.until.data;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.appengine.api.memcache.MemcacheService.SetPolicy;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.TxnType;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Parent;
import com.protostar.billingnstock.user.entities.BusinessEntity;

/**
 * A counter which can be incremented rapidly.
 *
 * Capable of incrementing the counter and increasing the number of shards. When
 * incrementing, a random shard is selected to prevent a single shard from being
 * written too frequently. If increments are being made too quickly, increase
 * the number of shards to divide the load. Performs datastore operations using
 * the low level datastore API.
 */
public class SequenceGeneratorShardedService {

	/**
	 * Convenience class which contains constants related to a named sharded
	 * counter. The counter name provided in the constructor is used as the
	 * entity key.
	 */
	@Cache
	@Entity
	public static final class CounterEntity {
		@Id
		private String counterName;

		@Parent
		private Key<BusinessEntity> business;
		private int shardCount;

		@Ignore
		private int tempDSCounterValue;
		@Ignore
		private int tempMCCounterValue;

		public CounterEntity() {

		}

		public CounterEntity(Key<BusinessEntity> business) {
			this.business = business;
		}

		public String getCounterName() {
			return counterName;
		}

		public void setCounterName(String counterName) {
			this.counterName = counterName;
		}

		public int getShardCount() {
			return shardCount;
		}

		public void setShardCount(int shardCount) {
			this.shardCount = shardCount;
		}

		public int getTempDSCounterValue() {
			return tempDSCounterValue;
		}

		public void setTempDSCounterValue(int tempDSCounterValue) {
			this.tempDSCounterValue = tempDSCounterValue;
		}

		public int getTempMCCounterValue() {
			return tempMCCounterValue;
		}

		public void setTempMCCounterValue(int tempMCCounterValue) {
			this.tempMCCounterValue = tempMCCounterValue;
		}

	}

	/**
	 * Convenience class which contains constants related to the counter shards.
	 * The shard number (as a String) is used as the entity key.
	 */
	@Cache
	@Entity
	public static final class CounterShard {
		@Parent
		private Key<CounterEntity> counter;
		@Id
		private long shardNumber;
		private int count;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public Key<CounterEntity> getCounter() {
			return counter;
		}

		public void setCounter(Key<CounterEntity> counter) {
			this.counter = counter;
		}

		public long getShardNumber() {
			return shardNumber;
		}

		public void setShardNumber(long shardNumber) {
			this.shardNumber = shardNumber;
		}

	}

	/**
	 * Default number of shards.
	 */
	private static final int INITIAL_SHARDS = 20;

	/**
	 * Cache duration for memcache.
	 */
	private static final int CACHE_PERIOD = 60;

	/**
	 * The name of this counter.
	 */
	private String counterName;
	private Key<CounterEntity> counterKey;
	private Key<BusinessEntity> businessKey;

	/**
	 * A random number generating, for distributing writes across shards.
	 */
	private final Random generator = new Random();

	/**
	 * Memcache service object for Memcache access.
	 */
	private final MemcacheService mc = MemcacheServiceFactory.getMemcacheService();

	/**
	 * A logger object.
	 */
	private static final Logger logger = Logger.getLogger(SequenceGeneratorShardedService.class.getName());

	/**
	 * Constructor which creates a sharded counter using the provided counter
	 * name.
	 *
	 * @param name
	 *            name of the sharded counter
	 */
	public SequenceGeneratorShardedService(Key<BusinessEntity> businessKey, final String counterName) {
		this.businessKey = businessKey;
		this.counterName = counterName;
		this.counterKey = Key.create(businessKey, CounterEntity.class, counterName);
	}

	/**
	 * Increase the number of shards for a given sharded counter. Will never
	 * decrease the number of shards.
	 *
	 * @param count
	 *            Number of new shards to build and store
	 */
	public final void addShards(final int count) {
		// Add the new number of shards to existing count.
		// To implement
	}

	public int getTempMCValue() {
		Object object = mc.get(this.counterKey);
		if (object == null) {
			return 0;
		} else {
			return (Integer) object;
		}
	}

	public final int getCounterCount() {
		Integer value = null;
		final int RETRYCOUNT = 5;
		boolean success = false;

		for (int i = 0; i < RETRYCOUNT; i++) {
			IdentifiableValue identifiable = mc.getIdentifiable(this.counterKey);
			if (identifiable != null) {
				value = (Integer) identifiable.getValue();
			}
			if (identifiable == null) {
				int sum = 0;
				List<CounterShard> counterShardList = ofy().load().type(CounterShard.class).ancestor(this.counterKey)
						.list();
				for (CounterShard shard : counterShardList) {
					sum += shard.getCount();
				}
				mc.put(this.counterKey, sum, Expiration.byDeltaSeconds(CACHE_PERIOD),
						SetPolicy.ADD_ONLY_IF_NOT_PRESENT);
				continue;
			}
			if (value != null) {
				success = mc.putIfUntouched(this.counterKey, identifiable, new Integer(value + 1));
				if (success) {
					break;
				}
				{
					continue;
				}
			}
		}

		if (!success) {
			RuntimeException runtimeException = new RuntimeException(
					"Could not generate unique counter from Memcache for kind: " + this.counterKey.getName()
							+ " . There is lot of load on this counder. Please try latter on.");
			throw runtimeException;
		}
		return value;
	}

	/**
	 * Retrieve the value of this sharded counter.
	 *
	 * @return Summed total of all shards' counts
	 */
	public final int getNextSequenceNumber() {
		// Increament in DB first. That is mostly fails.
		if (increment()) {
			return getCounterCount();
		}
		return 0;
	}

	/**
	 * Increment the value of this sharded counter.
	 */
	private final boolean increment() {
		// Find how many shards are in this counter.
		int numShards = getShardCount();

		// Choose the shard randomly from the available shards.
		int shardNum = generator.nextInt(numShards) + 1;

		Key<CounterShard> shardKey = Key.create(this.counterKey, CounterShard.class, shardNum);
		return incrementShardCountTx(shardKey, 1, 1);
	}

	/**
	 * Get the number of shards in this counter.
	 *
	 * @return shard count
	 */
	private int getShardCount() {
		CounterEntity counterEntity = ofy().load().key(this.counterKey).now();
		if (counterEntity == null) {
			counterEntity = new CounterEntity(this.businessKey);
			counterEntity.setCounterName(this.counterName);
			counterEntity.setShardCount(INITIAL_SHARDS);
			ofy().save().entity(counterEntity).now();
		}
		return counterEntity.getShardCount();
	}

	/**
	 * Increment datastore property value inside a transaction. If the entity
	 * with the provided key does not exist, instead create an entity with the
	 * supplied initial property value.
	 *
	 * @param key
	 *            the entity key to update or create
	 * @param prop
	 *            the property name to be incremented
	 * @param increment
	 *            the amount by which to increment
	 * @param initialValue
	 *            the value to use if the entity does not exist
	 */
	private boolean incrementShardCountTx(final Key<CounterShard> shardKey, final int increment,
			final int initialValue) {
		try {
			ofy().transactNew(1, new Work<Key<CounterShard>>() {
				private Key<CounterShard> shardKey;
				private Key<CounterEntity> counterKey;

				private Work<Key<CounterShard>> init(Key<CounterShard> shardKey, Key<CounterEntity> counterKey) {
					this.shardKey = shardKey;
					this.counterKey = counterKey;
					return this;
				}

				public Key<CounterShard> run() {
					CounterShard shardEntity;
					int value;

					shardEntity = ofy().load().key(shardKey).now();
					if (shardEntity == null) {
						shardEntity = new CounterShard();
						shardEntity.setCounter(this.counterKey);
						shardEntity.setShardNumber(shardKey.getId());
						value = initialValue;
					}
					value = shardEntity.getCount() + increment;
					shardEntity.setCount(value);
					ofy().save().entity(shardEntity).now();
					return shardKey;
				}
			}.init(shardKey, this.counterKey));
		} catch (ConcurrentModificationException e) {
			logger.log(Level.WARNING, "You may need more shards. Consider adding more shards.");
			logger.log(Level.WARNING, e.toString(), e);
			throw new RuntimeException(
					"There is load on this counter: " + this.counterName + ". Consider increasing shard count.", e);
		} catch (Exception e) {
			logger.log(Level.WARNING, e.toString(), e);
			throw new RuntimeException("There is error while executing incrementShardCountTx() method.", e);
		}

		return true;
	}
}
