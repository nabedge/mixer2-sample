package org.mixer2.sample.view;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.Resources;
import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Form;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Input;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.jaxb.xhtml.Table;
import org.mixer2.jaxb.xhtml.Tbody;
import org.mixer2.jaxb.xhtml.Tr;
import org.mixer2.sample.dto.CartDto;
import org.mixer2.sample.dto.CartItemDto;
import org.mixer2.sample.form.CheckoutForm;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.util.RequestUtil;

public class CheckoutHelper {

    private static Logger logger = Logger.getLogger(CheckoutHelper.class);

    /**
     * replace form/input tags on template by inputted shipping data.
     * @param html
     * @param checkoutForm action form of CheckoutAction
     * @throws TagTypeUnmatchException
     */
    public static void replaceShippingForm(Html html, CheckoutForm checkoutForm) throws TagTypeUnmatchException {

        Form shippingForm = html.getBody().getById("shippingForm", Form.class);

        if (StringUtil.isEmpty(checkoutForm.firstName)) {
            shippingForm.getById("firstName", Input.class).setValue("");
        } else {
            shippingForm.getById("firstName", Input.class).setValue(
                    checkoutForm.firstName);
        }
        if (StringUtil.isEmpty(checkoutForm.lastName)) {
            shippingForm.getById("lastName", Input.class).setValue("");
        } else {
            shippingForm.getById("lastName", Input.class).setValue(
                    checkoutForm.lastName);
        }
        if (StringUtil.isEmpty(checkoutForm.zipCode)) {
            shippingForm.getById("zipCode", Input.class).setValue("");
        } else {
            shippingForm.getById("zipCode", Input.class).setValue(
                    checkoutForm.zipCode);
        }
        if (StringUtil.isEmpty(checkoutForm.address)) {
            shippingForm.getById("address", Input.class).setValue("");
        } else {
            shippingForm.getById("address", Input.class).setValue(
                    checkoutForm.address);
        }

        String ctx = RequestUtil.getRequest().getContextPath();

        // back to cart link
        html.getBody().getById("backToCartAnchorLink", A.class).setHref(
                ctx + "/cart/view");

        // form tag
        shippingForm.setMethod("post");
        shippingForm.setAction(ctx + "/checkout/confirm");

    }

    /**
     * @param html
     * @param cartDto
     * @param checkoutForm
     * @throws TagTypeUnmatchException
     */
    public static void replaceCartTable(Html html, CartDto cartDto,
            CheckoutForm checkoutForm) throws TagTypeUnmatchException {

        Table cartTable = html.getBody().getById("cartTable", Table.class);
        Tbody cartTbody = cartTable.getTbody().get(0);

        Tr baseTr = cartTbody.getTr().get(0).copy(Tr.class);
        cartTbody.getTr().clear();

        for (CartItemDto cartItem : cartDto.getReadOnlyItemList()) {
            // create tr (copy)
            Tr tr = baseTr.copy(Tr.class);

            // item name
            Span itemNameSpan = new Span();
            itemNameSpan.getContent().add(cartItem.itemDto.name);
            itemNameSpan.addCssClass("itemName");
            tr.replaceDescendants("itemName", Span.class, itemNameSpan);

            // item price
            Span itemPriceSpan = new Span();
            itemPriceSpan.getContent().add(cartItem.itemDto.price.toString());
            itemPriceSpan.addCssClass("itemPrice");
            tr.replaceDescendants("itemPrice", Span.class, itemPriceSpan);

            // item amount
            Span itemAmountSpan = new Span();
            itemAmountSpan.getContent().add(Integer.toString(cartItem.amount));
            itemAmountSpan.addCssClass("itemAmount");
            tr.replaceDescendants("itemAmount", Span.class, itemAmountSpan);

            //
            cartTbody.getTr().add(tr);
        }

        // charge for deligery
        cartTable.getTfoot().getById("chargeForDelivery", Span.class)
                .getContent().clear();
        cartTable.getTfoot().getById("chargeForDelivery", Span.class)
                .getContent().add(checkoutForm.chargeForDelivery.toString());

        // total price
        BigDecimal totalPrice = new BigDecimal(0);
        for (CartItemDto cartItem : cartDto.getReadOnlyItemList()) {
            totalPrice = totalPrice.add(cartItem.itemDto.price
                    .multiply(BigDecimal.valueOf(cartItem.amount)));
        }
        totalPrice = totalPrice.add(checkoutForm.chargeForDelivery);
        cartTable.getTfoot().getById("totalPrice", Span.class).getContent()
                .clear();
        cartTable.getTfoot().getById("totalPrice", Span.class).getContent()
                .add(totalPrice.toString());

    }

    public static void replaceShipToAddress(Html html, CheckoutForm checkoutForm) throws TagTypeUnmatchException {
        // name
        Span shipToNameSpan = html.getBody().getById("shipToName", Span.class);
        shipToNameSpan.getContent().clear();
        shipToNameSpan.getContent().add(
                checkoutForm.firstName + " " + checkoutForm.lastName);
        // address
        Span shipToAddressSpan = html.getBody().getById("shipToAddress",
                Span.class);
        shipToAddressSpan.getContent().clear();
        shipToAddressSpan.getContent().add(
                checkoutForm.address + " " + checkoutForm.zipCode);
    }

    public static void replaceOrderCompleteForm(Html html) throws TagTypeUnmatchException {
        String ctx = RequestUtil.getRequest().getContextPath();
        Form form = html.getById("orderCompleteForm", Form.class);
        form.setMethod("post");
        form.setAction(ctx + "/checkout/complete");
    }

    public static void replaceErrorMessage(Html html) throws TagTypeUnmatchException {

        // get ActionMessage from request scope attribute
        ActionMessages actionMessages = (ActionMessages) RequestUtil
                .getRequest().getAttribute(Globals.ERROR_KEY);
        MessageResources mr = Resources.getMessageResources(RequestUtil
                .getRequest());

        // shipping form tag
        Form shippingForm = html.getBody().getById("shippingForm", Form.class);

        // if no error, remove all span tag having errorMessage class attribute.
        if (actionMessages == null) {
            shippingForm.removeDescendants("errorMessage", Span.class);
            return;
        }

        // transform ActionMessage to simple hashmap having error message.
        HashMap<String, String> errorMsgMap = new HashMap<String, String>();
        Iterator<?> ite = actionMessages.properties();
        while (ite.hasNext()) {
            String actionFormField = ite.next().toString();
            logger.debug("##### actionFormField = " + actionFormField);
            Iterator<?> _ite = actionMessages.get(actionFormField);
            while (_ite.hasNext()) {
                ActionMessage am = (ActionMessage) _ite.next();
                String errorMessage = mr
                        .getMessage(am.getKey(), am.getValues());
                errorMsgMap.put(actionFormField, errorMessage);
            }
        }

        if (logger.isDebugEnabled()) {
            for (Map.Entry<String, String> e : errorMsgMap.entrySet()) {
                logger.debug("## " + e.getKey() + " = " + e.getValue());
            }
        }

        // replace span tag by error message
        for (Span span : shippingForm
                .getDescendants("errorMessage", Span.class)) {
            boolean replaced = false;
            for (Map.Entry<String, String> e : errorMsgMap.entrySet()) {
                if (span.hasCssClass(e.getKey())) {
                    span.getContent().clear();
                    span.getContent().add(e.getValue());
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
