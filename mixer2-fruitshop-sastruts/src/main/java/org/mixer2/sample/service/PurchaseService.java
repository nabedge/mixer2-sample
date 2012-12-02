package org.mixer2.sample.service;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.mixer2.sample.dto.CartDto;
import org.mixer2.sample.dto.CartItemDto;
import org.mixer2.sample.form.CheckoutForm;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.SqlBatchUpdate;

public class PurchaseService {

    @Resource
    protected JdbcManager jdbcManager;

    private String sql_seq = "select common_seq.nextval";

    private String sql_ins_order = "insert into purchase" //
            + " (id, first_name, last_name, zip_code, address, charge_for_delivery)" //
            + " values (?, ?, ?, ?, ?, ?)";

    private String sql_ins_order_detail = "insert into purchase_detail" //
            + " (id, purchase_id, item_id, amount)" //
            + " values (?, ?, ?, ?)";

    public boolean execPurchase(CartDto cartDto, CheckoutForm checkoutForm) {

        // get id from sequence
        long purchase_id = jdbcManager.selectBySql(Long.class, sql_seq)
                .getSingleResult().longValue();

        // insert to purchase table
        jdbcManager.updateBySql(sql_ins_order, //
                Long.class, //
                String.class, //
                String.class, //
                String.class, //
                String.class, //
                BigDecimal.class) //
                .params(Long.valueOf(purchase_id), //
                        checkoutForm.firstName, //
                        checkoutForm.lastName, //
                        checkoutForm.zipCode, //
                        checkoutForm.address, //
                        checkoutForm.chargeForDelivery //
                ).execute();

        // insert to purchase_detail table
        SqlBatchUpdate batchUpdate = jdbcManager.updateBatchBySql(
                sql_ins_order_detail, //
                Long.class, // id
                Long.class, // order_id
                Long.class, // item_id
                Integer.class); // amount
        for (CartItemDto cartItemDto : cartDto.getReadOnlyItemList()) {
            long order_detail_id = jdbcManager.selectBySql(Long.class, sql_seq)
                    .getSingleResult().longValue();
            batchUpdate.params( //
                    order_detail_id, //
                    purchase_id, //
                    cartItemDto.itemDto.id, //
                    cartItemDto.amount);
        }

        batchUpdate.execute();

        return true;
    }

}
