package org.mixer2.sample.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.dto.Item;
import org.mixer2.sample.service.ItemService;
import org.mixer2.sample.web.dto.Cart;
import org.mixer2.sample.web.dto.CartItem;
import org.mixer2.sample.web.view.CartHelper;
import org.mixer2.sample.web.view.M2staticHelper;
import org.mixer2.sample.web.view.SectionHelper;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cart")
@SessionAttributes(value = { "cart" })
public class CartController {

    private Logger logger = Logger.getLogger(CartController.class);

    @Autowired
    protected ItemService itemService;

    @Autowired
    protected Mixer2Engine mixer2Engine;

    private String mainTemplate = "classpath:m2mockup/m2template/cart.html";

    @ModelAttribute("cart")
    public Cart createCart() {
        logger.debug("#### create cart object...");
        return new Cart();
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    public String checkout(Cart cart, @RequestParam("amountArray") Integer[] amountArray) {
        logger.info("##### checkout method going...");
        List<CartItem> itemList = cart.getReadOnlyItemList();
        // amount which user input on form
        for (int i = 0; i < amountArray.length; i++) {
            CartItem cartItem = itemList.get(i);
            cart.setItem(cartItem.getItem(), amountArray[i]);
        }
        return "redirect:/checkout/shipping";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Cart cart, @RequestParam("itemId") long itemId,
            @RequestParam("amount") int amount) {

        Item item = itemService.getItem(itemId);
        cart.setItem(item, amount);
        logger.debug("### add to cart: itemId=" + itemId + " amount=" + amount);
        return "redirect:/cart/view";
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView view(Cart cart) throws IOException, TagTypeUnmatchException {

        // load html template
        File file = ResourceUtils.getFile(mainTemplate);
        Html html = mixer2Engine.loadHtmlTemplate(file);

        // fill cart table
        List<CartItem> list = cart.getReadOnlyItemList();
        if (list.size() > 0) {
            html.getBody().removeById("emptyCart");
            CartHelper.replaceCartForm(html, list);
        } else {
            html.getBody().removeById("cartForm");
        }

        // replace static file path
        M2staticHelper.replaceM2staticPath(html);

        // header,footer
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        ModelAndView modelAndView = new ModelAndView("mixer2view", "htmlString", mixer2Engine
                .saveToString(html));
        return modelAndView;
    }

}
