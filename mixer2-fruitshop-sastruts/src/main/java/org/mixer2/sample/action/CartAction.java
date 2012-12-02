package org.mixer2.sample.action;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.dto.CartDto;
import org.mixer2.sample.dto.CartItemDto;
import org.mixer2.sample.dto.ItemDto;
import org.mixer2.sample.form.CartForm;
import org.mixer2.sample.service.ItemService;
import org.mixer2.sample.view.CartHelper;
import org.mixer2.sample.view.M2staticHelper;
import org.mixer2.sample.view.SectionHelper;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

public class CartAction {

    private static Logger logger = Logger.getLogger(CartAction.class);

    public String htmlString;

    @Resource
    protected Mixer2Engine mixer2Engine;

    @Resource
    protected ItemService itemService;

    @ActionForm
    @Resource
    protected CartForm cartForm;

    /** cart object in session */
    @Resource
    protected CartDto cartDto;

    private String mainTemplate = "m2mockup/m2template/cart.html";

    /**
     * checkout start. reset amount of cart items and go to shipping information input page.
     */
    @Execute(validator = false)
    public String checkout() {
        // list of items in cart
        List<CartItemDto> itemList = cartDto.getReadOnlyItemList();
        // amount which user input on form
        String[] amountArray = cartForm.amountArray;
        // reset amount of cart items.
        for (int i = 0; i < amountArray.length; i++) {
            CartItemDto cartItemDto = itemList.get(i);
            cartDto.setItem(cartItemDto.itemDto, Integer
                    .parseInt(amountArray[i]));
        }
        return "/checkout/shipping?redirect=true";
    }

    /**
     * add item to cart. If validation error, just redirect to cart view.
     */
    @Execute(validator = true, input = "/cart/view?redirect=true")
    public String add() {
        ItemDto item = itemService.getItem(Long.parseLong(cartForm.itemId));
        cartDto.setItem(item, Integer.parseInt(cartForm.amount));
        return "/cart/view?redirect=true";
    }

    /**
     * cart view
     */
    @Execute(validator = false)
    public String view() throws IOException, TagTypeUnmatchException {

        // load html template
        File file = ResourceUtil.getResourceAsFile(mainTemplate);
        Html html = mixer2Engine.loadHtmlTemplate(file);

        // get the list of items in cart
        List<CartItemDto> itemList = cartDto.getReadOnlyItemList();

        // remove <form>... tag or <div> empty cart... tag.
        if (itemList.size() < 1) {
            html.getBody().removeById("cartForm");
        } else {
            html.getBody().removeById("emptyCart");
            CartHelper.replaceCartForm(html, itemList);
        }

        if (logger.isDebugEnabled() && cartForm.amountArray != null) {
            logger.debug("######## cartForm.amountArray"
                    + cartForm.amountArray.toString());
        }

        // replace static file path
        M2staticHelper.replaceM2staticPath(html);
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);
        htmlString = mixer2Engine.saveToString(html);
        return "/mixer2view.jsp";
    }

}
