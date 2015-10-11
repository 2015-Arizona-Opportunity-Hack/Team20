package controller;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Animal;
import model.Issues;
import model.User;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.gson.Gson;
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
    
    
    
    String animalType="c";
    String sex="x";
    String color="xc";
    String collared="x";
    String tagged="x";
    String location="static";
    String time="12345678";
    
    
    
    String email="x";
    String fName="x";
    String lName="x";
    String phone="x";
    String other="x";
    String requestType="found";
    ConnectionsClient client=new ConnectionsClient();
	AmazonDynamoDBClient dynamo=client.getClient();
	DynamoDBMapper mapper = new DynamoDBMapper(dynamo);
	DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
	ArrayList<Issues> allProducts=new ArrayList<Issues>();
	List<Issues> scanResults = mapper.scan(Issues.class, scanExpression);
	Gson gson=new Gson();
	JSONArray array=new JSONArray();
	if(requestType.equalsIgnoreCase("lost"))
	{
		//need to search in found
		for(Issues issue:scanResults)
		{
			if(issue.getType().equalsIgnoreCase("found"))
			{
				String jsonString=gson.toJson(issue);
				
				List<Animal> animals=mapper.scan(Animal.class, scanExpression);
				for(Animal animal:animals)
				{
				if(Integer.parseInt(issue.getPkAnimal())==animal.getPrimary_key())
				{
					List<User> users=mapper.scan(User.class, scanExpression);
					for(User user:users)
					{
					if(user.getEmail().equalsIgnoreCase(issue.getPkUser()))
					{
						if((!checkNull(issue.getLocation())&&issue.getLocation().equalsIgnoreCase(location))
								||check_range(animal.getPrimary_key(),Integer.parseInt(time))
								||(!checkNull(animal.getType())&&animal.getType().equalsIgnoreCase(animalType))
								||(!checkNull(animal.getFeatures())&&animal.getFeatures().contains(sex))
								||(!checkNull(animal.getFeatures())&&animal.getFeatures().contains(color))){
						
						JSONObject animalobj=new JSONObject(gson.toJson(animal));
						JSONObject userobj=new JSONObject(gson.toJson(user));

						JSONObject jsonObj=new JSONObject(jsonString);
						JSONObject merged = new JSONObject();
						JSONObject[] objs = new JSONObject[] { jsonObj, animalobj,userobj };
						for (JSONObject obj : objs) {
						    Iterator it = obj.keys();
						    while (it.hasNext()) {
						        String key = (String)it.next();
						        merged.put(key, obj.get(key));
						    }
						}
						array.put(merged);
						
					}
					}
					}
				}
				
				}
			}
		}
	}
	else if(requestType.equalsIgnoreCase("found"))
	{
		//need to search in found
		for(Issues issue:scanResults)
		{
			if(issue.getType().equalsIgnoreCase("lost"))
			{
				String jsonString=gson.toJson(issue);
				
				
				List<Animal> animals=mapper.scan(Animal.class, scanExpression);
				for(Animal animal:animals)
				{
				if(Integer.parseInt(issue.getPkAnimal())==animal.getPrimary_key())
				{
					List<User> users=mapper.scan(User.class, scanExpression);
					for(User user:users)
					{
					if(user.getEmail().equalsIgnoreCase(issue.getPkUser()))
					{if((issue.getLocation().equalsIgnoreCase(location))
							||check_range(animal.getPrimary_key(),Integer.parseInt(time))
							||(animal.getType().equalsIgnoreCase(animalType))
							||(animal.getFeatures().contains(sex))
							||(animal.getFeatures().contains(color)))
					{
						
						JSONObject animalobj=new JSONObject(gson.toJson(animal));
						JSONObject userobj=new JSONObject(gson.toJson(user));
						JSONObject jsonObj=new JSONObject(jsonString);
						JSONObject merged = new JSONObject();
						JSONObject[] objs = new JSONObject[] { jsonObj, animalobj,userobj };
						for (JSONObject obj : objs) {
						    Iterator it = obj.keys();
						    while (it.hasNext()) {
						        String key = (String)it.next();
						        merged.put(key, obj.get(key));
						    }
						}
						array.put(merged);
						
					}
					}
					}
				}
				
				}
			}
		}
	}
	else
	{
		
	}
	System.out.println(array.toString());
}

private static boolean check_range(int primary_key, int time) {
	// TODO Auto-generated method stub
	if(Math.abs(primary_key-time)<30)
	{
		return true;
	}
	return false;
}

private static boolean checkNull(String val)
{
	if(val.equals("")||(val==null))
	{
		return false;
	}
	else
	{
		return true;
	}
}

}
