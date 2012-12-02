package org.mixer2.sample.view;

import java.util.List;

import org.apache.log4j.Logger;
import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Li;
import org.mixer2.jaxb.xhtml.Ul;
import org.mixer2.sample.dto.CategoryDto;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.seasar.struts.util.RequestUtil;

/**
 * Helper class for handle sidebar of html template.
 */
public class SectionHelper {

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(SectionHelper.class);

    public static void rewriteHeader(Html html) throws TagTypeUnmatchException {

        // get contextPath
        String ctx = RequestUtil.getRequest().getContextPath();

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
    public static void rewriteSideBar(Html html, List<CategoryDto> categoryList) throws TagTypeUnmatchException {

        Div sideBar = html.getBody().getById("sidebar", Div.class);
        // get category list ul
        Ul ul = sideBar.getById("categoryList", Ul.class);

        // keep copy of first li and clear of the content.
        Li li = ul.getLi().get(0).copy(Li.class);
        li.getContent().clear();

        // clear li tags of ul.
        ul.getLi().clear();

        // get context path.
        String ctx = RequestUtil.getRequest().getContextPath();

        // add new li tags contains category name and anchor link
        for (CategoryDto category : categoryList) {

            Li _li = li.copy(Li.class);

            // create anchor link
            A a = new A();
            a.setHref(ctx + "/itemList/" + Long.toString(category.id));
            a.getContent().add(category.name);
            _li.getContent().add(a);

            //
            ul.getLi().add(_li);
        }

        //
        sideBar.getById("viewCartAnchorLink", A.class).setHref(
                ctx + "/cart/view");

    }

}
