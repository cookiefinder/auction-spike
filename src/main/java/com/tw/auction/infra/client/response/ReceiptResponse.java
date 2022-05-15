package com.tw.auction.infra.client.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReceiptResponse {
    private String bid;
    private String url;
}
