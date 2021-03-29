package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.OfacWatchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OfacWatchlistRepository extends JpaRepository<OfacWatchlist, Long> {

}
