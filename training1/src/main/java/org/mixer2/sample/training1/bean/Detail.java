package org.mixer2.sample.training1.bean;

import java.math.BigDecimal;

/**
 * 請求の商品毎の詳細
 */
public class Detail {

    /** 商品名 */
    private String productName;

    /** 数量 */
    private int count = 0;
    
    /** 単価 */
    private BigDecimal unitPrice = new BigDecimal(0); //単価
    
    /**
     * 単価x数量を返す
     * @return
     */
    public BigDecimal getSubtotal() {
        return unitPrice.multiply(new BigDecimal(count));
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
}
