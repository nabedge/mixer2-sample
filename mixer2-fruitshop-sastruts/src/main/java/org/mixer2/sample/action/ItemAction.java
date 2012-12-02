package org.mixer2.sample.action;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.dto.ItemDto;
import org.mixer2.sample.form.ItemForm;
import org.mixer2.sample.service.CategoryService;
import org.mixer2.sample.service.ItemService;
import org.mixer2.sample.view.ItemHelper;
import org.mixer2.sample.view.M2staticHelper;
import org.mixer2.sample.view.SectionHelper;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

public class ItemAction {

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(ItemAction.class);

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
    protected ItemForm itemForm;

    private String mainTemplate = "m2mockup/m2template/item.html";

    /**
     * show one item detail screen.
     */
    @Execute(validator = false, urlPattern = "{itemId}")
    public String item() throws IOException, TagTypeUnmatchException {

        // load html template
        File file = ResourceUtil.getResourceAsFile(mainTemplate);
        Html html = mixer2Engine.loadHtmlTemplate(file);

        // get item data from database
        ItemDto item = itemService.getItem(Long
                .valueOf(itemForm.itemId));
        
        // embed item boxes
        ItemHelper.replaceItemBox(html, item);

        // replace static file path
        M2staticHelper.replaceM2staticPath(html);

        // embed category list on side bar
        SectionHelper.rewriteSideBar(html, categoryService.getCategoryList());

        // header,footer
        SectionHelper.rewriteHeader(html);
        SectionHelper.rewiteFooter(html);

        // output
        htmlString = mixer2Engine.saveToString(html);
        return "/mixer2view.jsp";
    }
}
