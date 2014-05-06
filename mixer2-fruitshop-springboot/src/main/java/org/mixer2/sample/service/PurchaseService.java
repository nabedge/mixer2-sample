package org.mixer2.sample.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.mixer2.sample.web.dto.Cart;
import org.mixer2.sample.web.dto.CartItem;
import org.mixer2.sample.web.dto.Shipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PurchaseService {

    private Logger logger = Logger.getLogger(PurchaseService.class);

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private String sql_seq = "select common_seq.nextval";

    private String sql_ins_purchase = "insert into purchase" //
            + " (id, first_name, last_name, zip_code, address, charge_for_delivery)" //
            + " values (?, ?, ?, ?, ?, ?)";

    private String sql_ins_purchase_detail = "insert into purchase_detail" //
            + " (id, purchase_id, item_id, amount)" //
            + " values (?, ?, ?, ?)";

    public boolean execPurchase(Cart cart, Shipping shipping) {

        // get id from sequence
        long purchaseId = jdbcTemplate.queryForObject(sql_seq, Long.class);

        // insert to purchase table
        int result = jdbcTemplate.update(sql_ins_purchase, purchaseId,
                shipping.getFirstName(), shipping.getLastName(),
                shipping.getZipCode(), shipping.getAddress(),
                shipping.getChargeForDelivery());
        logger.debug("# inserted into purchase table : " + result);

        // insert to purchase_detail table
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (CartItem cartItem : cart.getReadOnlyItemList()) {
            List<Object> params = new ArrayList<Object>();
            params.add(jdbcTemplate.queryForObject(sql_seq, Long.class)); // id
            params.add(purchaseId);
            params.add(cartItem.getItem().getId());
            params.add(cartItem.getAmount());
            batchArgs.add(params.toArray());
        }
        int[] result2 = jdbcTemplate.batchUpdate(sql_ins_purchase_detail,
                batchArgs);
        logger.debug("# inserted into purchase_detail table : " + result2);

        return true;
    }

}
