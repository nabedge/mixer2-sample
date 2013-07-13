package org.mixer2.sample.service;

import java.util.ArrayList;
import java.util.List;

import org.mixer2.sample.dto.Item;

public interface ItemService {

    public Item getItem(long itemId);

    public List<Item> getItemList(long categoryId);

    public ArrayList<Item> getOneItemByOneCategory();

}
