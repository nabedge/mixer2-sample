package org.mixer2.sample.training1.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bill {

	private Date issueDate; // 発行日
	private boolean reissue = false; // 再発行か否か
	private String destination; // 請求先
	private String title; // 但し書き
	private List<Detail> detailList = new ArrayList<Detail>();
	
	public BigDecimal calcCharge() {
	    BigDecimal total = new BigDecimal(0);
	    for(Detail d : detailList) {
	        total = total.add(d.calcSubtotal());
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

    public boolean isReissue() {
        return reissue;
    }

    public void setReissue(boolean reissue) {
        this.reissue = reissue;
    }


}
