package com.etz.fraudeagleeyemanager.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class CardBinResponse implements Serializable {
	private static final long serialVersionUID = 1L;
    @JsonProperty("bank_name")
    private String bankName;
    private String bin;
    private String country;
    private String scheme;
    private String type;
    private String url;
}
