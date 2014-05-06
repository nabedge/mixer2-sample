package org.mixer2.sample.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Item implements java.io.Serializable {

    private static final long serialVersionUID = -7542865246382197538L;

    private long id;

    private String name;

    private String description;

    private BigDecimal price;

    private long categoryId;

    private String categoryName;

}
