package com.tw.auction.api.representation;

import com.tw.auction.service.BailmentModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BailmentRepresentation {
    private String id;
    private Long aid;
    private String status;

    public static BailmentRepresentation from(BailmentModel bailment) {
        return new BailmentRepresentation(bailment.getId(),
                bailment.getAid(),
                bailment.getStatus());
    }
}
