package com.etz.fraudeagleeyemanager.repository;


import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface RedisRepository<T, U> {

    public void create(T model);

    public Map<U, T> findAll();

    public T findById(U id);

    public void update(T model);

    public void delete(U id);
}
