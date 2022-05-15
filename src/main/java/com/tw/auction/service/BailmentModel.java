package com.tw.auction.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BailmentModel {
    private String id;
    private Long aid;
    private String status;
}
