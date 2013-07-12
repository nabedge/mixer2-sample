package org.mixer2.sample.web.view;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.Form;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Input;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.jaxb.xhtml.Tbody;
import org.mixer2.sample.dto.Item;
import org.mixer2.sample.web.dto.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml",
        "classpath:mvc-dispatcher-servlet.xml" })
public class CartHelperTest {

    private String mainTemplate = "classpath:m2mockup/m2template/cart.html";

    @Autowired
    private Mixer2Engine mixer2Engine;

    @Autowired
    private ResourceLoader resourceLoader;
    
    @Test
    public void emptyCartTest() throws Exception {

    	// create empty cart.
        Cart cart = new Cart();
        
        // load html template
        Html html = mixer2Engine.loadHtmlTemplate(
                resourceLoader.getResource(mainTemplate).getInputStream());

        // execute replace method !
        CartHelper.replaceCartForm(html, cart);

        // assert no form tag.
        assertThat(html.getById("cartForm", Form.class), is(nullValue()));

        // assert empty message.
        assertThat(html.getById("emptyCart", Div.class), is(notNullValue()));
    }
    
    @Test
    public void oneItemCartTest() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder
                .setRequestAttributes(new ServletRequestAttributes(request));

        // cart has two "foo" item. 
        Cart cart = new Cart();
        Item item = new Item();
        item.setName("foo");
        item.setPrice(BigDecimal.valueOf(100));
        cart.setItem(item, 2);
        
        // load html template
        Html html = mixer2Engine.loadHtmlTemplate(
                resourceLoader.getResource(mainTemplate).getInputStream());
        CartHelper.replaceCartForm(html, cart);
        
        // not exists empty message.
        assertThat(html.getById("emptyCart", Div.class), is(nullValue()));
        
        // assert existence of cart form tag
        assertThat(html.getById("cartForm", Form.class), is(notNullValue()));

        // assert cart table has one item.
        Tbody tbody = html.getById("cartTbody", Tbody.class);
        assertThat(tbody.getTr().size(), is(1));
        
        // assert item name span tag.
        Span itemNameSpan = tbody.getDescendants("itemName", Span.class).get(0);
        String itemName = itemNameSpan.getContent().get(0).toString();
        assertThat(itemName, is("foo"));
        
        // assert item price span tag.
        Span itemPriceSpan = tbody.getDescendants("itemPrice", Span.class).get(0);
        String itemPrice = itemPriceSpan.getContent().get(0).toString();
        assertThat(itemPrice, is("100"));
        
        // assert item amount input tag.
        Input itemAmountInput = tbody.getDescendants("itemAmount", Input.class).get(0);
        assertThat(itemAmountInput.getValue(), is("2"));
    }
}
