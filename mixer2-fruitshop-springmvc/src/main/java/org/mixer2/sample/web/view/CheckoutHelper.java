package org.mixer2.sample.web.view;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
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
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.mixer2.xhtml.util.FormUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

public class CheckoutHelper {

    private static Logger logger = Logger.getLogger(CheckoutHelper.class);

    public static void replaceShippingForm(Html html, Shipping shipping) throws TagTypeUnmatchException, IllegalAccessException, InvocationTargetException {

        // populate form tag
        Form shippingForm = html.getBody().getById("shippingForm", Form.class);
        FormUtil.populateForm(shippingForm, shipping);

        // get contextPath
        String ctx = RequestUtil.getContextPath();

        // back to cart link
        html.getBody().getById("backToCartAnchorLink", A.class).setHref(
                ctx + "/cart/view");

        // form tag
        shippingForm.setMethod("post");
        shippingForm.setAction(ctx + "/checkout/confirm");
    }

    public static void replaceCartTable(Html html, Cart cart, Shipping shipping) throws TagTypeUnmatchException {
        Table cartTable = html.getBody().getById("cartTable", Table.class);
        Tbody cartTbody = cartTable.getTbody().get(0);

        Tr baseTr = cartTbody.getTr().get(0).copy(Tr.class);
        cartTbody.getTr().clear();

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
            totalPrice = totalPrice.add(cartItem.getItem().getPrice().multiply(
                    BigDecimal.valueOf(cartItem.getAmount())));
        }
        totalPrice = totalPrice.add(shipping.getChargeForDelivery());
		cartTable.getTfoot().getById("totalPrice", Span.class).unsetContent();
        cartTable.getTfoot().getById("totalPrice", Span.class).getContent()
                .add(totalPrice.toString());

    }

    public static void replaceShipToAddress(Html html, Shipping shipping) throws TagTypeUnmatchException {
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

    public static void replaceOrderCompleteForm(Html html) throws TagTypeUnmatchException {
        String ctx = RequestUtil.getContextPath();
        Form form = html.getById("orderCompleteForm", Form.class);
        form.setMethod("post");
        form.setAction(ctx + "/checkout/complete");
    }

    public static void removeAllErrorMessages(Html html) throws TagTypeUnmatchException {
        Form shippingForm = html.getBody().getById("shippingForm", Form.class);
        shippingForm.removeDescendants("errorMessage", Span.class);
    }

    public static void replaceErrorMessage(Html html, Errors errors) throws TagTypeUnmatchException {

        Form shippingForm = html.getBody().getById("shippingForm", Form.class);

        if (errors == null || errors.hasErrors() == false) {
            logger.debug("#### no errors");
            shippingForm.removeDescendants("errorMessage", Span.class);
            return;
        }

        for (Span span : shippingForm
                .getDescendants("errorMessage", Span.class)) {
            boolean replaced = false;
            for (FieldError e : errors.getFieldErrors()) {
                logger.debug("##### " + e.getField());
                if (span.hasCssClass(e.getField())) {
                    span.unsetContent();
                    span.getContent().add(e.getDefaultMessage());
                    replaced = true;
                    continue;
                }
            }
            // if unmatch on error message map,
            // mark by adding class attribute for remove it
            if (!replaced) {
                span.addCssClass("remove");
            }
        }

        // remove span tag having mark.
        shippingForm.removeDescendants("remove", Span.class);
    }

}
