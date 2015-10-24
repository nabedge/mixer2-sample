package org.mixer2.sample.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mixer2.sample.config.SpringConfig;
import org.mixer2.sample.dto.Category;
import org.mixer2.sample.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
@Transactional
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    
    @Test
    public void testGetCategoryList() throws Exception {
        List<Category> list = categoryService.getCategoryList();
        assertThat(list.get(0).getName(), is("berry"));
    }
    
}
