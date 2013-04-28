package org.mixer2.sample.web.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

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
import org.mixer2.sample.web.view.SectionHelper;
import org.mixer2.sample.web.view.TransactionTokenHelper;
import org.mixer2.springmvc.Mixer2XhtmlView;
import org.mixer2.xhtml.PathAjuster;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
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

    @Autowired
    protected ResourceLoader resourceLoader;

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
    public Mixer2XhtmlView shipping(
            @RequestParam(value = "redirected", required = false) boolean redirected,
            @Valid Shipping shipping, Errors errors) throws IOException,
            TagTypeUnmatchException, IllegalAccessException,
            InvocationTargetException {

        // load html template
        Html html = mixer2Engine.loadHtmlTemplate(resourceLoader.getResource(
                shippingTemplate).getInputStream());

        CheckoutHelper.replaceShippingForm(html, shipping);

        if (redirected) {
            CheckoutHelper.replaceErrorMessage(html, errors);
        } else {
            CheckoutHelper.removeAllErrorMessages(html);
        }

        // replace static file path
        Pattern pattern = Pattern.compile("^\\.+/.*m2static/(.*)$");
        String ctx = RequestUtil.getRequest().getContextPath();
        PathAjuster.replacePath(html, pattern, ctx + "/m2static/$1");

        // header,footer
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        return new Mixer2XhtmlView(mixer2Engine, html);
    }

    @RequestMapping(value = "confirm")
    public ModelAndView confirm(Cart cart, @Valid Shipping shipping,
            Errors errors, HttpSession httpSession) throws IOException,
            TagTypeUnmatchException {

        ModelAndView modelAndView = new ModelAndView();

        // if cart is empty, redirect to cart view page.
        if (cart.getReadOnlyItemList().size() < 1) {
            modelAndView.setViewName("redirect:/cart/view");
            return modelAndView;
        }

        // validation
        if (errors.hasErrors()) {
            logger.debug(errors);
            modelAndView.setViewName("redirect:shipping?redirected=true");
            return modelAndView;
        }

        // load html template
        Html html = mixer2Engine.loadHtmlTemplate(resourceLoader.getResource(
                confirmTemplate).getInputStream());

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
        Pattern pattern = Pattern.compile("^\\.+/.*m2static/(.*)$");
        PathAjuster.replacePath(html, pattern, ctx + "/m2static/$1");

        // header,footer
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        modelAndView.setView(new Mixer2XhtmlView(mixer2Engine, html));
        return modelAndView;
    }

    @RequestMapping(value = "complete")
    public String complete(Shipping shipping, Cart cart, HttpSession httpSession)
            throws Exception {

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
    public Mixer2XhtmlView thankyou() throws IOException, TagTypeUnmatchException {
        String template = "classpath:m2mockup/m2template/checkout/thankyou.html";
        Html html = mixer2Engine.loadHtmlTemplate(resourceLoader.getResource(
                template).getInputStream());

        // replace anchor link to top page
        String ctx = RequestUtil.getRequest().getContextPath();
        html.getBody().getById("goToTopPageAnchorLink", A.class).setHref(ctx);

        // replace static file path
        Pattern pattern = Pattern.compile("^\\.+/.*m2static/(.*)$");
        PathAjuster.replacePath(html, pattern, ctx + "/m2static/$1");
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        return new Mixer2XhtmlView(mixer2Engine, html);
    }

}
