package com.etz.fraudeagleeyemanager.util;

import com.etz.fraudeagleeyemanager.dto.response.CardBinResponse;
import com.etz.fraudeagleeyemanager.dto.response.ProductRestOutput;
import com.etz.fraudeagleeyemanager.exception.FraudEngineException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
//import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.client.HttpClients;

import java.util.Collections;


@Slf4j
public class RestTemplateUtil {

    @Value("${apikey}")
    private String apiKey;

    private HttpHeaders getBinHttpHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("apikey", apiKey);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

//    private HttpComponentsClientHttpRequestFactory getBinCodeHttpClientFactory(){
//        CloseableHttpClient httpClient
//                = HttpClients.custom()
//                .setSSLHostnameVerifier(new NoopHostnameVerifier())
//                .build();
//        HttpComponentsClientHttpRequestFactory requestFactory
//                = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setHttpClient(httpClient);
//        return requestFactory;
//    }


//    public CardBinResponse getCardDetailsByCardBin(String cardBin) {
//        CardBinResponse cardBinResponse = new CardBinResponse();
//        try {
//            HttpHeaders httpHeaders = getBinHttpHeaders();
//            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
//            ResponseEntity<CardBinResponse> response
//                = new RestTemplate(getBinCodeHttpClientFactory()).exchange(
//                "https://api.promptapi.com/bincheck/"+cardBin, HttpMethod.GET, httpEntity, CardBinResponse.class);
//            cardBinResponse = response.getBody() != null ? response.getBody() :
//                    null;
//        } catch (HttpClientErrorException e) {
//            log.error(e.getMessage());
//            throw new FraudEngineException(e.getLocalizedMessage());
//        } catch (RestClientException e) {
//            log.error(e.getMessage());
//            throw new FraudEngineException(e.getLocalizedMessage());
//        }
//        return cardBinResponse;
//    }

    


}
