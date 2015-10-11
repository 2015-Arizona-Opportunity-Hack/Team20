package model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
@DynamoDBTable(tableName="user")
public class User {
private String email;
private String firstName;
private String lastName;
private String phoneNumber;
private String donorFlag;
@DynamoDBHashKey(attributeName="email")
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
@DynamoDBAttribute(attributeName="first_name")
public String getFirstName() {
	return firstName;
}
public void setFirstName(String firstName) {
	this.firstName = firstName;
}
@DynamoDBAttribute(attributeName="last_name")
public String getLastName() {
	return lastName;
}
public void setLastName(String lastName) {
	this.lastName = lastName;
}
@DynamoDBAttribute(attributeName="phone")
public String getPhoneNumber() {
	return phoneNumber;
}
public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
}
@DynamoDBAttribute(attributeName="donor_flag")
public String getDonorFlag() {
	return donorFlag;
}
public void setDonorFlag(String donorFlag) {
	this.donorFlag = donorFlag;
}

}
