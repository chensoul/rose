package com.chensoul.spring.boot.redis.zk;

public interface ZkLock {

	boolean lock(String lockpath);

	boolean unlock(String lockpath);

}
