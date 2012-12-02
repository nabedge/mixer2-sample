package org.mixer2.sample.view;

import java.util.List;

import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.H2;
import org.mixer2.jaxb.xhtml.H3;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.sample.dto.ItemDto;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.seasar.struts.util.RequestUtil;

/**
 * Helper class for handle sidebar of html template.
 */
public class IndexHelper {

    /**
     * replace category list ul/li tags on sidebar by category table data.
     * @param html index page template data
     * @param categoryList
     * @throws TagTypeUnmatchException
     */
    public static void replaceCategoryBox(Html html, List<ItemDto> itemList) throws TagTypeUnmatchException {
        // get contextPath
        String ctx = RequestUtil.getRequest().getContextPath();

        // contet div
        Div contentDiv = html.getBody().getById("content", Div.class);

        // keep copy of CategoryBox
        Div categoryBox = contentDiv.getDescendants("categoryBox", Div.class)
                .get(0).copy(Div.class);

        // remove all categoryBox
        contentDiv.removeDescendants("categoryBox", Div.class);

        // create categoryBox and add it to content.
        for (ItemDto item : itemList) {

            String itemAnchor = ctx + "/item/" + item.id;
            String itemListAnchor = ctx + "/itemList/" + item.categoryId;
            Div _categoryBox = categoryBox.copy(Div.class);

            // category name
            for (H2 h2 : _categoryBox.getDescendants("categoryName", H2.class)) {
                h2.getContent().clear();
                h2.getContent().add(item.categoryName);
            }

            // item name and anchor
            for (H3 h3 : _categoryBox.getDescendants("itemNameLink", H3.class)) {
                h3.getDescendants(A.class).get(0).getContent().clear();
                h3.getDescendants(A.class).get(0).getContent().add(item.name);
                h3.getDescendants(A.class).get(0).setHref(itemAnchor);
            }

            // item image and anchor
            for (A a : _categoryBox.getDescendants("itemImageLink", A.class)) {
                a.setHref(itemAnchor);
            }

            // category list link
            for (A a : _categoryBox.getDescendants("itemListLink", A.class)) {
                a.setHref(itemListAnchor);
            }

            // item description
            for (Span span : _categoryBox.getDescendants("itemDescription",
                    Span.class)) {
                span.getContent().clear();
                span.getContent().add(item.description);
            }

            // add category box
            contentDiv.getContent().add(_categoryBox);
        }

    }

}
