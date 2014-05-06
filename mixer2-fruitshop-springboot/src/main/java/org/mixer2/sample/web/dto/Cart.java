package org.mixer2.sample.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.mixer2.sample.dto.Item;

public class Cart implements Serializable {

    private static final long serialVersionUID = 667380357803679157L;

    private List<CartItem> itemList;

    public Cart() {
        this.itemList = Collections
                .synchronizedList(new ArrayList<CartItem>());
    }

    public List<CartItem> getReadOnlyItemList() {
        return Collections.unmodifiableList(this.itemList);
    }

    public void removeAll() {
        this.itemList.clear();
    }

    public void setItem(Item item, int amount) {
        // remove if amount == 0
        if (amount < 1) {
            Iterator<CartItem> ite = this.itemList.iterator();
            while (ite.hasNext()) {
                CartItem cartItem = ite.next();
                if (cartItem.getItem().getId() == item.getId()) {
                    ite.remove();
                }
            }
            return;
        }
        // search cart
        for (CartItem cartItem : this.itemList) {
            if (cartItem.getItem().getId() == item.getId()) {
                cartItem.setAmount(amount);
                return;
            }
        }
        // if not exists on cart, add item.
        CartItem cartItem = new CartItem();
        cartItem.setItem(item);
        cartItem.setAmount(amount);
        this.itemList.add(cartItem);
    }
}
