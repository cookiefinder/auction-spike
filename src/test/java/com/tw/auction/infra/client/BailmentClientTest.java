package com.tw.auction.infra.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.tw.auction.WireMockConfig;
import com.tw.auction.common.exception.NotFoundException;
import com.tw.auction.common.exception.TimeoutException;
import com.tw.auction.infra.client.response.BailmentResponse;
import com.tw.auction.infra.client.response.ReceiptResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

import static java.nio.charset.Charset.defaultCharset;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.util.StreamUtils.copyToString;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {WireMockConfig.class})
class BailmentClientTest {

    @Autowired
    private WireMockServer mockServer;

    @Autowired
    private BailmentFeignClient feignClient;

    private BailmentClient bailmentClient;

    @BeforeEach
    void setUp() {
        bailmentClient = new BailmentClient(feignClient);
    }

    @Test
    void should_search_bailment_by_aid_successfully() throws IOException {
        // Given
        Long aid = 1L;

        mockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/bailments?aid=1"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(copyToString(BailmentClientTest.class.getClassLoader()
                                        .getResourceAsStream("payload/search-bailment-response.json"),
                                defaultCharset()))));

        // When
        BailmentResponse bailmentResponse = bailmentClient.searchBailments(aid);

        // Then
        Assertions.assertEquals(aid, bailmentResponse.getAid());
        Assertions.assertEquals("9823", bailmentResponse.getBid());
    }

    @Test
    void should_receipt_successfully() throws IOException {
        // Given
        String bid = "9823";

        mockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/bailments/9823/receipt"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(copyToString(BailmentClientTest.class.getClassLoader()
                                        .getResourceAsStream("payload/receipt-response.json"),
                                defaultCharset()))));

        // When
        ReceiptResponse receiptResponse = bailmentClient.receipts(bid);

        // Then
        Assertions.assertEquals(bid, receiptResponse.getBid());
        Assertions.assertEquals("http://test.com", receiptResponse.getUrl());
    }

    @Test
    void should_throw_not_found_exception_when_return_404() {
        // Given
        Long aid = 1L;

        mockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/bailments?aid=1"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())));

        // When
        assertThrows(NotFoundException.class, () -> bailmentClient.searchBailments(aid));
    }

    @Test
    void should_throw_timeout_exception_when_return_504() {
        // Given
        Long aid = 1L;

        mockServer.stubFor(WireMock.post(WireMock.urlEqualTo("/bailments?aid=1"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.REQUEST_TIMEOUT.value())));

        // When
        assertThrows(TimeoutException.class, () -> bailmentClient.searchBailments(aid));
    }
}