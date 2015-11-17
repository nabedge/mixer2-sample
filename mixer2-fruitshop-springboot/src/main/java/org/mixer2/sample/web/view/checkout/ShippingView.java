package org.mixer2.sample.web.view.checkout;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Form;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Span;
import org.mixer2.sample.web.dto.Shipping;
import org.mixer2.sample.web.util.RequestUtil;
import org.mixer2.sample.web.view.helper.SectionHelper;
import org.mixer2.spring.webmvc.AbstractMixer2XhtmlView;
import org.mixer2.xhtml.PathAdjuster;
import org.mixer2.xhtml.util.FormUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

public class ShippingView extends AbstractMixer2XhtmlView {

    private static Logger logger = Logger.getLogger(ShippingView.class);

    @Override
    protected Html renderHtml(Html html, Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {

        Shipping shipping = (Shipping) model.get("shipping");
        boolean redirected = (Boolean) model.get("redirected");
        Errors errors = (Errors) model.get("errors");

        replaceShippingForm(html, shipping);

        if (redirected) {
            replaceErrorMessage(html, errors);
        } else {
            removeAllErrorMessages(html);
        }

        // replace static file path
        Pattern pattern = Pattern.compile("^\\.+/.*m2static/(.*)$");
        String ctx = RequestUtil.getContextPath();
        PathAdjuster.replacePath(html, pattern, ctx + "/m2static/$1");

        // header,footer
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        return html;
    }

    private void replaceShippingForm(Html html, Shipping shipping)
            throws IllegalAccessException, InvocationTargetException {

        // populate form tag
        Form shippingForm = html.getBody().getById("shippingForm", Form.class);
        FormUtil.populateForm(shippingForm, shipping);

        // get contextPath
        String ctx = RequestUtil.getContextPath();

        // back to cart link
        html.getBody().getById("backToCartAnchorLink", A.class).setHref(ctx + "/cart/view");

        // form tag
        shippingForm.setMethod("post");
        shippingForm.setAction(ctx + "/checkout/confirm");
    }

    private void replaceErrorMessage(Html html, Errors errors) {

        Form shippingForm = html.getBody().getById("shippingForm", Form.class);

        if (errors == null || errors.hasErrors() == false) {
            logger.debug("#### no errors");
            shippingForm.removeDescendants("errorMessage", Span.class);
            return;
        }

        for (Span span : shippingForm.getDescendants("errorMessage", Span.class)) {
            boolean replaced = false;
            for (FieldError e : errors.getFieldErrors()) {
                logger.debug("##### " + e.getField());
                if (span.hasCssClass(e.getField())) {
                    span.unsetContent();
                    span.getContent().add(e.getDefaultMessage());
                    logger.warn(e.getRejectedValue());
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

    private void removeAllErrorMessages(Html html) {
        Form shippingForm = html.getBody().getById("shippingForm", Form.class);
        shippingForm.removeDescendants("errorMessage", Span.class);
    }

}
