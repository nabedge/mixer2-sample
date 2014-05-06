package org.mixer2.sample.web.dto;

import lombok.Data;

import org.mixer2.sample.dto.Item;

@Data
public class CartItem implements java.io.Serializable {

    private static final long serialVersionUID = 7055119781001205010L;

    private Item item;

    private int amount;

}
