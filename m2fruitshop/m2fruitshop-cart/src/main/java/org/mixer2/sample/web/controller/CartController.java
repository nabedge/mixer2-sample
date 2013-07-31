package org.mixer2.sample.web.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.mixer2.sample.dto.Item;
import org.mixer2.sample.service.ItemService;
import org.mixer2.sample.web.dto.Cart;
import org.mixer2.sample.web.dto.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/cart")
@SessionAttributes(value = { "cart" })
public class CartController {

    private Logger logger = Logger.getLogger(CartController.class);

    @Autowired
    protected ItemService itemService;

    @Autowired
    protected ResourceLoader resourceLoader;

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
    public String view(Model model, Cart cart) {
        model.addAttribute("cart", cart);
        return "cartView";
    }
}
