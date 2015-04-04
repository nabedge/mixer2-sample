package org.mixer2.sample.training1.controller;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Header;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.training1.bean.Bill;
import org.mixer2.sample.training1.bean.Detail;
import org.mixer2.sample.training1.service.BillService;
import org.mixer2.sample.training1.view.M2billView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    protected BillService billService;

    @RequestMapping("/json")
    @ResponseBody
    public Bill json(
            @RequestParam(required = false, defaultValue = "false") boolean reissue) {
        Bill bill = billService.createBill();
        return bill;
    }

    @RequestMapping("/html")
    public String html(
            Model model,
            @RequestParam(required = false, defaultValue = "false") boolean reissue) {
        Bill bill = json(reissue);
        model.addAttribute("bill", bill);
        return "bill";
    }

    @RequestMapping("/m2html")
    public String m2html(
            Model model,
            @RequestParam(required = false, defaultValue = "false") boolean reissue) {
        Bill bill = json(reissue);
        model.addAttribute("bill", bill);
        return "m2bill";
    }

    @RequestMapping(value = "/pdf", produces = "application/pdf")
    public void pdf(
            Model model,
            @RequestParam(required = false, defaultValue = "false") boolean reissue,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // pdfに埋め込む値は画面と同じように取得
        Bill bill = json(reissue);
        model.addAttribute("bill", bill);

        // 請求書に「再発行」の表示をつけるか
        bill.setReissue(reissue);

        // web画面用のテンプレートを読み込んでmixer2のhtml型のインスタンスを作る
        InputStream is = resourceLoader.getResource(
                "classpath:/templates/m2bill.html").getInputStream();
        Html tmplHtml = mixer2Engine.loadHtmlTemplate(is);
        IOUtils.closeQuietly(is);

        // 読み込んだテンプレートに対し、画面用のビュークラスを使って値の埋め込みを行う
        M2billView m2billView = new M2billView();
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
