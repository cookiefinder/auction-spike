package com.tw.auction.infra.client;

import com.tw.auction.infra.client.response.BailmentResponse;
import com.tw.auction.infra.client.response.ReceiptResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "bailment", url = "${service.bailment.url}")
public interface BailmentFeignClient {
    @PostMapping("/bailments")
    List<BailmentResponse> searchBailments(@RequestParam Long aid);

    @PostMapping("/bailments/{bid}/receipt")
    ReceiptResponse receipts(@PathVariable String bid);
}
