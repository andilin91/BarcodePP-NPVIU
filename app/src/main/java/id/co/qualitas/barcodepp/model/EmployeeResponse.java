package id.co.qualitas.barcodepp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by caroline on 3/29/2016.
 */
public class EmployeeResponse implements Serializable{
    @SerializedName("employeeId")
    String employeeId;
    @SerializedName("employeeName")
    String employeeName;
    
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
   
    
    
    
}
