package com.softradix.network.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

	@SerializedName("driver_license_number")
	private String driverLicenseNumber;

	@SerializedName("last_updated")
	private String lastUpdated;

	@SerializedName("date_created")
	private String dateCreated;

	@SerializedName("address_id")
	private String addressId;

	@SerializedName("driver_license_expiry_date")
	private String driverLicenseExpiryDate;

	@SerializedName("number")
	private String number;

	@SerializedName("name")
	private String name;

	@SerializedName("insurance_expiry_date")
	private String insuranceExpiryDate;

	@SerializedName("information")
	private String information;

	@SerializedName("image_file")
	private String imageFile;

	@SerializedName("department")
	private String department;

	@SerializedName("mobile_number")
	private String mobileNumber;

	@SerializedName("email")
	private String email;

	@SerializedName("status")
	private String status;

	public void setDriverLicenseNumber(String driverLicenseNumber){
		this.driverLicenseNumber = driverLicenseNumber;
	}

	public String getDriverLicenseNumber(){
		return driverLicenseNumber;
	}

	public void setLastUpdated(String lastUpdated){
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdated(){
		return lastUpdated;
	}

	public void setDateCreated(String dateCreated){
		this.dateCreated = dateCreated;
	}

	public String getDateCreated(){
		return dateCreated;
	}

	public void setAddressId(String addressId){
		this.addressId = addressId;
	}

	public String getAddressId(){
		return addressId;
	}

	public void setDriverLicenseExpiryDate(String driverLicenseExpiryDate){
		this.driverLicenseExpiryDate = driverLicenseExpiryDate;
	}

	public String getDriverLicenseExpiryDate(){
		return driverLicenseExpiryDate;
	}

	public void setNumber(String number){
		this.number = number;
	}

	public String getNumber(){
		return number;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setInsuranceExpiryDate(String insuranceExpiryDate){
		this.insuranceExpiryDate = insuranceExpiryDate;
	}

	public String getInsuranceExpiryDate(){
		return insuranceExpiryDate;
	}

	public void setInformation(String information){
		this.information = information;
	}

	public String getInformation(){
		return information;
	}

	public void setImageFile(String imageFile){
		this.imageFile = imageFile;
	}

	public String getImageFile(){
		return imageFile;
	}

	public void setDepartment(String department){
		this.department = department;
	}

	public String getDepartment(){
		return department;
	}

	public void setMobileNumber(String mobileNumber){
		this.mobileNumber = mobileNumber;
	}

	public String getMobileNumber(){
		return mobileNumber;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}