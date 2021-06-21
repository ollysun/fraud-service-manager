package com.etz.fraudeagleeyemanager.util;

import java.util.Collections;

//import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


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
