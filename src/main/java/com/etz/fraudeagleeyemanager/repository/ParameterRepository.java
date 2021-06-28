package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long>{
        boolean existsByNameAndOperator(String name, String operator);
}
