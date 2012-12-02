package org.mixer2.sample.action;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.form.ItemListForm;
import org.mixer2.sample.service.CategoryService;
import org.mixer2.sample.service.ItemService;
import org.mixer2.sample.view.ItemListHelper;
import org.mixer2.sample.view.M2staticHelper;
import org.mixer2.sample.view.SectionHelper;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

public class ItemListAction {

    Logger logger = Logger.getLogger(ItemListAction.class);

    public String htmlString;

    @Resource
    protected Mixer2Engine mixer2Engine;

    @Resource
    protected JdbcManager jdbcManager;

    @Resource
    protected CategoryService categoryService;

    @Resource
    protected ItemService itemService;

    @ActionForm
    @Resource
    protected ItemListForm itemListForm;

    private String mainTemplate = "m2mockup/m2template/itemList.html";

    @Execute(validator = false, urlPattern = "{categoryId}")
    public String itemList() throws IOException, TagTypeUnmatchException {

        // load html template
        File file = ResourceUtil.getResourceAsFile(mainTemplate);
        Html html = mixer2Engine.loadHtmlTemplate(file);

        // embed category list on side bar
        SectionHelper.rewriteSideBar(html, categoryService.getCategoryList());

        // embed item boxes
        ItemListHelper.replaceCategoryBox(html, itemService.getItemList(Long
                .valueOf(itemListForm.categoryId)));

        // replace static file path
        M2staticHelper.replaceM2staticPath(html);

        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);
        // output
        htmlString = mixer2Engine.saveToString(html);
        return "/mixer2view.jsp";
    }
}
