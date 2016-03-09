package jtanks.game.util;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @param <K> key type
 * @param <V> value type
 */
public class Cache<K,V> {

    public static final Cache GLOBAL = new Cache<String, Object>();

    private HashMap<K,V> storage = new HashMap<K,V>();
    private Lock lock = new ReentrantLock();

    /**
     * Get storage object by key
     * 
     * @param key
     * @return
     */
    public V get(K key) {
        lock.lock();
        try {
            return (V) storage.get(key);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Get object associated with given key from cache storage
     * or get it from callable object and put into cache if it absent
     *
     * @param key
     * @param callable
     * @return associated object
     */
    public V get(K key, Callable<V> callable) {
        lock.lock();
        try {
            if (storage.get(key) != null) {
                return storage.get(key);
            }
            try {
                storage.put(key, callable.call());
            } catch (Exception ex) {
                Logger.getLogger(Cache.class.getName()).log(Level.SEVERE, null, ex);
            }
            return storage.get(key);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Store object in the cache if it absent
     * 
     * @param key
     * @param value
     */
    public void putIfAbsent(K key, V value) {
        lock.lock();
        try {
            if (storage.containsKey(key) == false || storage.get(key) == null) {
                storage.put(key, value);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Store object in the cache with the given key
     * 
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        lock.lock();
        try {
            storage.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Delete cache storage object associated with given key
     *
     * @param key
     * @return operation result
     */
    public boolean remove(K key) {
        lock.lock();
        try {
            return storage.remove(key) == null ? false : true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Clean storage
     */
    public void purge() {
        lock.lock();
        try {
            storage.clear();
        } finally {
            lock.unlock();
        }
    }
}
