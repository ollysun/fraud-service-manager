package com.etz.fraudeagleeyemanager.dto.response;

import java.io.Serializable;
import java.util.List;

public class ProductRestOutput implements Serializable {
    private String status;
    private String message;
    private List<ContentResponse> data;
}
