package org.mixer2.sample.action;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.dto.CategoryDto;
import org.mixer2.sample.service.CategoryService;
import org.mixer2.sample.service.ItemService;
import org.mixer2.sample.view.IndexHelper;
import org.mixer2.sample.view.M2staticHelper;
import org.mixer2.sample.view.SectionHelper;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.struts.annotation.Execute;

public class IndexAction {

    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(IndexAction.class);

    public String htmlString;

    @Resource
    protected Mixer2Engine mixer2Engine;

    @Resource
    protected JdbcManager jdbcManager;

    @Resource
    protected CategoryService categoryService;

    @Resource
    protected ItemService itemService;

    private String mainTemplate = "m2mockup/m2template/index.html";

    /**
     * show top page.
     */
    @Execute(validator = false)
    public String index() throws IOException, TagTypeUnmatchException {

        // load html template
        File file = ResourceUtil.getResourceAsFile(mainTemplate);
        Html html = mixer2Engine.loadHtmlTemplate(file);

        // get category data
        List<CategoryDto> categoryList = categoryService.getCategoryList();

        // embed category list on side bar
        SectionHelper.rewriteSideBar(html, categoryList);

        // embed category Box on content
        IndexHelper.replaceCategoryBox(html, itemService
                .getOneItemByOneCategory());

        // replace static file path
        M2staticHelper.replaceM2staticPath(html);

        // replace logo link to top page
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        // output
        htmlString = mixer2Engine.saveToString(html);
        return "/mixer2view.jsp";
    }
}
