package id.co.qualitas.barcodepp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Acien on 9/11/17.
 */

public class PWOResponse implements Serializable{

    @SerializedName("pwoNo")
    private String pwoNo;
	@SerializedName("operation")
	private String operationNumber;
	@SerializedName("resource")
	private String resource;
	@SerializedName("listOperation")
	private List<Operation> listOperation;
    @SerializedName("startTime")
    private String startTime;
    @SerializedName("stopDate")
    private String finishDate;
    @SerializedName("stopTime")
    private String finishTime;
    @SerializedName("planningDate")
    private String planningDate;
	@SerializedName("manuDate")
	private String manuDate;
    @SerializedName("yield")
    private double yield;
    @SerializedName("timeStart")
    private String timeStart;
    @SerializedName("postingDate")
    private String postingDate;
    @SerializedName("partial")
    private boolean isPartial;
    @SerializedName("prodType")
    private String prodType;
    @SerializedName("idEmployee")
    private String idEmployee;
    @SerializedName("duration")
    private String duration;
    @SerializedName("planningQty")
    private double planningQuantity;
    @SerializedName("confNo")
    private String confNo;
    @SerializedName("confNoSap")
    private String confNoSap;
    @SerializedName("messageId")
    int messageId;
    @SerializedName("message")
    String message;
    
    @SerializedName("productId")
    String productId;
    @SerializedName("productName")
    String productName;
    @SerializedName("step")
    int step;
    @SerializedName("listEmployee")
    List<ListEmployee> listEmployee;
    @SerializedName("sloc")
    private String sloc;
    @SerializedName("batch")
    private String batch;
    @SerializedName("uom")
	private String uom;
	@SerializedName("plant")
	private String plant;

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getOperationNumber() {
		return operationNumber;
	}

	public void setOperationNumber(String operationNumber) {
		this.operationNumber = operationNumber;
	}

	public String getSloc() {
		return sloc;
	}
	public void setSloc(String sloc) {
		this.sloc = sloc;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getConfNoSap() {
		return confNoSap;
	}
	public void setConfNoSap(String confNoSap) {
		this.confNoSap = confNoSap;
	}
	public String getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}
	public String getPlanningDate() {
		return planningDate;
	}
	public void setPlanningDate(String planningDate) {
		this.planningDate = planningDate;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getManuDate() {
		return manuDate;
	}

	public void setManuDate(String manuDate) {
		this.manuDate = manuDate;
	}

	public List<ListEmployee> getListEmployee() {
		return listEmployee;
	}
	public void setListEmployee(List<ListEmployee> listEmployee) {
		this.listEmployee = listEmployee;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
    
	public String getConfNo() {
		return confNo;
	}
	public void setConfNo(String confNo) {
		this.confNo = confNo;
	}
	public double getPlanningQuantity() {
		return planningQuantity;
	}
	public void setPlanningQuantity(double planningQuantity) {
		this.planningQuantity = planningQuantity;
	}
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
	public String getPwoNo() {
		return pwoNo;
	}
	public void setPwoNo(String pwoNo) {
		this.pwoNo = pwoNo;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public double getYield() {
		return yield;
	}
	public void setYield(double yield) {
		this.yield = yield;
	}
	public String getPostingDate() {
		return postingDate;
	}
	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}
	public boolean isPartial() {
		return isPartial;
	}
	public void setPartial(boolean isPartial) {
		this.isPartial = isPartial;
	}
	public String getProdType() {
		return prodType;
	}
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	public String getIdEmployee() {
		return idEmployee;
	}
	public void setIdEmployee(String idEmployee) {
		this.idEmployee = idEmployee;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}

	public List<Operation> getListOperation() {
		return listOperation;
	}

	public void setListOperation(List<Operation> listOperation) {
		this.listOperation = listOperation;
	}
}
