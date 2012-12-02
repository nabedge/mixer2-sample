package org.mixer2.sample.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(instance = InstanceType.SESSION)
public class CartDto implements Serializable {

    private static final long serialVersionUID = 8669223579749264098L;

    private List<CartItemDto> itemList;

    public CartDto() {
        this.itemList = Collections
                .synchronizedList(new ArrayList<CartItemDto>());
    }

    public List<CartItemDto> getReadOnlyItemList() {
        return Collections.unmodifiableList(this.itemList);
    }

    public void removeAll() {
        this.itemList.clear();
    }

    public void setItem(ItemDto itemDto, int amount) {
        // remove if amount == 0
        if (amount < 1) {
            Iterator<CartItemDto> ite = this.itemList.iterator();
            while (ite.hasNext()) {
                CartItemDto dto = ite.next();
                if (dto.itemDto.id == itemDto.id) {
                    ite.remove();
                }
            }
            return;
        }
        // search cart
        for (CartItemDto cartItem : this.itemList) {
            if (cartItem.itemDto.id == itemDto.id) {
                cartItem.amount = amount;
                return;
            }
        }
        // if not exists on cart, add item.
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.itemDto = itemDto;
        cartItemDto.amount = amount;
        this.itemList.add(cartItemDto);
    }

}
