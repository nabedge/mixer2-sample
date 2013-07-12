package org.mixer2.sample.web.view;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.Form;
import org.mixer2.jaxb.xhtml.H1;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.sample.dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
        "classpath:mvc-dispatcher-servlet.xml" })
public class ItemHelperTest {

    private String mainTemplate = "classpath:m2mockup/m2template/item.html";

    @Autowired
    private Mixer2Engine mixer2Engine;

    @Autowired
    private ResourceLoader resourceLoader;
    
    private String contextPath = "/test-ctx";

    @Test
    public void testReplaceItemBox() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath(contextPath);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(
                request));

        // item info for test.
        Item item = new Item();
        item.setName("product foo bar");
        item.setPrice(BigDecimal.valueOf(100));
        item.setDescription("description foo bar");

        // load html template
        Html html = mixer2Engine.loadHtmlTemplate(
        		resourceLoader.getResource(mainTemplate).getInputStream());

        // execute replace method !
        ItemHelper.replaceItemBox(html, item);

        // assert
        Div itemBox = html.getById("itemBox", Div.class);
        assertThat(
                itemBox.getById("itemName", H1.class)
                    .getContent().get(0).toString(),
                is("product foo bar"));
        assertThat(
                itemBox.getById("itemPrice", Span.class)
                    .getContent().get(0).toString(),
                is("100"));
        assertThat(
                itemBox.getById("itemDescription", Div.class)
                    .getContent().get(0).toString(),
                is("description foo bar"));
        Form addCartForm = itemBox.getById("addCartForm", Form.class);
        assertThat(
        		addCartForm.getAction(), 
        		is(contextPath + "/cart/add"));
    }
}
