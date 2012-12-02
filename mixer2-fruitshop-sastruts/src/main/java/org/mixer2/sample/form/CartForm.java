package org.mixer2.sample.form;

import org.seasar.struts.annotation.Required;

public class CartForm {

    @Required
    public String itemId;

    @Required
    public String amount;

    public String[] amountArray;
}
