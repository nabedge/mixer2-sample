package org.mixer2.sample.view;

import java.util.List;

import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.H1;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.jaxb.xhtml.Table;
import org.mixer2.jaxb.xhtml.Td;
import org.mixer2.jaxb.xhtml.Tr;
import org.mixer2.sample.dto.ItemDto;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.seasar.struts.util.RequestUtil;

public class ItemListHelper {

    /**
     * create category page.
     * @param html item list (category) page template
     * @param itemList
     * @throws TagTypeUnmatchException
     */
    public static void replaceCategoryBox(Html html, List<ItemDto> itemList) throws TagTypeUnmatchException {

        // get contextPath
        String ctx = RequestUtil.getRequest().getContextPath();

        // contet div
        Div contentDiv = html.getBody().getById("content", Div.class);

        // set category name by H1
        String categoryName = itemList.get(0).categoryName;
        H1 categoryNameH1 = contentDiv.getById("categoryName", H1.class);
        categoryNameH1.getContent().clear();
        categoryNameH1.getContent().add(categoryName);

        // keep copy of td of itemTable and clear other.
        Table itemTable = html.getBody().getById("content", Div.class).getById(
                "itemTable", Table.class);
        Td _td = itemTable.getTr().get(0).getThOrTd().get(0).cast(Td.class);
        itemTable.getTr().clear();

        // embed item td in table
        int itemCount = 0;
        int trCount = 0;
        for (ItemDto item : itemList) {
            // create td tag (copy)
            Td itemTd = _td.copy(Td.class);

            // anchor link to item page
            for (A a : itemTd.getDescendants("itemNameAnchorLink", A.class)) {
                a.setHref(ctx + "/item/" + item.id);
                a.getContent().clear();
                a.getContent().add(item.name);
            }
            for (A a : itemTd.getDescendants("itemImageAnchorLink", A.class)) {
                a.setHref(ctx + "/item/" + item.id);
            }

            // item price
            for (Span span : itemTd.getDescendants("itemPrice", Span.class)) {
                span.getContent().clear();
                span.getContent().add(item.price.toString());
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
