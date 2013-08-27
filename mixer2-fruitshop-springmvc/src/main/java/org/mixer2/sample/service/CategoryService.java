package org.mixer2.sample.service;

import java.util.List;

import org.mixer2.sample.dto.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CategoryService {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private String sql = "select id, name from category order by id";

    public List<Category> getCategoryList() {
        List<Category> categoryList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Category.class));
        return categoryList;
    }
}
