package org.mixer2.sample.web.view.helper;

import java.util.List;

import org.apache.log4j.Logger;
import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Li;
import org.mixer2.jaxb.xhtml.Ul;
import org.mixer2.sample.dto.Category;
import org.mixer2.sample.web.util.RequestUtil;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;

/**
 * Helper class for handle sidebar of html template.
 */
public class SectionHelper {

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(SectionHelper.class);

    public static void rewriteHeader(Html html) throws TagTypeUnmatchException {

        String ctx = RequestUtil.getContextPath();

        Div header = html.getBody().getById("header", Div.class);

        for (A a : header.getDescendants("topPageAnchor", A.class)) {
            a.setHref(ctx + "/");
        }

    }

    public static void rewiteFooter(Html html) throws TagTypeUnmatchException {
        // TODO
    }

    /**
     * rewrite side bar. replace category list ul/li tags on sidebar by category table data. and link to view cart
     * @param html
     * @param categoryList
     * @throws TagTypeUnmatchException
     */
    public static void rewriteSideBar(Html html, List<Category> categoryList) throws TagTypeUnmatchException {

        Div sideBar = html.getBody().getById("sidebar", Div.class);
        // get category list ul
        Ul ul = sideBar.getById("categoryList", Ul.class);

        // keep copy of first li and clear of the content.
        Li li = ul.getLi().get(0).copy(Li.class);
        li.getContent().clear();

        // clear li tags of ul.
        ul.getLi().clear();

        // get context path.
        String contextPath = RequestUtil.getContextPath();

        // add new li tags contains category name and anchor link
        for (Category category : categoryList) {

            Li _li = li.copy(Li.class);

            // create anchor link
            A a = new A();
            a.setHref(contextPath + "/itemList/"
                    + Long.toString(category.getId()));
            a.getContent().add(category.getName());
            _li.getContent().add(a);

            //
            ul.getLi().add(_li);
        }

        //
        sideBar.getById("viewCartAnchorLink", A.class).setHref(
                contextPath + "/cart/view");

    }

}
