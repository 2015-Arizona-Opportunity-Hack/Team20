package controller;

import java.util.ArrayList;
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
					JSONObject jsonObj=new JSONObject(jsonString);
					List<Animal> animals=mapper.scan(Animal.class, scanExpression);
					for(Animal animal:animals)
					{
					if(issue.getLocation().equalsIgnoreCase("")||issue.getDetails().toLowerCase().contains(""))
					{
					
					}
					}
				}
			}
		}
		else if(requestType.equalsIgnoreCase("found"))
		{
			//need to search in lost
		}
		else
		{
			
		}
		
		return null;
	}
}
