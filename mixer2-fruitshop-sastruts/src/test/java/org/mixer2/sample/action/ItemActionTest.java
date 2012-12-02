package org.mixer2.sample.action;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.H1;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.sample.form.ItemForm;
import org.mixer2.sample.service.CategoryService;
import org.mixer2.sample.service.ItemService;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class ItemActionTest {

    protected Logger logger = Logger.getLogger(ItemActionTest.class);

    protected JdbcManager jdbcManager;

    protected Mixer2Engine mixer2Engine;

    protected CategoryService categoryService;

    protected ItemService itemService;

    public void request_Item_10001_get_Apple_tx() throws Exception {

        String itemName_expected = "Apple";

        // prepare actionform
        ItemForm itemForm = new ItemForm();
        itemForm.itemId = "10001";

        // prepare action
        ItemAction itemAction = new ItemAction();
        itemAction.itemForm = itemForm;
        itemAction.mixer2Engine = mixer2Engine;
        itemAction.jdbcManager = jdbcManager;
        itemAction.categoryService = categoryService;
        itemAction.itemService = itemService;

        // execute method
        String result = itemAction.item();

        // assert
        assertEquals("/mixer2view.jsp", result);
        Html html = mixer2Engine.loadHtmlTemplate(itemAction.htmlString);
        Div itemBox = html.getBody().getById("itemBox", Div.class);
        assertThat(itemBox.getById("itemName", H1.class).getContent().get(0)
                .toString(), is(itemName_expected));
    }

}
