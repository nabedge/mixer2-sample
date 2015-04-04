package org.mixer2.sample.training1.bean;

import java.math.BigDecimal;

public class Detail {

    private String productName; // 商品名

    private int count = 0; // 数量
    
    private BigDecimal unitPrice = new BigDecimal(0); //単価
    
    /** velocityだとBigDecimalを直接表示するのが面倒くさいので専用の文字列化メソッドを用意しておく */
    public String getSubTotalAsString() {
        return calcSubtotal().toPlainString();
    }
    
    /**
     * 単価x数量を返す
     * @return
     */
    public BigDecimal calcSubtotal() {
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
