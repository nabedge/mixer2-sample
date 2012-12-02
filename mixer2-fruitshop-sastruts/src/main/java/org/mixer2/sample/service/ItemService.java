package org.mixer2.sample.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.mixer2.sample.dto.CategoryDto;
import org.mixer2.sample.dto.ItemDto;
import org.seasar.extension.jdbc.JdbcManager;

public class ItemService {

    @Resource
    JdbcManager jdbcManager;

    @Resource
    CategoryService categoryService;

    private String sql_item = "select"
            + " i.id, i.name, i.price, i.description, c.name as category_name"
            + " from item i, category c"
            + " where i.category_id=c.id and i.id = ?";

    private String sql_oneItemByOneCategory = ""//
            + "select i.id, i.name, i.description, c.id as category_id, c.name as category_name"
            + " from item i, category c"
            + " where i.category_id=c.id and i.category_id = ? order by i.id limit 1";

    private String sql_itemlist = "select"
            + " i.id, i.name, i.price, i.description, c.name as category_name"
            + " from item i, category c"
            + " where i.category_id=c.id and i.category_id = ?"
            + " order by i.id";

    public ItemDto getItem(long itemId) {
        ItemDto item = jdbcManager.selectBySql(ItemDto.class, sql_item, itemId)
                .disallowNoResult().getSingleResult();
        return item;
    }

    public List<ItemDto> getItemList(long categoryId) {
        return jdbcManager.selectBySql(ItemDto.class, sql_itemlist, categoryId)
                .disallowNoResult().getResultList();
    }

    public List<ItemDto> getOneItemByOneCategory() {
        ArrayList<ItemDto> itemList = new ArrayList<ItemDto>();
        for (CategoryDto category : categoryService.getCategoryList()) {
            ItemDto item = jdbcManager.selectBySql(ItemDto.class,
                    sql_oneItemByOneCategory, category.id).getSingleResult();
            itemList.add(item);
        }
        return itemList;
    }
}
