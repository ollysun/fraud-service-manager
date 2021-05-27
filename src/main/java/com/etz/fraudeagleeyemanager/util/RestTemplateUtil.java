package com.etz.fraudeagleeyemanager.util;

import com.etz.fraudeagleeyemanager.dto.response.ProductRestOutput;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@Slf4j
public class RestTemplateUtil {


    private final RestTemplate restTemplate = new RestTemplate();


    private HttpHeaders getHttpHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
       // httpHeaders.add("Authorization", "Bearer "+token);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return httpHeaders;
    }
    
    public ProductRestOutput getAllProducts() {
        ProductRestOutput productRestOutput = new ProductRestOutput();
        try {
            HttpHeaders httpHeaders = getHttpHeaders();
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<ProductRestOutput> val = restTemplate.exchange("http://172.17.10.83:12000/api/v1/ims/product",
                    HttpMethod.GET, httpEntity, ProductRestOutput.class);

            productRestOutput = val.getBody() != null ? val.getBody() :
                    null;

        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return productRestOutput;
    }

    


}
