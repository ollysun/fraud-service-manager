package com.etz.fraudeagleeyemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.etz.fraudeagleeyemanager.entity.OfacWatchlist;


@Repository
public interface OfacWatchlistRepository extends JpaRepository<OfacWatchlist, Long> {

}
