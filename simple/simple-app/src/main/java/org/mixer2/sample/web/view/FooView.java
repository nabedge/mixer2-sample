package org.mixer2.sample.web.view;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Script;
import org.mixer2.sample.web.helper.PartsHelper;
import org.mixer2.sample.web.util.RequestUtil;
import org.mixer2.spring.webmvc.AbstractMixer2XhtmlView;
import org.mixer2.xhtml.PathAdjuster;
import org.mixer2.xhtml.TagCreator;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.springframework.beans.factory.annotation.Autowired;

public class FooView extends AbstractMixer2XhtmlView {

	@Autowired
	private PartsHelper partsHelper;

    @Override
    protected Html renderHtml(Html html, Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws TagTypeUnmatchException {

		// テンプレに書いてあるダミーのjsonのscriptタグを置換する
    	Script script = TagCreator.script();
    	script.setType("text/javascript");
    	script.setId("valueJson");
    	script.setSrc("/valueJson");
    	html.getBody().replaceById("valueJson", script);

    	// replace header,footer
    	html.replaceById("header", partsHelper.getHeader());
    	html.replaceById("footer", partsHelper.getFooter());

        // replace static file path
        String ctx = RequestUtil.getContextPath();
        Pattern pattern = Pattern.compile("^\\.+/.*m2static/(.*)$");
        PathAdjuster.replacePath(html, pattern, ctx + "/m2static/$1");

        // replace html contents path
        Pattern ptn2 = Pattern.compile("^\\.+/.*m2template/(.*)\\.html$");
        PathAdjuster.replacePath(html, ptn2, ctx + "/$1");

        return html;
    }
}
