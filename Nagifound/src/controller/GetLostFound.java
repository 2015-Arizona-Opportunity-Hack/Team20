package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Animal;
import model.Issues;
import model.User;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.google.gson.Gson;
import com.sun.jersey.multipart.FormDataMultiPart;

@Path("/getlostfound")
public class GetLostFound {
	@POST
	@Produces("application/json")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response getlostfound(FormDataMultiPart formData) {
		String requestType=formData.getField("requestType").getValue();
		String location=formData.getField("location").getValue();
		String animalType=formData.getField("type").getValue();
		String sex=formData.getField("sex").getValue();
		String color=formData.getField("color").getValue();
		String breed=formData.getField("breed").getValue();
		String height=formData.getField("height").getValue();
		String tagged=formData.getField("tagged").getValue();
		String time=formData.getField("time").getValue();
		ConnectionsClient client=new ConnectionsClient();
		AmazonDynamoDBClient dynamo=client.getClient();
		DynamoDBMapper mapper = new DynamoDBMapper(dynamo);
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
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
					JSONObject jsonObj=new JSONObject(jsonString);
					
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
							if((check_empty(issue.getLocation())&&issue.getLocation().equalsIgnoreCase(location))
									||check_range(animal.getPrimary_key(),Integer.parseInt(time))
									||(check_empty(animal.getType())&&animal.getType().equalsIgnoreCase(animalType))
									||(check_empty(animal.getFeatures())&&animal.getFeatures().contains(sex))
									||(check_empty(animal.getFeatures())&&animal.getFeatures().contains(color))){
							
							JSONObject animalobj=new JSONObject(gson.toJson(animal));
							JSONObject userobj=new JSONObject(gson.toJson(user));
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
					JSONObject jsonObj=new JSONObject(jsonString);
					
					List<Animal> animals=mapper.scan(Animal.class, scanExpression);
					for(Animal animal:animals)
					{
					if(Integer.parseInt(issue.getPkAnimal())==animal.getPrimary_key())
					{
						List<User> users=mapper.scan(User.class, scanExpression);
						for(User user:users)
						{
						if(user.getEmail().equalsIgnoreCase(issue.getPkUser()))
						{if((check_empty(issue.getLocation())&&issue.getLocation().equalsIgnoreCase(location))
								||check_range(animal.getPrimary_key(),Integer.parseInt(time))
								||(check_empty(animal.getType())&&animal.getType().equalsIgnoreCase(animalType))
								||(check_empty(animal.getFeatures())&&animal.getFeatures().contains(sex))
								||(check_empty(animal.getFeatures())&&animal.getFeatures().contains(color)))
						{
							
							JSONObject animalobj=new JSONObject(gson.toJson(animal));
							JSONObject userobj=new JSONObject(gson.toJson(user));
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
		String result = array.toString();
		return Response.status(200).entity(result).build();
	}

	private boolean check_empty(String x)
	{
		return (!(x=="")&&!(x==null));
	}
	private boolean check_range(int primary_key, int time) {
		// TODO Auto-generated method stub
		if(Math.abs(primary_key-time)<300000)
		{
			return true;
		}
		return false;
	}
}
