package org.mixer2.sample.view;

import java.util.List;

import org.mixer2.jaxb.xhtml.Form;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Input;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.jaxb.xhtml.Table;
import org.mixer2.jaxb.xhtml.Tbody;
import org.mixer2.jaxb.xhtml.Tr;
import org.mixer2.sample.dto.CartItemDto;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.seasar.struts.util.RequestUtil;

public class CartHelper {

    /**
     * replace table on template by item data.
     * @param html cart view page template data
     * @param itemList
     * @throws TagTypeUnmatchException
     */
    public static void replaceCartForm(Html html, List<CartItemDto> itemList) throws TagTypeUnmatchException {

        // get contextPath
        String ctx = RequestUtil.getRequest().getContextPath();

        // keep copy of first tr tag
        Tbody cartTbody = html.getBody().getById("cartTable", Table.class)
                .getById("cartTbody", Tbody.class);
        Tr baseTr = cartTbody.getTr().get(0).copy(Tr.class);
        cartTbody.getTr().clear();

        // replace attribute of form tag
        html.getBody().getById("cartForm", Form.class).setMethod("post");
        html.getBody().getById("cartForm", Form.class).setAction(
                ctx + "/cart/checkout");

        // embed tr tag by item data
        int i = -1;
        for (CartItemDto cartItem : itemList) {
            i++;

            // create tr from copy
            Tr tr = baseTr.copy(Tr.class);

            // item name
            Span itemNameSpan = new Span();
            itemNameSpan.getContent().add(cartItem.itemDto.name);
            itemNameSpan.addCssClass("itenName");
            tr.replaceDescendants("itemName", Span.class, itemNameSpan);

            // item price
            Span itemPriceSpan = new Span();
            itemPriceSpan.getContent().add(cartItem.itemDto.price.toString());
            itemPriceSpan.addCssClass("itemPrice");
            tr.replaceDescendants("itemPrice", Span.class, itemPriceSpan);

            // item amount
            for (Input input : tr.getDescendants("itemAmount", Input.class)) {
                input.setName("amountArray[" + i + "]");
                input.setValue(Integer.toString(cartItem.amount));
            }

            // add tr tag to tbody tag.
            cartTbody.getTr().add(tr);
        }

    }
}
