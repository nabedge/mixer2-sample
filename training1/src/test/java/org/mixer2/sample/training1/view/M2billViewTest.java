package org.mixer2.sample.training1.view;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.sample.training1.Application;
import org.mixer2.sample.training1.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

/**
 * test for view class (not for controller class)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class M2billViewTest {

	@Autowired
	protected ApplicationContext appCtx;
	
    @Autowired
    protected Mixer2Engine mixer2Engine;

    @Autowired
    protected ResourceLoader resourceLoader;

    @Autowired
    protected BillService billService;

    @Test
    public void test再発行() throws Exception {

        // テンプレート読み込み
        InputStream is = resourceLoader.getResource(
                "classpath:/templates/m2bill.html").getInputStream();
        Html tmplHtml = mixer2Engine.loadHtmlTemplate(is);

        // 再発行フラグつきでのリクエストがあったというていでビューを実行する。
        // まずmodelを準備
        Model model = new ExtendedModelMap();
        model.addAttribute("reissue", true);
        model.addAttribute("bill", billService.createBill());
        // ビューを準備
        M2billView m2billView = new M2billView();
		appCtx.getAutowireCapableBeanFactory().autowireBean(m2billView);
		// request,responseはモックで。
		MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        // ビューを実行する
        Html html = m2billView.renderHtml(tmplHtml, model.asMap(), request,
                response);
        // ビューの実行結果に<span id="reissue">(再発行)</span>が存在することをassert
        assertNotNull(html.getBody().getById("reissue", Span.class));
    }
    
}
