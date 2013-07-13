package org.mixer2.sample.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mixer2.sample.dto.Category;
import org.mixer2.sample.dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ItemServiceImpl implements ItemService {

    @Autowired
    protected CategoryService categoryService;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private String sql_item = "select"
            + " i.id, i.name, i.price, i.description, c.name as category_name"
            + " from item i, category c"
            + " where i.category_id=c.id and i.id = ?";

    private String sql_oneItemByOneCategory = "select"
            + " i.id, i.name, i.description, c.id as category_id, c.name as category_name"
            + " from item i, category c"
            + " where i.category_id=c.id and i.category_id = ? order by i.id limit 1";

    private String sql_itemlist = "select"
            + " i.id, i.name, i.price, i.description, c.name as category_name"
            + " from item i, category c"
            + " where i.category_id=c.id and i.category_id = ?"
            + " order by i.id";

    public Item getItem(long itemId) {
        Item itemDto = jdbcTemplate.queryForObject(sql_item,
                new RowMapper<Item>() {
                    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Item dto = new Item();
                        dto.setId(rs.getLong("id"));
                        dto.setName(rs.getString("name"));
                        dto.setPrice(rs.getBigDecimal("price"));
                        dto.setCategoryName(rs.getString("category_name"));
                        return dto;
                    }
                }, itemId);
        return itemDto;
    }

    public List<Item> getItemList(long categoryId) {
        List<Item> itemList = jdbcTemplate.query(sql_itemlist,
                new RowMapper<Item>() {
                    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Item dto = new Item();
                        dto.setId(rs.getLong("id"));
                        dto.setName(rs.getString("name"));
                        dto.setPrice(rs.getBigDecimal("price"));
                        dto.setDescription(rs.getString("description"));
                        dto.setCategoryName(rs.getString("category_name"));
                        return dto;
                    }
                }, categoryId);
        return itemList;
    }

    public ArrayList<Item> getOneItemByOneCategory() {
        ArrayList<Item> itemList = new ArrayList<Item>();
        for (Category category : categoryService.getCategoryList()) {
            Item item = jdbcTemplate.queryForObject(
                    sql_oneItemByOneCategory, new RowMapper<Item>() {
                        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Item dto = new Item();
                            dto.setId(rs.getLong("id"));
                            dto.setName(rs.getString("name"));
                            dto.setCategoryId(rs.getLong("category_id"));
                            dto.setCategoryName(rs.getString("category_name"));
                            dto.setDescription(rs.getString("description"));
                            return dto;
                        }
                    }, category.getId());
            itemList.add(item);
        }
        return itemList;
    }

}
