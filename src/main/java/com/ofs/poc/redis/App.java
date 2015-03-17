package com.ofs.poc.redis;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Hello world!
 *
 */
public class App {

	private static final String redisHost = "192.168.8.153";
	private static final Integer redisPort = 6379;

	private static final int NO_OF_ENTRIES = 100;
	private static final String KEY_PREFIX = "key";

	public static void main(String[] args) {

//		log("Creating key array");
//		String[] keys = new String[NO_OF_ENTRIES];
//		for (int index = 0; index < NO_OF_ENTRIES;) {
//			keys[index++] = KEY_PREFIX + index;
//		}
		
		log("Connecting to " + redisHost + ":" + redisPort);
		log("Creating Jedis connection pool..");
		JedisPool jedisPool = new JedisPool(redisHost, redisPort);
		log("Connection pool created.");

		Jedis jedis = jedisPool.getResource();
		log("Acquired the jedis instance.");

		System.out.println("Inserting " + NO_OF_ENTRIES + " entries to REDIS !!");
		try {

			long startTime = System.currentTimeMillis();
			for (int index = 0; index < NO_OF_ENTRIES; index++) {

				String str = KEY_PREFIX + index;
				jedis.set(str, str); // adds a key
				//jedis.del(str); // removes a key
				//jedis.hexists(str, str); //checks whether a key-value pair exists
				
				/*Map<String, String> strMap = new HashMap<String, String>();
				strMap.put(str, str);
				jedis.hmset(str, strMap);*/
			}

			//Set<String> result = jedis.keys(KEY_PREFIX + "*");
			log ("Saving data to REDIS.");
			jedis.save();
			log ("Saved");

			log ("Total Insertion time : " + (System.currentTimeMillis() - startTime) + "ms");
		} catch (JedisException e) {

			// if something wrong happen, return it back to the pool
			if (null != jedis) {
				jedisPool.returnBrokenResource(jedis);
				jedis = null;
			}

		} finally {

			// it's important to return the Jedis instance to the pool once
			// you've finished using it
			if (null != jedis) {
				jedisPool.returnResource(jedis);
			}
		}
		
		jedisPool.close();
	}
	
	private static void log (String str) {
		System.out.println(str);
	}
}
