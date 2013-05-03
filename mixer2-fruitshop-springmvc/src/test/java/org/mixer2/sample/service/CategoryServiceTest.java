package org.mixer2.sample.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mixer2.sample.dto.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
        "classpath:mvc-dispatcher-servlet.xml" })
public class CategoryServiceTest {

    private Logger logger = Logger.getLogger(CategoryServiceTest.class);

    @Autowired
    private CategoryService categoryService;

    @Test
    public void test() {
        List<Category> list = categoryService.getCategoryList();
        // TODO
        logger.debug(list.toString());
    }

}
