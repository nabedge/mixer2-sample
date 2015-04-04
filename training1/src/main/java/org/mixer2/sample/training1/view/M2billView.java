package org.mixer2.sample.training1.view;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Tbody;
import org.mixer2.sample.training1.bean.Bill;
import org.mixer2.sample.training1.bean.Detail;
import org.mixer2.spring.webmvc.AbstractMixer2XhtmlView;
import org.mixer2.xhtml.PathAdjuster;
import org.mixer2.xhtml.builder.TableBuilder;

public class M2billView extends AbstractMixer2XhtmlView {

    @Override
    public Html renderHtml(Html html, Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Bill bill = (Bill) model.get("bill");
        
        // 再発行フラグが立っていない場合は再発行の表示をはずす
        if (bill.isReissue() == false) {
            html.removeById("isReissue");
        }

        // 請求書の内容
        html.replaceById("billDestination", bill.getDestination());
        html.replaceById("billTitle", bill.getTitle());
        html.replaceById("billIssueDate",
                new DateTime(bill.getIssueDate()).toString("yyyy-MM-dd"));
        TableBuilder tb = new TableBuilder();
        for (Detail d : bill.getDetailList()) {
            tb.addTr()
                .addTd(d.getProductName())
                .addTd(Integer.toString(d.getCount()))
                .addTd(d.getUnitPrice().toPlainString())
                .addTd(d.calcSubtotal().toPlainString());
        }
        html.getById("billDetailList", Tbody.class).unsetTr();
        html.getById("billDetailList", Tbody.class).getTr().addAll(tb.build().getTr());
        html.replaceById("billCharge", bill.calcCharge().toPlainString());

        // replace static file path
        Pattern pattern = Pattern.compile("^\\.+/static/(.*)$");
        String ctx = request.getContextPath();
        PathAdjuster.replacePath(html, pattern, "http://localhost:8080" + ctx
                + "/$1");

        return html;
    }

}
