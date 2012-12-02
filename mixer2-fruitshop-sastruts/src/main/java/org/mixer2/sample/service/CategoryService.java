package org.mixer2.sample.service;

import java.util.List;

import javax.annotation.Resource;

import org.mixer2.sample.dto.CategoryDto;
import org.seasar.extension.jdbc.JdbcManager;

public class CategoryService {

    @Resource
    JdbcManager jdbcManger;

    String sql = "select id, name from category order by id";

    public List<CategoryDto> getCategoryList() {
        return jdbcManger.selectBySql(CategoryDto.class, sql)
                .disallowNoResult().getResultList();
    }
}
