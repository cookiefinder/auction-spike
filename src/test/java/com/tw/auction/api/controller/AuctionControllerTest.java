package com.tw.auction.api.controller;

import com.tw.auction.api.representation.BailmentRepresentation;
import com.tw.auction.common.exception.BusinessException;
import com.tw.auction.common.exception.NetWorkException;
import com.tw.auction.service.AuctionService;
import com.tw.auction.service.BailmentModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class AuctionControllerTest {
    @MockBean
    private AuctionService auctionService;

    private MockMvc mockMvc;

    private AuctionController auctionController;

    @BeforeEach
    public void setup() {
        auctionController = new AuctionController(auctionService);
        mockMvc = MockMvcBuilders.standaloneSetup(auctionController).build();
    }

    @Test
    void should_get_bailment_representation_successfully() {
        // Given
        Long aid = 1L;
        String status = "RECEIVED";
        String bid = "1001";
        BailmentModel bailmentModel = BailmentModel.builder()
                .id(bid)
                .aid(aid)
                .status(status)
                .build();
        when(auctionService.bailment(aid)).thenReturn(bailmentModel);

        // When
        Object data = auctionController.bailment(aid).getBody().getData();
        BailmentRepresentation bailmentRepresentation = (BailmentRepresentation) data;

        // Then
        Assertions.assertEquals(bid, bailmentRepresentation.getId());
        Assertions.assertEquals(aid, bailmentRepresentation.getAid());
        Assertions.assertEquals(status, bailmentRepresentation.getStatus());
    }

    @Test
    void should_serialize_representation() throws Exception {
        // Given
        String id = "1001";
        Long aid = 1L;
        String status = "RECEIVED";
        BailmentModel bailmentModel = BailmentModel.builder()
                .id(id)
                .aid(aid)
                .status(status)
                .build();
        when(auctionService.bailment(aid)).thenReturn(bailmentModel);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/auctions/1/bailment"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{\"data\":{\"id\":\"1001\",\"aid\":1,\"status\":\"RECEIVED\"}}"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void should_throw_business_exception() {
        // Given
        Long aid = 1L;
        when(auctionService.bailment(aid)).thenThrow(new BusinessException(-1, "拍品未送达"));

        // When
        Object data = auctionController.bailment(aid).getBody().getData();
        BusinessException e = (BusinessException) data;

        // Then
        assertEquals(-1, e.getCode());
        assertEquals("拍品未送达", e.getMessage());
    }

    @Test
    void should_serialize_business_exception() throws Exception {
        // Given
        Long aid = 1L;
        when(auctionService.bailment(aid)).thenThrow(new BusinessException(-1, "拍品未送达"));

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/auctions/1/bailment"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void should_throw_network_exception() {
        // Given
        Long aid = 1L;
        when(auctionService.bailment(aid)).thenThrow(new NetWorkException(-2, "网络异常，请稍后重试"));

        // When
        Object data = auctionController.bailment(aid).getBody().getData();
        NetWorkException e = (NetWorkException) data;

        // Then
        assertEquals(-2, e.getCode());
        assertEquals("网络异常，请稍后重试", e.getMessage());
    }

    @Test
    void should_serialize_network_exception() throws Exception {
        // Given
        Long aid = 1L;
        when(auctionService.bailment(aid)).thenThrow(new NetWorkException(-2, "网络异常，请稍后重试"));

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/auctions/1/bailment"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andDo(MockMvcResultHandlers.print());
    }
}