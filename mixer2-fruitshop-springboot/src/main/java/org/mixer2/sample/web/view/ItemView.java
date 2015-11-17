package org.mixer2.sample.web.view;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.Form;
import org.mixer2.jaxb.xhtml.H1;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Input;
import org.mixer2.jaxb.xhtml.InputType;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.sample.dto.Category;
import org.mixer2.sample.dto.Item;
import org.mixer2.sample.web.util.RequestUtil;
import org.mixer2.sample.web.view.helper.SectionHelper;
import org.mixer2.spring.webmvc.AbstractMixer2XhtmlView;
import org.mixer2.xhtml.PathAdjuster;

public class ItemView extends AbstractMixer2XhtmlView {

    @Override
    protected Html renderHtml(Html html, Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) {

        @SuppressWarnings("unchecked")
        List<Category> categoryList = (List<Category>) model.get("categoryList");
        Item item = (Item) model.get("item");

        // embed item box
        replaceItemBox(html, item);

        // embed category list on side bar
        SectionHelper.rewriteSideBar(html, categoryList);

        // replace static file path
        Pattern pattern = Pattern.compile("^\\.+/.*m2static/(.*)$");
        String ctx = request.getContextPath();
        PathAdjuster.replacePath(html, pattern, ctx + "/m2static/$1");

        // header,footer
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        return html;
    }

    /**
     * replace tags on template by item data.
     * 
     * @param html
     *            item detail page template.
     * @param item
     */
    private static void replaceItemBox(Html html, Item item) {

        // get contextPath
        String ctx = RequestUtil.getContextPath();
        // contet div
        Div itemBox = html.getBody().getById("itemBox", Div.class);

        // item information
        itemBox.getById("itemName", H1.class).unsetContent();
        itemBox.getById("itemName", H1.class).getContent().add(item.getName());
        itemBox.getById("itemPrice", Span.class).unsetContent();
        itemBox.getById("itemPrice", Span.class).getContent().add(item.getPrice().toString());
        itemBox.getById("itemDescription", Div.class).unsetContent();
        itemBox.getById("itemDescription", Div.class).getContent().add(item.getDescription());

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
