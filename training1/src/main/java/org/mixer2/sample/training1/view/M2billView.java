package org.mixer2.sample.training1.view;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.joda.time.DateTime;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.jaxb.xhtml.Table;
import org.mixer2.jaxb.xhtml.Tbody;
import org.mixer2.sample.training1.bean.Bill;
import org.mixer2.sample.training1.bean.Detail;
import org.mixer2.spring.webmvc.AbstractMixer2XhtmlView;
import org.mixer2.xhtml.PathAdjuster;
import org.mixer2.xhtml.TagCreator;
import org.mixer2.xhtml.builder.TableBuilder;
import org.springframework.beans.factory.annotation.Value;

/**
 * mixer2による請求書ビュークラス。これは画面表示用とpdf出力とで兼用です。
 * @see http://mixer2.org/site/ja/springmvc.html
 */
public class M2billView extends AbstractMixer2XhtmlView {

	/**
	 * @see http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
	 */
	@Value("${server.port}")
	private int serverPort;

	@Override
	public Html renderHtml(Html html, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// コントローラから渡された請求情報
		Bill bill = (Bill) model.get("bill");

		// コントローラから再発行フラグも与えられている場合に再発行の表示を追加する.
		Boolean reissue = (Boolean) model.get("reissue");
		if (BooleanUtils.isTrue(reissue)) {
			Span span = TagCreator.spanWithId("reissue");
			span.getContent().add("再発行");
			html.insertAfterId("billIssueDate", span);
		}

		// 請求書の内容を埋め込む
		html.getById("billDestination", Span.class).replaceInner(
				bill.getDestination());
		html.getById("billTitle", Span.class).replaceInner(bill.getTitle());
		String issueDate = new DateTime(bill.getIssueDate()).toString("yyyy-MM-dd");
		html.getById("billIssueDate", Span.class).replaceInner(issueDate);

		// 請求の詳細の部分
		// テンプレートのtbodyの中をごっそり入れ替えるためのtr/tdタグを新たに生成する
		TableBuilder tb = new TableBuilder();
		for (Detail d : bill.getDetailList()) {
			tb.addTr().addTd(d.getProductName())
					.addTd(Integer.toString(d.getCount()))
					.addTd(d.getUnitPrice().toPlainString())
					.addTd(d.getSubtotal().toPlainString());
		}
		Table tmpTable = tb.build();
		// tbodyタグのなかをカラにしておく
		html.getById("billDetailList", Tbody.class).unsetTr();
		// tbodyタグの中に, さっき作ったtrタグを入れる
		html.getById("billDetailList", Tbody.class).getTr()
				.addAll(tmpTable.getTr());

		// 合計金額
		html.getById("billCharge", Span.class).replaceInner(
				bill.getCharge().toPlainString());

		// すべての静的ファイルへの相対パスを絶対urlに変換する
		Pattern pattern = Pattern.compile("^\\.+/static/(.*)$");
		String ctx = request.getContextPath();
		PathAdjuster.replacePath(html, pattern, "http://localhost:"
				+ serverPort + ctx + "/$1");

		return html;
	}
}
