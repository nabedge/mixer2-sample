package org.mixer2.sample.web.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Form;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.service.PurchaseService;
import org.mixer2.sample.web.dto.Cart;
import org.mixer2.sample.web.dto.Shipping;
import org.mixer2.sample.web.util.RequestUtil;
import org.mixer2.sample.web.view.CheckoutHelper;
import org.mixer2.sample.web.view.M2staticHelper;
import org.mixer2.sample.web.view.SectionHelper;
import org.mixer2.sample.web.view.TransactionTokenHelper;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/checkout")
@SessionAttributes(value = { "shipping", "cart" })
public class CheckoutController {

    private Logger logger = Logger.getLogger(CheckoutController.class);

    @Autowired
    protected Mixer2Engine mixer2Engine;

    @Autowired
    protected PurchaseService purchaseService;

    private String shippingTemplate = "classpath:m2mockup/m2template/checkout/shipping.html";

    private String confirmTemplate = "classpath:m2mockup/m2template/checkout/confirm.html";

    @ModelAttribute("shipping")
    public Shipping createShipping() {
        logger.debug("#### create shipping object...");
        return new Shipping();
    }

    @ModelAttribute("cart")
    public Cart createCart() {
        logger.debug("#### create cart object...");
        return new Cart();
    }

    @RequestMapping(value = "/shipping")
    public ModelAndView shipping(
            @RequestParam(value = "redirected", required = false) boolean redirected,
            @Valid Shipping shipping, Errors errors) throws IOException, TagTypeUnmatchException, IllegalAccessException, InvocationTargetException {

        // load html template
        File file = ResourceUtils.getFile(shippingTemplate);
        Html html = mixer2Engine.loadHtmlTemplate(file);

        CheckoutHelper.replaceShippingForm(html, shipping);

        if (redirected) {
            CheckoutHelper.replaceErrorMessage(html, errors);
        } else {
            CheckoutHelper.removeAllErrorMessages(html);
        }

        // replace static file path
        M2staticHelper.replaceM2staticPath(html);

        // header,footer
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        ModelAndView modelAndView = new ModelAndView("mixer2view", "htmlString", mixer2Engine
                .saveToString(html));
        return modelAndView;
    }

    @RequestMapping(value = "confirm")
    public ModelAndView confirm(Cart cart, @Valid Shipping shipping,
            Errors errors, HttpSession httpSession) throws IOException, TagTypeUnmatchException {

        // if cart is empty, redirect to cart view page.
        if (cart.getReadOnlyItemList().size() < 1) {
            return new ModelAndView("redirect:/cart/view");
        }

        // validation
        if (errors.hasErrors()) {
            logger.debug(errors);
            return new ModelAndView("redirect:shipping?redirected=true");
        }

        // load html template
        File file = ResourceUtils.getFile(confirmTemplate);
        Html html = mixer2Engine.loadHtmlTemplate(file);

        CheckoutHelper.replaceCartTable(html, cart, shipping);
        CheckoutHelper.replaceShipToAddress(html, shipping);
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

        // header,footer
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        ModelAndView modelAndView = new ModelAndView("mixer2view", "htmlString", mixer2Engine
                .saveToString(html));
        return modelAndView;
    }

    @RequestMapping(value = "complete")
    public String complete(Shipping shipping, Cart cart, HttpSession httpSession) throws Exception {

        // check transaction token
        boolean checkResult = TransactionTokenHelper.checkToken(httpSession,
                shipping.getTransactionToken());
        if (!checkResult) {
            throw new Exception("Transaction Token Unmatch !");
        }

        boolean result = purchaseService.execPurchase(cart, shipping);
        if (result) {
            TransactionTokenHelper.removeToken(httpSession);
            cart.removeAll();
            logger.debug("### purchase succeed!!");
        }

        return "redirect:thankyou";
    }

    @RequestMapping(value = "thankyou")
    public ModelAndView thankyou() throws IOException, TagTypeUnmatchException {
        String template = "classpath:m2mockup/m2template/checkout/thankyou.html";
        File file = ResourceUtils.getFile(template);
        Html html = mixer2Engine.loadHtmlTemplate(file);

        // replace anchor link to top page
        String ctx = RequestUtil.getRequest().getContextPath();
        html.getBody().getById("goToTopPageAnchorLink", A.class).setHref(ctx);

        // replace static file path
        M2staticHelper.replaceM2staticPath(html);
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        ModelAndView modelAndView = new ModelAndView("mixer2view", "htmlString", mixer2Engine
                .saveToString(html));
        return modelAndView;
    }

}
