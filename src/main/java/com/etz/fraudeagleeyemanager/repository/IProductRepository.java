package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface IProductRepository extends JpaRepository<Product, BigInteger> {
}
