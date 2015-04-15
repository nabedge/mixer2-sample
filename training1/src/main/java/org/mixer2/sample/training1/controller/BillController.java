package org.mixer2.sample.training1.controller;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.training1.bean.Bill;
import org.mixer2.sample.training1.form.BillForm;
import org.mixer2.sample.training1.service.BillService;
import org.mixer2.sample.training1.view.M2billView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Controller
@RequestMapping("/bill")
public class BillController {

	@Autowired
	private ApplicationContext appCtx;

	@Autowired
	protected Mixer2Engine mixer2Engine;

	@Autowired
	protected ResourceLoader resourceLoader;

	@Autowired
	protected BillService billService;

	@RequestMapping("/json")
	@ResponseBody
	public Bill json() {
		Bill bill = billService.createBill();
		return bill;
	}

	@RequestMapping("/html")
	public String html(Model model, BillForm billForm) {
		Bill bill = json();
		model.addAttribute("bill", bill);
		// 再発行の表示を入れるかのフラグもmodelに入れておく（あとでviewに渡す）
		model.addAttribute("reissue", billForm.isReissue());
		return "bill";
	}

	@RequestMapping("/m2html")
	public String m2html(Model model, BillForm billForm) {
		Bill bill = json();
		model.addAttribute("bill", bill);
		// 再発行の表示を入れるかのフラグもmodelに入れておく（あとでviewに渡す）
		model.addAttribute("reissue", billForm.isReissue());
		return "m2bill";
	}

	@RequestMapping(value = "/pdf", produces = "application/pdf")
	public void pdf(Model model, BillForm billForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// pdfに埋め込む値は画面と同じように取得してmodelに入れる
		Bill bill = json();
		model.addAttribute("bill", bill);

		// 再発行の表示を入れるかのフラグもmodelに入れておく（あとでviewに渡す）
		model.addAttribute("reissue", billForm.isReissue());

		// web画面用のテンプレートを読み込んでmixer2のhtml型のインスタンスを作る
		InputStream is = resourceLoader.getResource(
				"classpath:/templates/m2bill.html").getInputStream();
		Html tmplHtml = mixer2Engine.loadHtmlTemplate(is);
		IOUtils.closeQuietly(is);

		// html画面表示用のビュークラスをpdf出力に流用するためにまずnewする
		M2billView m2billView = new M2billView();

		// M2billViewクラスに対して自力でDIする(@Autowiredや@Valueで指定されたプロパティに値を自動挿入）
		appCtx.getAutowireCapableBeanFactory().autowireBean(m2billView);

		// 読み込んでおいたテンプレートをviewクラスに処理させて値を埋め込む
		Html html = m2billView.renderHtml(tmplHtml, model.asMap(), request,
				response);

		// headerの部分はpdfでは不要なので削除
		html.removeById("header");

		// htmlをstringに戻し、さらにそこからpdfを生成してresponseに詰め込む
		String htmlStr = mixer2Engine.saveToString(html);
		ITextRenderer itr = new ITextRenderer();
		itr.setDocumentFromString(htmlStr);
		itr.layout();
		itr.createPDF(response.getOutputStream());
	}
}
