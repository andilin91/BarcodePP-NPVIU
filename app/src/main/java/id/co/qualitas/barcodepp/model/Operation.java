package id.co.qualitas.barcodepp.model;

import java.io.Serializable;

/**
 * Created by Acien on 9/11/17.
 */

public class Operation implements Serializable {

	private String pwoNo;
	private String operation;
	private String confNo;
	private String status;

	public String getPwoNo() {
		return pwoNo;
	}

	public void setPwoNo(String pwoNo) {
		this.pwoNo = pwoNo;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getConfNo() {
		return confNo;
	}

	public void setConfNo(String confNo) {
		this.confNo = confNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
