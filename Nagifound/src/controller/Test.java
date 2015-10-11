package controller;

import java.io.File;
import java.io.InputStream;

import model.Animal;
import model.Issues;
import model.User;

import org.apache.commons.io.FileUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sun.jersey.multipart.FormDataBodyPart;

public class Test {
public static void main(String args[])
{
	String fileName = "C:/Users/Admin/Downloads/Tuan.txt";
	File targetFile=new File(fileName);
    //FileUtils.copyInputStreamToFile(fileStream,targetFile);
    
  //s3 file upload part
    AWSCredentials credentials = new BasicAWSCredentials("AKIAJZVJB3Y773NIB7EA", "0Gm8tcMfHD5JXojLPbxbUvtDDANmEyIwmHs9fSW4");
    AmazonS3 s3client = new AmazonS3Client(credentials);
    
    PutObjectRequest por=new PutObjectRequest("nagifoundation", fileName, targetFile);
    por.setCannedAcl(CannedAccessControlList.PublicRead);
    s3client.putObject(por);
    
    
    
    String animalType="test";
    String sex="test";
    String color="test";
    String breed="test";
    String height="test";
    String collared="test";
    String tagged="test";
    String location="test";
    String time="test";
    
    String email="test";
    String fName="test";
    String lName="test";
    String phone="test";
    String other="test";
    ConnectionsClient client=new ConnectionsClient();
	AmazonDynamoDBClient dynamo=client.getClient();
	DynamoDBMapper mapper = new DynamoDBMapper(dynamo);
	
    Animal animal=new Animal();
    animal.setImage("https://s3.amazonaws.com/nagifoundation/"+fileName);
    animal.setType(animalType);
    animal.setFeatures(sex+","+color+","+breed+","+height+","+collared+","+tagged);
    animal.setPrimary_key(Integer.parseInt("12345"));
    mapper.save(animal);
    
    User user=new User();
    user.setEmail(email);
    user.setFirstName(fName);
    user.setLastName(lName);
    user.setPhoneNumber(phone);
    user.setDonorFlag("false");
    
    mapper.save(user);
    
    Issues issues=new Issues();
    issues.setPkAnimal("12345");
    issues.setPkUser(email);
    issues.setLocation(location);
    issues.setDetails(other);
    
    mapper.save(issues);
    
}
}
