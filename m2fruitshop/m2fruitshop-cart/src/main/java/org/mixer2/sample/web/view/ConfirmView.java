package org.mixer2.sample.web.view;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Form;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.jaxb.xhtml.Table;
import org.mixer2.jaxb.xhtml.Tbody;
import org.mixer2.jaxb.xhtml.Tr;
import org.mixer2.sample.web.dto.Cart;
import org.mixer2.sample.web.dto.CartItem;
import org.mixer2.sample.web.dto.Shipping;
import org.mixer2.sample.web.util.RequestUtil;
import org.mixer2.sample.web.view.helper.SectionHelper;
import org.mixer2.sample.web.view.helper.TransactionTokenHelper;
import org.mixer2.springmvc.AbstractMixer2XhtmlView;
import org.mixer2.xhtml.PathAjuster;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ConfirmView extends AbstractMixer2XhtmlView {

    @Autowired
    protected ResourceLoader resourceLoader;

    @Autowired
    protected HttpSession httpSession;

    @Override
    protected Html createHtml(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Cart cart = (Cart) model.get("cart");
        Shipping shipping = (Shipping) model.get("shipping");

        // load html template
        String template = "classpath:m2mockup/m2template/checkout/confirm.html";
        Html html = getMixer2Engine().loadHtmlTemplate(
                resourceLoader.getResource(template).getInputStream());

        replaceCartTable(html, cart, shipping);
        replaceShipToAddress(html, shipping);
        replaceOrderCompleteForm(html);

        // set transaction token
        Form orderCompleteForm = html.getById("orderCompleteForm", Form.class);
        TransactionTokenHelper.addToken(httpSession, orderCompleteForm);

        // replace anchor link
        String ctx = RequestUtil.getContextPath();
        html.getBody().getById("backToShippingInfoAnchorLink", A.class)
                .setHref(ctx + "/checkout/shipping");

        // replace static file path
        Pattern pattern = Pattern.compile("^\\.+/.*m2static/(.*)$");
        PathAjuster.replacePath(html, pattern, ctx + "/m2static/$1");

        // header,footer
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        return html;
    }

    private void replaceCartTable(Html html, Cart cart, Shipping shipping)
            throws TagTypeUnmatchException {
        Table cartTable = html.getBody().getById("cartTable", Table.class);
        Tbody cartTbody = cartTable.getTbody().get(0);

        Tr baseTr = cartTbody.getTr().get(0).copy(Tr.class);
        cartTbody.unsetTr(); // equals .getTr().clear()

        for (CartItem cartItem : cart.getReadOnlyItemList()) {
            // create tr (copy)
            Tr tr = baseTr.copy(Tr.class);

            // item name
            Span itemNameSpan = new Span();
            itemNameSpan.getContent().add(cartItem.getItem().getName());
            itemNameSpan.addCssClass("itemName");
            tr.replaceDescendants("itemName", Span.class, itemNameSpan);

            // item price
            Span itemPriceSpan = new Span();
            itemPriceSpan.getContent().add(
                    cartItem.getItem().getPrice().toString());
            itemPriceSpan.addCssClass("itemPrice");
            tr.replaceDescendants("itemPrice", Span.class, itemPriceSpan);

            // item amount
            Span itemAmountSpan = new Span();
            itemAmountSpan.getContent().add(
                    Integer.toString(cartItem.getAmount()));
            itemAmountSpan.addCssClass("itemAmount");
            tr.replaceDescendants("itemAmount", Span.class, itemAmountSpan);

            //
            cartTbody.getTr().add(tr);
        }

        // charge for deligery
        cartTable.getTfoot().getById("chargeForDelivery", Span.class)
                .unsetContent();
        cartTable.getTfoot().getById("chargeForDelivery", Span.class)
                .getContent().add(shipping.getChargeForDelivery().toString());

        // total price
        BigDecimal totalPrice = new BigDecimal(0);
        for (CartItem cartItem : cart.getReadOnlyItemList()) {
            totalPrice = totalPrice.add(cartItem.getItem().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getAmount())));
        }
        totalPrice = totalPrice.add(shipping.getChargeForDelivery());
        cartTable.getTfoot().getById("totalPrice", Span.class).unsetContent();
        cartTable.getTfoot().getById("totalPrice", Span.class).getContent()
                .add(totalPrice.toString());

    }

    private void replaceShipToAddress(Html html, Shipping shipping)
            throws TagTypeUnmatchException {
        // name
        Span shipToNameSpan = html.getBody().getById("shipToName", Span.class);
        shipToNameSpan.unsetContent();
        shipToNameSpan.getContent().add(
                shipping.getFirstName() + " " + shipping.getLastName());
        // address
        Span shipToAddressSpan = html.getBody().getById("shipToAddress",
                Span.class);
        shipToAddressSpan.unsetContent();
        shipToAddressSpan.getContent().add(
                shipping.getAddress() + " " + shipping.getZipCode());
    }

    private void replaceOrderCompleteForm(Html html)
            throws TagTypeUnmatchException {
        String ctx = RequestUtil.getContextPath();
        Form form = html.getById("orderCompleteForm", Form.class);
        form.setMethod("post");
        form.setAction(ctx + "/checkout/complete");
    }

}
