/*
 * $Id: CacheMap.java,v 1.13 2007/02/09 01:55:01 tryggvil Exp $
 * Created on 6.1.2006 in project com.idega.core
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.core.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;


/**
 * <p>
 * Wrapper for the Cache implemented as a standard Map
 * </p>
 *  Last modified: $Date: 2007/02/09 01:55:01 $ by $Author: tryggvil $
 * 
 * @author <a href="mailto:tryggvil@idega.com">tryggvil</a>
 * @version $Revision: 1.13 $
 */
public class CacheMap<K extends Serializable, V> implements Map<K, V> {

	private static final Logger LOGGER = Logger.getLogger(CacheMap.class.getName());
	
	private boolean resetable = Boolean.TRUE;
	
	private Cache cache;
	
	private List<CacheMapListener<K, V>> cacheListeners;
	private List<CacheMapGuardian<K, V>> guardians;
	
	CacheMap(Cache cache) {
		this(cache, Boolean.TRUE);
	}
	
	CacheMap(Cache cache, boolean resetable) {
		this(cache, resetable, null, null);
	}
	
	CacheMap(Cache cache, boolean resetable, CacheMapListener<K, V> cacheListener) {
		this(cache, resetable, cacheListener, null);
	}
	
	CacheMap(Cache cache, boolean resetable, CacheMapGuardian<K, V> guardian) {
		this(cache, resetable, null, guardian);
	}
	
	CacheMap(Cache cache, boolean resetable, CacheMapListener<K, V> cacheListener, CacheMapGuardian<K, V> guardian) {
		this.cache = cache;
		this.resetable = resetable;
		
		if (cacheListener != null) {
			addCacheListener(cacheListener);
		}
		if (guardian != null) {
			addCacheGuardian(guardian);
		}
	}
	
	Cache getCache() {
		return this.cache;
	}
	
	public int size() {
		return cache.getSize();
	}

	public boolean isEmpty() {
		return cache.getSize() == 0;
	}

