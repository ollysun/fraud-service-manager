package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.entity.Card;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties({ "cards"})
public class CardResponse extends Card {
}
