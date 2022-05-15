package com.tw.auction.infra.client.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BailmentResponse {
    private Long aid;
    public String bid;
}
