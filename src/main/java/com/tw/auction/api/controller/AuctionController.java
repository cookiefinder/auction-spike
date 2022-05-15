package com.tw.auction.api.controller;

import com.tw.auction.api.representation.BailmentRepresentation;
import com.tw.auction.api.representation.ShellRepresentation;
import com.tw.auction.common.exception.BusinessException;
import com.tw.auction.common.exception.NetWorkException;
import com.tw.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;

    @PostMapping("/{aid}/bailment")
    public ResponseEntity<ShellRepresentation> bailment(@PathVariable Long aid) {
        try {
            return ResponseEntity.ok(ShellRepresentation.of(BailmentRepresentation.from(auctionService.bailment(aid))));
        } catch (BusinessException | NetWorkException e) {
            return ResponseEntity.internalServerError().body(ShellRepresentation.of(e));
        }
    }
}