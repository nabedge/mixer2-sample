package org.mixer2.sample.web.helper;

import java.io.IOException;
import java.io.InputStream;

import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class PartsHelper {

	private final String partsTemplate = "classpath:/m2mockup/m2template/parts.html";

	@Autowired
	private Mixer2Engine mixer2Engine;

	@Autowired
	private ResourceLoader resourceLoader;

	public Div getHeader() {
		return getPart("header");
	}

	public Div getFooter() {
		return getPart("footer");
	}

	private Div getPart(String id) {
		Div div = null;
		try {
			// テンプレートの変更を即時反映させるために、いちいちテンプレをロードする。
			// 本番環境ではロード後にキャッシュしておいてcopy()使って返すべき。
			InputStream is = resourceLoader.getResource(partsTemplate).getInputStream();
			Html tmpl = mixer2Engine.loadHtmlTemplate(is);
			div = tmpl.getById(id, Div.class);
		} catch (IOException | TagTypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return div;
	}

}
