package com.tw.auction.infra.repository;

import com.tw.auction.infra.repository.entity.AuctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<AuctionEntity, Long> {
}
