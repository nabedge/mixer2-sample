package org.mixer2.sample.form;

import java.math.BigDecimal;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Required;

@Component(instance = InstanceType.SESSION)
public class CheckoutForm implements java.io.Serializable {

    private static final long serialVersionUID = 5144716583057765995L;

    @Required(target = "confirm")
    public String firstName;

    @Required(target = "confirm")
    public String lastName;

    @Required(target = "confirm")
    public String zipCode;

    @Required(target = "confirm")
    public String address;

    public BigDecimal chargeForDelivery = new BigDecimal("3.00");

    // must be same property name in Const.java
    public String transactionToken;

}
