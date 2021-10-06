package com.etz.fraudeagleeyemanager.entity;

import java.io.Serializable;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class CardProductId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
    private String productCode;

    private Long cardId;
}
