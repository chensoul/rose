package com.chensoul.rose.redis.zk;

public interface ZkLock {

    boolean lock(String lockpath);

    boolean unlock(String lockpath);
}
