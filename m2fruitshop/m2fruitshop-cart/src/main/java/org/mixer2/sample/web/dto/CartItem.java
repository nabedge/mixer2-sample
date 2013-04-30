package org.mixer2.sample.web.dto;

import org.mixer2.sample.dto.Item;

public class CartItem implements java.io.Serializable {

    private static final long serialVersionUID = 7055119781001205010L;

    private Item item;

    private int amount;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
