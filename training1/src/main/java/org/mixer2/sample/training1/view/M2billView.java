package org.mixer2.sample.training1.view;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.joda.time.DateTime;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.jaxb.xhtml.Tbody;
import org.mixer2.sample.training1.bean.Bill;
import org.mixer2.sample.training1.bean.Detail;
import org.mixer2.spring.webmvc.AbstractMixer2XhtmlView;
import org.mixer2.xhtml.PathAdjuster;
import org.mixer2.xhtml.builder.TableBuilder;

/**
 * mixer2による請求書ビュークラス。これは画面表示用とpdf出力とで兼用です。
 */
public class M2billView extends AbstractMixer2XhtmlView {

	@Override
	public Html renderHtml(Html html, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// コントローラから渡された請求情報
		Bill bill = (Bill) model.get("bill");

		// コントローラから再発行フラグも与えられている場合に再発行の表示を追加する.
		Boolean reissue = (Boolean) model.get("reissue");
		if (BooleanUtils.isTrue(reissue)) {
			html.insertAfterId("billIssueDate", "(再発行)");
		}

		// 請求書の内容を埋め込む
		html.getById("billDestination", Span.class).replaceInner(
				bill.getDestination());
		html.getById("billTitle", Span.class).replaceInner(bill.getTitle());
		html.getById("billIssueDate", Span.class).replaceInner(
				new DateTime(bill.getIssueDate()).toString("yyyy-MM-dd"));
		// 請求の詳細の部分はMixer2のTableビルダーを使ってtr,tdタグを作り、
		// テンプレートのtbodyの中をごっそり入れ替えるためのtr/tdタグを新たに生成する
		TableBuilder tb = new TableBuilder();
		for (Detail d : bill.getDetailList()) {
			tb.addTr().addTd(d.getProductName())
					.addTd(Integer.toString(d.getCount()))
					.addTd(d.getUnitPrice().toPlainString())
					.addTd(d.getSubtotal().toPlainString());
		}
		// tbodyタグのなかをカラにする
		html.getById("billDetailList", Tbody.class).unsetTr();
		// tbodyタグの中に作ったtrタグを入れる
		html.getById("billDetailList", Tbody.class).getTr()
				.addAll(tb.build().getTr());
		// 合計金額
		html.getById("billCharge", Span.class).replaceInner(bill.getCharge().toPlainString());

		// すべての静的ファイルへの相対パスを絶対urlに変換する
		Pattern pattern = Pattern.compile("^\\.+/static/(.*)$");
		String ctx = request.getContextPath();
		PathAdjuster.replacePath(html, pattern, "http://localhost:8080" + ctx
				+ "/$1");

		return html;
	}
}
