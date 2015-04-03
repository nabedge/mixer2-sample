package org.mixer2.sample.training1.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.training1.bean.Bill;
import org.mixer2.spring.webmvc.AbstractMixer2XhtmlView;
import org.springframework.stereotype.Component;

public class M2billView extends AbstractMixer2XhtmlView {

	@Override
	public Html renderHtml(Html html, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Bill bill = (Bill) model.get("bill");
		html.replaceById("billSource", bill.getSource());
		html.replaceById("billDestination", bill.getDestination());
		
		return html;
	}

}
