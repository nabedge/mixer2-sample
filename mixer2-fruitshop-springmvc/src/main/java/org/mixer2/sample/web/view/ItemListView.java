package org.mixer2.sample.web.view;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.H1;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.jaxb.xhtml.Table;
import org.mixer2.jaxb.xhtml.Td;
import org.mixer2.jaxb.xhtml.Tr;
import org.mixer2.sample.dto.Category;
import org.mixer2.sample.dto.Item;
import org.mixer2.sample.web.util.RequestUtil;
import org.mixer2.sample.web.view.helper.SectionHelper;
import org.mixer2.springmvc.AbstractMixer2XhtmlView;
import org.mixer2.xhtml.PathAjuster;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ItemListView extends AbstractMixer2XhtmlView {

    @Autowired
    protected ResourceLoader resourceLoader;

    private String mainTemplate = "classpath:m2mockup/m2template/itemList.html";

    @Override
    protected Html createHtml(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // load html template
        Html html = getMixer2Engine().loadHtmlTemplate(
                resourceLoader.getResource(mainTemplate).getInputStream());

        // embed category list on side bar
        @SuppressWarnings("unchecked")
        List<Category> categoryList = (List<Category>) model
                .get("categoryList");
        SectionHelper.rewriteSideBar(html, categoryList);

        @SuppressWarnings("unchecked")
        List<Item> itemList = (List<Item>) model.get("itemList");

        // embed item box
        replaceItemBox(html, itemList);

        // replace static file path
        Pattern pattern = Pattern.compile("^\\.+/.*m2static/(.*)$");
        String ctx = RequestUtil.getContextPath();
        PathAjuster.replacePath(html, pattern, ctx + "/m2static/$1");

        // header,footer
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        return html;
    }

    private void replaceItemBox(Html html, List<Item> itemList)
            throws TagTypeUnmatchException {

        // get contextPath
        String ctx = RequestUtil.getContextPath();

        // contet div
        Div contentDiv = html.getBody().getById("content", Div.class);

        // set category name by H1
        String categoryName = itemList.get(0).getCategoryName();
        H1 categoryNameH1 = contentDiv.getById("categoryName", H1.class);
        categoryNameH1.unsetContent();
        categoryNameH1.getContent().add(categoryName);

        // keep copy of td of itemTable and clear other.
        Table itemTable = html.getBody().getById("content", Div.class)
                .getById("itemTable", Table.class);
        Td _td = itemTable.getTr().get(0).getThOrTd().get(0).cast(Td.class);
        itemTable.unsetTr(); // equals .getTr().clear()

        // embed item td in table
        int itemCount = 0;
        int trCount = 0;
        for (Item item : itemList) {
            // create td tag (copy)
            Td itemTd = _td.copy(Td.class);

            // anchor link to item page
            for (A a : itemTd.getDescendants("itemNameAnchorLink", A.class)) {
                a.setHref(ctx + "/item/" + item.getId());
                a.unsetContent();
                a.getContent().add(item.getName());
            }
            for (A a : itemTd.getDescendants("itemImageAnchorLink", A.class)) {
                a.setHref(ctx + "/item/" + item.getId());
            }

            // item price
            for (Span span : itemTd.getDescendants("itemPrice", Span.class)) {
                span.unsetContent();
                span.getContent().add(item.getPrice().toString());
            }

            // create 2 column tr/td tags
            if (itemCount % 2 == 0) {
                Tr tr = new Tr();
                tr.getThOrTd().add(itemTd);
                itemTable.getTr().add(tr);
            } else {
                itemTable.getTr().get(trCount).getThOrTd().add(itemTd);
                trCount++;
            }
            itemCount++;
        }
        if (itemList.size() % 2 != 0) {
            Td td = new Td(); // empty td tag
            itemTable.getTr().get(trCount).getThOrTd().add(td);
        }

    }
}
