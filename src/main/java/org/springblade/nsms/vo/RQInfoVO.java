package org.springblade.nsms.vo;

/**
 * @author Eugene-Forest
 * @date 2022/4/14
 **/
public class RQInfoVO {
	private String rq;
	private String message;

	public RQInfoVO() {
	}

	public RQInfoVO(String rq, String message) {
		this.rq = rq;
		this.message = message;
	}

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
