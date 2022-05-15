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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final BailmentClient bailmentClient;

    @Transactional
    public BailmentModel bailment(Long aid) {
        try {
            AuctionEntity auctionEntity = auctionRepository.findById(aid).get();
            auctionEntity.setBailmentStatus("RECEIVED");
            auctionRepository.save(auctionEntity);
            BailmentResponse bailmentResponse = bailmentClient.searchBailments(aid);
            ReceiptResponse receiptResponse = bailmentClient.receipts(bailmentResponse.getBid());
            return BailmentModel.builder()
                    .id(bailmentResponse.getBid())
                    .aid(aid)
                    .status("RECEIVED")
                    .build();
        } catch (NotFoundException e) {
            throw new BusinessException(-1, "拍品未送达");
        } catch (TimeoutException e) {
            throw new NetWorkException(-2, "网络异常，请稍后重试");
        }
    }
}
