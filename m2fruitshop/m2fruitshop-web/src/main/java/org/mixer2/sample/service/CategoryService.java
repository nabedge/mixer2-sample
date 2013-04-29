package org.mixer2.sample.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mixer2.sample.dto.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryService {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private String sql = "select id, name from category order by id";

    public List<Category> getCategoryList() {
        List<Category> categoryList = jdbcTemplate.query(sql,
                new RowMapper<Category>() {
                    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Category dto = new Category();
                        dto.setId(rs.getLong("id"));
                        dto.setName(rs.getString("name"));
                        return dto;
                    }
                });
        return categoryList;
    }
}
