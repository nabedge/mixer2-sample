package org.mixer2.sample.web.view;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.web.util.RequestUtil;
import org.mixer2.sample.web.view.helper.SectionHelper;
import org.mixer2.springmvc.AbstractMixer2XhtmlView;
import org.mixer2.xhtml.PathAjuster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ThankyouView extends AbstractMixer2XhtmlView {

    @Autowired
    protected ResourceLoader resourceLoader;

    @Override
    protected Html createHtml(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String template = "classpath:m2mockup/m2template/checkout/thankyou.html";
        Html html = getMixer2Engine().loadHtmlTemplate(
                resourceLoader.getResource(template).getInputStream());

        // replace anchor link to top page
        String ctx = RequestUtil.getContextPath();
        html.getBody().getById("goToTopPageAnchorLink", A.class).setHref(ctx);

        // replace static file path
        Pattern pattern = Pattern.compile("^\\.+/.*m2static/(.*)$");
        PathAjuster.replacePath(html, pattern, ctx + "/m2static/$1");
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);
        return html;
    }

}
