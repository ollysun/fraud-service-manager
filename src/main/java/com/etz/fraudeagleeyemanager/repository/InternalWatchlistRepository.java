package com.etz.fraudeagleeyemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.InternalWatchlist;

@Repository
public interface InternalWatchlistRepository extends JpaRepository<InternalWatchlist, Long>{

}
