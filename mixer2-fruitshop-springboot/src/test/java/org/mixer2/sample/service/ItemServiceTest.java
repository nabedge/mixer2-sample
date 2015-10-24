package org.mixer2.sample.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mixer2.sample.config.SpringConfig;
import org.mixer2.sample.dto.Item;
import org.mixer2.sample.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
@Transactional
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;
    
    @Test
    public void testGetItem() throws Exception {
        Item item = itemService.getItem(2L);
        assertThat(item.getName(), is("blueberry"));
    }
    
}
