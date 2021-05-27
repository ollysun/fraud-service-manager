package com.etz.fraudeagleeyemanager.dto.response;

import com.etz.fraudeagleeyemanager.entity.ProductEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;




@JsonIgnoreProperties({ "productDataset", "productRules", "products", "productLists" })
public class ProductResponse extends ProductEntity {
}
