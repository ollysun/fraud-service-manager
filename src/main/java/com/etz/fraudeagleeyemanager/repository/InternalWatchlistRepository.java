package com.etz.fraudeagleeyemanager.repository;

import com.etz.fraudeagleeyemanager.entity.InternalWatchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalWatchlistRepository extends JpaRepository<InternalWatchlist, Long>{

}
