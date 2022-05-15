package com.tw.auction.service;

import com.tw.auction.common.exception.BusinessException;
import com.tw.auction.common.exception.NetWorkException;
import com.tw.auction.common.exception.NotFoundException;
import com.tw.auction.common.exception.TimeoutException;
import com.tw.auction.infra.client.BailmentClient;
import com.tw.auction.infra.client.response.BailmentResponse;
import com.tw.auction.infra.client.response.ReceiptResponse;
import com.tw.auction.infra.repository.AuctionRepository;
import com.tw.auction.infra.repository.entity.AuctionEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class AuctionServiceTest {
    @MockBean
    private AuctionRepository auctionRepository;
    @MockBean
    private BailmentClient bailmentClient;

    private AuctionService auctionService;

    @BeforeEach
    public void setup() {
        auctionService = new AuctionService(auctionRepository, bailmentClient);
    }

    @Test
    void should_get_bailment_model_successfully() {
        // Given
        long aid = 1L;
        String CN = "M12391";
        String name = "pearl";
        String status = "NOT_RECEIVED";
        AuctionEntity auctionEntity = new AuctionEntity(aid, CN, name, status);
        when(auctionRepository.findById(aid)).thenReturn(Optional.of(auctionEntity));

        String bid = "890123";
        BailmentResponse bailmentResponse = new BailmentResponse(aid, bid);
        when(bailmentClient.searchBailments(aid)).thenReturn(bailmentResponse);

        ReceiptResponse receiptResponse = ReceiptResponse.builder()
                .bid(bid)
                .url("http://test")
                .build();
        when(bailmentClient.receipts(bid)).thenReturn(receiptResponse);
        // When
        BailmentModel bailmentModel = auctionService.bailment(aid);
        // Then
        Assertions.assertEquals(aid, bailmentModel.getAid());
        Assertions.assertEquals(bid, bailmentModel.getId());
        Assertions.assertEquals("RECEIVED", bailmentModel.getStatus());
    }

    @Test
    void should_throw_business_exception_when_catch_not_found_exception() {
        // Given
        long aid = 1L;
        String CN = "M12391";
        String name = "pearl";
        String status = "NOT_RECEIVED";
        AuctionEntity auctionEntity = new AuctionEntity(aid, CN, name, status);
        when(auctionRepository.findById(aid)).thenReturn(Optional.of(auctionEntity));

        when(bailmentClient.searchBailments(aid)).thenThrow(new NotFoundException());

        // When
        BusinessException e = assertThrows(BusinessException.class, () -> auctionService.bailment(aid));
        // Then
        assertEquals(-1, e.getCode());
        assertEquals("拍品未送达", e.getMessage());
    }

    @Test
    void should_throw_network_exception_when_catch_timeout_exception() {
        // Given
        long aid = 1L;
        String CN = "M12391";
        String name = "pearl";
        String status = "NOT_RECEIVED";
        AuctionEntity auctionEntity = new AuctionEntity(aid, CN, name, status);
        when(auctionRepository.findById(aid)).thenReturn(Optional.of(auctionEntity));

        when(bailmentClient.searchBailments(aid)).thenThrow(new TimeoutException());

        // When
        NetWorkException e = assertThrows(NetWorkException.class, () -> auctionService.bailment(aid));
        // Then
        assertEquals(-2, e.getCode());
        assertEquals("网络异常，请稍后重试", e.getMessage());
    }
}