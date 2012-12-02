package org.mixer2.sample.dto;

import java.math.BigDecimal;

public class ItemDto implements java.io.Serializable {

    private static final long serialVersionUID = -7542865246382197538L;

    public long id;

    public String name;

    public String description;

    public BigDecimal price;

    public long categoryId;

    public String categoryName;

}
