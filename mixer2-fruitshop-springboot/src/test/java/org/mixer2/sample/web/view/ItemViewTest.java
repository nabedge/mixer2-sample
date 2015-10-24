package org.mixer2.sample.web.view;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.H1;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.config.SpringConfig;
import org.mixer2.sample.dto.Category;
import org.mixer2.sample.dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfig.class })
@Transactional
public class ItemViewTest {

    @Autowired
    private Mixer2Engine m2e;

    private String template = "classpath:m2mockup/m2template/item.html";

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void testItemView() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ExtendedModelMap model = new ExtendedModelMap();

        // category list
        Category c1 = new Category();
        c1.setId(1);
        c1.setName("foo");
        Category c2 = new Category();
        c2.setId(2);
        c2.setName("bar");
        model.put("categoryList", Arrays.asList(c1, c2));

        // item
        Item item = new Item();
        item.setCategoryId(c1.getId());
        item.setCategoryName(c1.getName());
        item.setId(1);
        item.setName("foo item");
        item.setPrice(new BigDecimal(999));
        item.setDescription("foo description");
        model.put("item", item);

        // template
        InputStream is = resourceLoader.getResource(template).getInputStream();
        Html html = m2e.loadHtmlTemplate(is);

        // render!
        ItemView view = new ItemView();
        Html renderedHtml = view.renderHtml(html, model, request, response);

        // assert
        String itemName = renderedHtml.getById("itemName", H1.class).getContent().get(0).toString();
        assertThat(itemName, is(item.getName()));
    }

}
