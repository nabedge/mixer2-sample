package org.mixer2.sample.training1.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 請求書の内容
 *
 */
public class Bill {

    /** 発行日 */
	private Date issueDate;
	/** 請求先 */
	private String destination;
	/** 但し書き */
	private String title; 
	/** 詳細 */
	private List<Detail> detailList = new ArrayList<Detail>();
	
	/**
	 * 合計金額を計算して返す
	 * @return
	 */
	public BigDecimal getCharge() {
	    BigDecimal total = new BigDecimal(0);
	    for(Detail d : detailList) {
	        total = total.add(d.getSubtotal());
	    }
	    return total;
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public List<Detail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<Detail> detailList) {
        this.detailList = detailList;
    }

}
