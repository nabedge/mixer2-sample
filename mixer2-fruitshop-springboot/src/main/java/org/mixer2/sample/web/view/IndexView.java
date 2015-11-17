package org.mixer2.sample.web.view;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.H2;
import org.mixer2.jaxb.xhtml.H3;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.sample.dto.Category;
import org.mixer2.sample.dto.Item;
import org.mixer2.sample.web.util.RequestUtil;
import org.mixer2.sample.web.view.helper.SectionHelper;
import org.mixer2.spring.webmvc.AbstractMixer2XhtmlView;
import org.mixer2.xhtml.PathAdjuster;

public class IndexView extends AbstractMixer2XhtmlView {

    @Override
    protected Html renderHtml(Html html, Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) {

        // get data from model
        @SuppressWarnings("unchecked")
        List<Category> categoryList = (List<Category>) model.get("categoryList");
        @SuppressWarnings("unchecked")
        List<Item> oneItemByOneCategory = (List<Item>) model.get("oneItemByOneCategory");

        // embed category list on side bar
        SectionHelper.rewriteSideBar(html, categoryList);

        // embed category Box on content
        replaceCategoryBox(html, oneItemByOneCategory);

        // replace static file path
        Pattern pattern = Pattern.compile("^\\.+/.*m2static/(.*)$");
        String ctx = RequestUtil.getContextPath();
        PathAdjuster.replacePath(html, pattern, ctx + "/m2static/$1");

        // header,footer
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        return html;
    }

    /**
     * replace category list ul/li tags on sidebar by category table data.
     * 
     * @param html
     *            index page template data
     * @param categoryList
     * @throws TagTypeUnmatchException
     */
    private void replaceCategoryBox(Html html, List<Item> itemList) {
        // get contextPath
        String ctx = RequestUtil.getContextPath();

        // contet div
        Div contentDiv = html.getBody().getById("content", Div.class);

        // keep copy of CategoryBox
        Div categoryBox = contentDiv.getDescendants("categoryBox", Div.class).get(0).copy(Div.class);

        // remove all categoryBox
        contentDiv.removeDescendants("categoryBox", Div.class);

        // create categoryBox and add it to content.
        for (Item item : itemList) {

            String itemAnchor = ctx + "/item/" + item.getId();
            String itemListAnchor = ctx + "/itemList/" + item.getCategoryId();
            Div _categoryBox = categoryBox.copy(Div.class);

            // category name
            for (H2 h2 : _categoryBox.getDescendants("categoryName", H2.class)) {
                h2.unsetContent();
                h2.getContent().add(item.getCategoryName());
            }

            // item name and anchor
            for (H3 h3 : _categoryBox.getDescendants("itemNameLink", H3.class)) {
                h3.getDescendants(A.class).get(0).unsetContent();
                h3.getDescendants(A.class).get(0).getContent().add(item.getName());
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
            for (Span span : _categoryBox.getDescendants("itemDescription", Span.class)) {
                span.unsetContent();
                span.getContent().add(item.getDescription());
            }

            // add category box
            contentDiv.getContent().add(_categoryBox);
        }

    }
}
