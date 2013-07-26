package org.mixer2.sample.web.view;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.H1;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.sample.dto.Category;
import org.mixer2.sample.dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
        "classpath:mvc-dispatcher-servlet.xml" })
public class ItemViewTest {

    @Autowired
    protected Mixer2Engine mixer2Engine;

    @Autowired
    protected ItemView itemView;

    @Test
    public void testCreateHtml() throws Exception {

        Item item = new Item();
        item.setId(999);
        item.setName("name999");
        item.setPrice(new BigDecimal(999));
        item.setDescription("description999");

        Category category = new Category();
        category.setId(9);
        category.setName("category9");
        List<Category> categoryList = new ArrayList<Category>();

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("item", item);
        model.put("categoryList", categoryList);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(
                request));

        itemView.setMixer2Engine(mixer2Engine);
        Html html = itemView.createHtml(model, request, response);
        Div itemDiv = html.getById("itemBox", Div.class);

        assertThat(itemDiv.getById("itemName", H1.class).getContent().get(0)
                .toString(), is("name999"));
        assertThat(itemDiv.getById("itemPrice", Span.class).getContent().get(0)
                .toString(), is("999"));
        assertThat(itemDiv.getById("itemDescription", Div.class).getContent()
                .get(0).toString(), is("description999"));
    }

}
