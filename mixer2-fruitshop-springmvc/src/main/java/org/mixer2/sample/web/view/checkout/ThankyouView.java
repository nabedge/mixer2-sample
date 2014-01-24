package org.mixer2.sample.web.view.checkout;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.web.util.RequestUtil;
import org.mixer2.sample.web.view.helper.SectionHelper;
import org.mixer2.spring.webmvc.AbstractMixer2XhtmlView;
import org.mixer2.xhtml.PathAdjuster;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;

public class ThankyouView extends AbstractMixer2XhtmlView {

    @Override
    protected Html renderHtml(Html html, Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws TagTypeUnmatchException {

        // replace anchor link to top page
        String ctx = RequestUtil.getContextPath();
        html.getBody().getById("goToTopPageAnchorLink", A.class).setHref(ctx);

        // replace static file path
        Pattern pattern = Pattern.compile("^\\.+/.*m2static/(.*)$");
        PathAdjuster.replacePath(html, pattern, ctx + "/m2static/$1");
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);
        return html;
    }

}