	public boolean containsKey(Object key) {
		try {
			Element element = getCache().get((Serializable) key);
			return element != null && (element.getObjectValue() != null || element.getValue() != null);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException("Method containsValue not implemented");
	}

	@SuppressWarnings("unchecked")
	public V get(Object key) {
		if (key == null) {
			return null;
		}
		
		try {
			K realKey = (K) key;
			
			boolean canGet = true;
			if (getCacheGuardians() != null) {
				for (Iterator<CacheMapGuardian<K, V>> guardiansIter = getCacheGuardians().iterator(); (guardiansIter.hasNext() && canGet);) {
					canGet = guardiansIter.next().beforeGet(realKey);
				}
			}
			if (!canGet) {
				LOGGER.warning("Object can not be fetched by the key " + key + " because of the guardian(s)!");
				return null;
			}
			
			Element element = getCache().get(key);
			if (element == null) {
				return null;
			}
			
			Object o = element.getObjectValue();
			if (o == null) {
				return null;
			}
			
			V result = (V) o;
			
			if (getCacheListeners() != null) {
				for (Iterator<CacheMapListener<K, V>> iterator = getCacheListeners().iterator(); iterator.hasNext();) {
					CacheMapListener<K, V> listener = iterator.next();
					listener.gotObject(realKey, result);
				}
			}
			
			return result;
		} catch (ClassCastException e) {
			LOGGER.log(Level.WARNING, "Error while casting", e);
			throw new RuntimeException(e);
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		} catch (CacheException e) {
			throw new RuntimeException(e);
		}
	}
	
	public V put(K key, V value) {
		if (key == null || value == null) {
			LOGGER.warning("Some element (key="+key+" or value="+value+") is null! Not adding element to cache.");
			return value;
		}
		
		if (!(value instanceof Serializable))
			LOGGER.warning("Attempting to put into the cache (name: '" + cache.getName() + "') not serializable object (key: '" + key + "', value: '" + value +
					"'): it may cause an error while trying to serialized the cache");
		
		try {
			boolean canPut = true;
			if (getCacheGuardians() != null) {
				for (Iterator<CacheMapGuardian<K, V>> guardiansIter = getCacheGuardians().iterator(); (guardiansIter.hasNext() && canPut);) {
					canPut = guardiansIter.next().beforePut(key, value);
				}
			}
			if (!canPut) {
				LOGGER.warning("Object " + value + " can not be put with the key " + key + " because of the guardian(s)!");
				return null;
			}
			
			if (cache.getCacheConfiguration().isOverflowToDisk() && !containsKey(key)) {
				long maxElementsInMemory = cache.getCacheConfiguration().getMaxElementsInMemory();
				long currentCacheSize = cache.getMemoryStoreSize();
				if (maxElementsInMemory == currentCacheSize) {
					LOGGER.info("Flushing the cache: " + cache.getName() + " because the cache size (" + currentCacheSize + ") has reached maximum: " +
							maxElementsInMemory);
					cache.flush();
				}
			}
			
			Element element = new Element(key, value);
			boolean checkTheSizes = !keySet().contains(key);
			int sizeBefore = cache.getSize();
			cache.put(element);
			int sizeAfter = cache.getSize();
			if (checkTheSizes && (sizeAfter <= 0 || sizeBefore == sizeAfter)) {
				LOGGER.warning("Value '" + value + "' with key '" + key + "' was not added to the cache " + cache.getName());
				return null;
			}
			
			if (getCacheListeners() != null) {
				for (Iterator<CacheMapListener<K, V>> iterator = getCacheListeners().iterator(); iterator.hasNext();) {
					CacheMapListener<K, V> listener = iterator.next();
					listener.putObject(key, value);
				}
			}
			
			return value;
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public V remove(Object key) {
		try {
			K realKey = (K) key;
			V realElementToRemove = null;
			Element objectToRemove = getCache().get(realKey);
			if (objectToRemove != null) {
				realElementToRemove = (V) objectToRemove.getObjectValue();
			}
			
			boolean canRemove = true;
			if (getCacheGuardians() != null) {
				for (Iterator<CacheMapGuardian<K, V>> guardiansIter = getCacheGuardians().iterator(); (guardiansIter.hasNext() && canRemove);) {
					canRemove = guardiansIter.next().beforeRemove(realKey, realElementToRemove);
				}
			}
			if (!canRemove) {
				LOGGER.warning("Object " + realElementToRemove + " can not be removed by the key " + key + " because of the guardian(s)!");
				return null;
			}			
			
			getCache().remove(realKey);
			
			if (getCacheListeners() != null) {
				for (Iterator<CacheMapListener<K, V>> iterator = getCacheListeners().iterator(); iterator.hasNext();) {
					CacheMapListener<K, V> listener = iterator.next();
					listener.removedObject(realKey);
				}
			}
			
			return realElementToRemove;
		} catch (ClassCastException e) {
			LOGGER.log(Level.WARNING, "Error while casting", e);
			throw new RuntimeException(e);
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		}
	}

	public void putAll(Map<? extends K, ? extends V> map) {
		for (Iterator<? extends K> iter = map.keySet().iterator(); iter.hasNext();) {
			K key = iter.next();
			V value = map.get(key);
			if (key != null && value != null) {
				put(key, value);
			}
		}
	}

	public void clear() {
		try {
			if (!resetable) {
				LOGGER.info("Cache " + cache.getName() + " is not resetable!");
				return;
			}
			
			boolean canClear = true;
			if (getCacheGuardians() != null) {
				for (Iterator<CacheMapGuardian<K, V>> guardiansIter = getCacheGuardians().iterator(); (guardiansIter.hasNext() && canClear);) {
					canClear = guardiansIter.next().beforeClear();
				}
			}
			if (!canClear) {
				LOGGER.warning("Cache " + cache.getName() + " can not be cleared because of the guardian(s)!");
				return;
			}
			
			if (getCache().getSize() > 0) {
				getCache().removeAll();
				LOGGER.info("Cleared cache: " + this.cache.getName());
			}
			if (getCacheListeners() != null) {
				for (Iterator<CacheMapListener<K, V>> iterator = getCacheListeners().iterator(); iterator.hasNext();) {
					CacheMapListener<K, V> listener = iterator.next();
					listener.cleared();
				}
			}
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public Set<K> keySet() {
		Set<K> set = new HashSet<K>();
		List<K> keys;
		try {
			keys = getCache().getKeys();
			set.addAll(keys);
			return set;
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		} catch (CacheException e) {
			throw new RuntimeException(e);
		}
	}

	public Collection<V> values() {
		Collection<V> values = new ArrayList<V>();
		for (Iterator<K> iter = keySet().iterator(); iter.hasNext();) {
			K key = iter.next();
			V value = get(key);
			if (value != null) {
				values.add(value);
			}
		}
		return values;
	}

	public Set<Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException("Method entrySet() not implemented");
	}
	
	/**
	 * @return Returns the cacheListeners.
	 */
	public List<CacheMapListener<K, V>> getCacheListeners() {
		return this.cacheListeners;
	}

	/**
	 * @param cacheListeners The cacheListeners to set.
	 */
	public void setCacheListeners(List<CacheMapListener<K, V>> cacheListeners) {
		this.cacheListeners = cacheListeners;
	}
	
	/**
	 * @return Returns the cacheListeners.
	 */
	public void addCacheListener(CacheMapListener<K, V> listener) {
		List<CacheMapListener<K, V>> cacheListeners = getCacheListeners();
		if (cacheListeners == null) {
			cacheListeners = new ArrayList<CacheMapListener<K, V>>();
			setCacheListeners(cacheListeners);
		}
		cacheListeners.add(listener);
	}
	
	public List<CacheMapGuardian<K, V>> getCacheGuardians() {
		return this.guardians;
	}

	public void setCacheGuardians(List<CacheMapGuardian<K, V>> guardians) {
		this.guardians = guardians;
	}
	
	public void addCacheGuardian(CacheMapGuardian<K, V> guardian) {
		if (getCacheGuardians() == null) {
			guardians = new ArrayList<CacheMapGuardian<K, V>>();
			setCacheGuardians(guardians);
		}
		guardians.add(guardian);
	}
	
	@Override
	public String toString() {
		return cache == null ? "Unknown cache" : "Cache: " + cache.getName() + ". Keys: " + keySet() + "; values: " + values();
	}
}