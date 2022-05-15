package com.tw.auction.infra.repository;

import com.tw.auction.infra.repository.entity.AuctionEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class AuctionRepositoryTest {
    @Autowired
    private AuctionRepository auctionRepository;

    @Test
    void should_find_auction_by_id_successfully() {
        // Given
        Long aid = 1L;

        // When
        AuctionEntity auctionEntity = auctionRepository.findById(aid).orElse(null);

        // Then
        Assertions.assertNotNull(auctionEntity);
    }

    @Test
    void should_save_auction_successfully() {
        // Given
        Long aid = 1L;
        AuctionEntity auctionEntity = auctionRepository.findById(aid).orElse(null);

        // When
        auctionEntity.setBailmentStatus("RECEIVED");
        auctionRepository.save(auctionEntity);

        // Then
        AuctionEntity saved = auctionRepository.findById(aid).orElse(null);
        Assertions.assertNotNull(saved);
        Assertions.assertEquals("RECEIVED", saved.getBailmentStatus());
    }
}