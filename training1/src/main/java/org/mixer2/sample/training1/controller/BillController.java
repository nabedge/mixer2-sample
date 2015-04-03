package org.mixer2.sample.training1.controller;

import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.training1.bean.Bill;
import org.mixer2.sample.training1.view.M2billView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Controller
@RequestMapping("/bill")
public class BillController {

	@Autowired
	protected Mixer2Engine mixer2Engine;

	@Autowired
	protected ResourceLoader resourceLoader;
	
	@Autowired
	protected ApplicationContext appCtx;

	@RequestMapping("/json")
	@ResponseBody
	public Bill json() {
		Bill bill = new Bill();
		bill.setSource("foo事務所");
		bill.setDestination("bar株式会社");
		return bill;
	}

	@RequestMapping("/html")
	public String html(Model model) {
		Bill bill = json();
		model.addAttribute("bill", bill);
		return "bill";
	}

	@RequestMapping("/m2html")
	public String m2html(Model model) {
		Bill bill = json();
		model.addAttribute("bill", bill);
		return "m2bill";
	}

	@RequestMapping(value = "/pdf", produces = "application/pdf")
	public void pdf(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Bill bill = json();
		model.addAttribute("bill", bill);
		
		M2billView m2billView = new M2billView();
		
		// 
		appCtx.getAutowireCapableBeanFactory().autowireBean(m2billView);
		
		
		InputStream is = resourceLoader.getResource(
				"classpath:/templates/m2bill.html").getInputStream();
		Html tmplHtml = mixer2Engine.loadHtmlTemplate(is);
		Html html = m2billView.renderHtml(tmplHtml, model.asMap(), request,
				response);

		// html.removeDescendants(A.class);

		ITextRenderer iTextRenderer = new ITextRenderer();
		iTextRenderer.setDocumentFromString(mixer2Engine.saveToString(html));
		iTextRenderer.layout();
		iTextRenderer.createPDF(response.getOutputStream());
	}

}
