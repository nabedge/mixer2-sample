package org.mixer2.sample.web.view;

import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.Form;
import org.mixer2.jaxb.xhtml.H1;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Input;
import org.mixer2.jaxb.xhtml.InputType;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.sample.dto.Item;
import org.mixer2.sample.web.util.RequestUtil;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;

public class ItemHelper {

    /**
     * replace tags on template by item data.
     * @param html item detail page template.
     * @param item
     * @throws TagTypeUnmatchException
     */
    public static void replaceItemBox(Html html, Item item) throws TagTypeUnmatchException {

        // get contextPath
        String ctx = RequestUtil.getContextPath();
        // contet div
        Div itemBox = html.getBody().getById("itemBox", Div.class);

        // item information
        itemBox.getById("itemName", H1.class).getContent().clear();
        itemBox.getById("itemName", H1.class).getContent().add(item.getName());
        itemBox.getById("itemPrice", Span.class).getContent().clear();
        itemBox.getById("itemPrice", Span.class).getContent().add(
                item.getPrice().toString());
        itemBox.getById("itemDescription", Div.class).getContent().clear();
        itemBox.getById("itemDescription", Div.class).getContent().add(
                item.getDescription());

        // addCart form
        Form addCartForm = itemBox.getById("addCartForm", Form.class);
        addCartForm.setAction(ctx + "/cart/add");
        Input input = new Input();
        input.setType(InputType.HIDDEN);
        input.setName("itemId");
        input.setValue(Long.toString(item.getId()));
        addCartForm.getContent().add(input);

    }
}
