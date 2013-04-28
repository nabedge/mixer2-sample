package org.mixer2.sample.web.controller;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.service.CategoryService;
import org.mixer2.sample.service.ItemService;
import org.mixer2.sample.web.util.RequestUtil;
import org.mixer2.sample.web.view.IndexHelper;
import org.mixer2.sample.web.view.SectionHelper;
import org.mixer2.springmvc.Mixer2XhtmlView;
import org.mixer2.xhtml.PathAjuster;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(IndexController.class);

    @Autowired
    protected Mixer2Engine mixer2Engine;

    @Autowired
    protected CategoryService categoryService;

    @Autowired
    protected ItemService itemService;

    @Autowired
    protected ResourceLoader resourceLoader;

    private String mainTemplate = "classpath:m2mockup/m2template/index.html";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Mixer2XhtmlView index() throws IOException, TagTypeUnmatchException {

        // load html template
        Html html = mixer2Engine.loadHtmlTemplate(resourceLoader.getResource(
                mainTemplate).getInputStream());

        // embed category list on side bar
        SectionHelper.rewriteSideBar(html, categoryService.getCategoryList());

        // embed category Box on content
        IndexHelper.replaceCategoryBox(html,
                itemService.getOneItemByOneCategory());

        // replace static file path
        Pattern pattern = Pattern.compile("^\\.+/.*m2static/(.*)$");
        String ctx = RequestUtil.getRequest().getContextPath();
        PathAjuster.replacePath(html, pattern, ctx + "/m2static/$1");

        // header,footer
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        return new Mixer2XhtmlView(mixer2Engine, html);
    }

}
