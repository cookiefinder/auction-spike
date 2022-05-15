package com.tw.auction.infra.client;

import com.tw.auction.common.exception.NotFoundException;
import com.tw.auction.common.exception.TimeoutException;
import com.tw.auction.infra.client.response.BailmentResponse;
import com.tw.auction.infra.client.response.ReceiptResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BailmentClient {
    private final BailmentFeignClient feignClient;

    public BailmentResponse searchBailments(Long aid) {
        try {
            return feignClient.searchBailments(aid).get(0);
        } catch (FeignException.NotFound e) {
            throw new NotFoundException();
        } catch (FeignException.FeignClientException e) {
            throw new TimeoutException();
        }
    }

    public ReceiptResponse receipts(String bid) {
        return feignClient.receipts(bid);
    }
}
