package id.co.qualitas.barcodepp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by caroline on 3/29/2016.
 */
public class WSMessagePWO implements Serializable{
	@SerializedName("idMessage")
    int messageId;
    @SerializedName("message")
    String message;
    @SerializedName("result")
    PWOResponse resultPWO;
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
	public PWOResponse getResultPWO() {
		return resultPWO;
	}
	public void setResultPWO(PWOResponse resultPWO) {
		this.resultPWO = resultPWO;
	}
	
    
}
