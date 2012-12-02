package org.mixer2.sample.action;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Form;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.dto.CartDto;
import org.mixer2.sample.form.CheckoutForm;
import org.mixer2.sample.service.PurchaseService;
import org.mixer2.sample.view.CheckoutHelper;
import org.mixer2.sample.view.M2staticHelper;
import org.mixer2.sample.view.SectionHelper;
import org.mixer2.sample.view.TransactionTokenHelper;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.RequestUtil;

public class CheckoutAction {

    private static Logger logger = Logger.getLogger(CheckoutAction.class);

    public String htmlString;

    @Resource
    protected Mixer2Engine mixer2Engine;

    @ActionForm
    @Resource
    protected CheckoutForm checkoutForm;

    @Resource
    protected CartDto cartDto;

    @Resource
    protected PurchaseService purchaseService;

    @Resource
    protected HttpSession httpSession;

    /**
     * shipping information input form.
     */
    @Execute(validator = false)
    public String shipping() throws IOException, TagTypeUnmatchException {

        // if cart is empty, redirect to cart view page.
        if (cartDto.getReadOnlyItemList().size() < 1) {
            return "/cart/view?redirect=true";
        }

        // load html template
        String template = "m2mockup/m2template/checkout/shipping.html";
        File file = ResourceUtil.getResourceAsFile(template);
        Html html = mixer2Engine.loadHtmlTemplate(file);

        // show error message if has error.
        CheckoutHelper.replaceErrorMessage(html);

        // replace form by shipping data
        CheckoutHelper.replaceShippingForm(html, checkoutForm);

        // replace static file path
        M2staticHelper.replaceM2staticPath(html);
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);
        htmlString = mixer2Engine.saveToString(html);
        return "/mixer2view.jsp";
    }

    /**
     * show confirmation screen. if error, show error message on shipping screen.
     */
    @Execute(validator = true, input = "/checkout/shipping")
    public String confirm() throws IOException, TagTypeUnmatchException {

        // if cartDto or shippingDto is empty, redirect to cart view page.
        if (cartDto.getReadOnlyItemList().size() < 1) {
            return "/cart/view?redirect=true";
        }

        // load html template
        String template = "m2mockup/m2template/checkout/confirm.html";
        File file = ResourceUtil.getResourceAsFile(template);
        Html html = mixer2Engine.loadHtmlTemplate(file);

        CheckoutHelper.replaceCartTable(html, cartDto, checkoutForm);
        CheckoutHelper.replaceShipToAddress(html, checkoutForm);
        CheckoutHelper.replaceOrderCompleteForm(html);

        // set transaction token
        Form orderCompleteForm = html.getById("orderCompleteForm", Form.class);
        TransactionTokenHelper.addToken(httpSession, orderCompleteForm);

        // replace anchor link
        String ctx = RequestUtil.getRequest().getContextPath();
        html.getBody().getById("backToShippingInfoAnchorLink", A.class)
                .setHref(ctx + "/checkout/shipping");

        // replace static file path
        M2staticHelper.replaceM2staticPath(html);
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);
        htmlString = mixer2Engine.saveToString(html);
        return "/mixer2view.jsp";
    }

    /**
     * finish to order. create order recored on database and remove cartDto. ActionForm "checkoutForm" will be removed if no
     * error.
     * @throws Exception
     */
    @Execute(validator = false, removeActionForm = true)
    public String complete() throws Exception {

        // check transaction token
        boolean checkResult = TransactionTokenHelper.checkToken(httpSession,
                checkoutForm.transactionToken);
        if (!checkResult) {
            throw new Exception("Transaction Token Unmatch !");
        }

        // order
        boolean result = purchaseService.execPurchase(cartDto, checkoutForm);
        if (result) {
            TransactionTokenHelper.removeToken(httpSession);
            cartDto.removeAll();
            logger.debug("### purchase succeed!!");
        }

        return "/checkout/thankyou?redirect=true";
    }

    /**
     * show thank you screen.
     */
    @Execute(validator = false)
    public String thankyou() throws IOException, TagTypeUnmatchException {

        // load html template
        String template = "m2mockup/m2template/checkout/thankyou.html";
        File file = ResourceUtil.getResourceAsFile(template);
        Html html = mixer2Engine.loadHtmlTemplate(file);

        // replace anchor link to top page
        String ctx = RequestUtil.getRequest().getContextPath();
        html.getBody().getById("goToTopPageAnchorLink", A.class).setHref(ctx);

        // replace static file path
        M2staticHelper.replaceM2staticPath(html);
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);
        htmlString = mixer2Engine.saveToString(html);
        return "/mixer2view.jsp";
    }

}
