package id.co.qualitas.barcodepp.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Acien on 9/11/17.
 */

public class ListEmployee implements Serializable {

	@SerializedName("employeeId")
    String idEmployee;
	@SerializedName("yield")
    String yield;
	@SerializedName("opNo")
	String opNo;
	@SerializedName("duration")
    String duration;
	
	
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getIdEmployee() {
		return idEmployee;
	}
	public void setIdEmployee(String idEmployee) {
		this.idEmployee = idEmployee;
	}
	public String getYield() {
		return yield;
	}
	public void setYield(String yield) {
		this.yield = yield;
	}

	public String getOpNo() {
		return opNo;
	}

	public void setOpNo(String opNo) {
		this.opNo = opNo;
	}
}
