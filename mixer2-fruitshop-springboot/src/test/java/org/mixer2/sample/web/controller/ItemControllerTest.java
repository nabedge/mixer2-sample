package org.mixer2.sample.web.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mixer2.sample.config.SpringConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
@Transactional
public class ItemControllerTest {

    @Autowired
    private ItemController itemController;
    
    @Test
    public void testShowItem() throws Exception {
        ExtendedModelMap model = new ExtendedModelMap();
        String viewName = itemController.showItem(2L, model);
        assertThat(viewName, is("item"));
    }
    
}
