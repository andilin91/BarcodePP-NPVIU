package id.co.qualitas.barcodepp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by caroline on 3/29/2016.
 */
public class WSMessageEmployee implements Serializable{
	@SerializedName("idMessage")
    int messageId;
    @SerializedName("message")
    String message;
    @SerializedName("result")
    EmployeeResponse resultEmployee;
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public EmployeeResponse getResultEmployee() {
		return resultEmployee;
	}
	public void setResultEmployee(EmployeeResponse resultEmployee) {
		this.resultEmployee = resultEmployee;
	}
}
