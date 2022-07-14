package id.co.qualitas.barcodepp.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Acien on 9/11/17.
 */

public class PWOEmployee implements Serializable {

	private PWOResponse pwoResponse;
	private EmployeeResponse employeeResponse;
	private long duration;
	private long delaytime;

	public long getDelaytime() {
		return delaytime;
	}

	public void setDelaytime(long delaytime) {
		this.delaytime = delaytime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public PWOResponse getPwoResponse() {
		return pwoResponse;
	}

	public void setPwoResponse(PWOResponse pwoResponse) {
		this.pwoResponse = pwoResponse;
	}

	public EmployeeResponse getEmployeeResponse() {
		return employeeResponse;
	}

	public void setEmployeeResponse(EmployeeResponse employeeResponse) {
		this.employeeResponse = employeeResponse;
	}

}
