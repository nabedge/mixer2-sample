package org.mixer2.sample.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mixer2.sample.dto.CartDto;
import org.mixer2.sample.dto.ItemDto;
import org.mixer2.sample.form.CheckoutForm;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.unit.DataAccessor;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.TestContext;

@RunWith(Seasar2.class)
public class PurchaseServiceTest {

    protected TestContext testContext;

    protected PurchaseService purchaseService;

    protected JdbcManager jdbcManager;

    protected DataAccessor accessor;

    @Test
    public void execute() throws Exception {

        // delete all records from purchase table
        accessor.deleteTable("purchase");
        accessor.deleteTable("purchase_detail");

        // prepare cart object with 2 item.
        ItemDto itemDto1 = new ItemDto();
        itemDto1.id = 888;
        ItemDto itemDto2 = new ItemDto();
        itemDto2.id = 999;
        CartDto cartDto = new CartDto();
        cartDto.setItem(itemDto1, 8);
        cartDto.setItem(itemDto2, 9);

        // prepare actionform
        CheckoutForm checkoutForm = new CheckoutForm();
        checkoutForm.address = "address888999";
        checkoutForm.firstName = "firstName888999";
        checkoutForm.lastName = "lastName888999";
        checkoutForm.zipCode = "888999";

        // execute method and assert
        boolean result = purchaseService.execPurchase(cartDto, checkoutForm);

        // assert
        assertTrue(result);

        DataTable purchase = accessor.readDbByTable("purchase");
        assertThat(purchase.getRowSize(), is(1));
        assertThat(purchase.getRow(0).getValue("address").toString(),
                is("address888999"));
        assertThat(purchase.getRow(0).getValue("firstName").toString(),
                is("firstName888999"));
        assertThat(purchase.getRow(0).getValue("lastName").toString(),
                is("lastName888999"));
        assertThat(purchase.getRow(0).getValue("zipCode").toString(),
                is("888999"));

        DataTable purchaseDetail = accessor.readDbByTable("purchase_detail");
        assertThat(purchaseDetail.getRowSize(), is(2));
        assertThat(purchaseDetail.getRow(0).getValue("itemId").toString(),
                is("888"));
        assertThat(purchaseDetail.getRow(0).getValue("amount").toString(),
                is("8"));
        assertThat(purchaseDetail.getRow(1).getValue("itemId").toString(),
                is("999"));
        assertThat(purchaseDetail.getRow(1).getValue("amount").toString(),
                is("9"));

    }

}
