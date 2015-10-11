package controller;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
import com.sun.jersey.multipart.FormDataMultiPart;

@Path("/postlostfound")
public class PostLostFound {

	@POST
	@Produces("application/json")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postlostfound(FormDataMultiPart formData) {
		
		try {
			//get data from the multipart form data
			String animalType=formData.getField("type").getValue();
            String sex=formData.getField("sex").getValue();
            String color=formData.getField("color").getValue();
            String breed=formData.getField("breed").getValue();
            String height=formData.getField("height").getValue();
            String collared=formData.getField("collared").getValue();
            String tagged=formData.getField("tagged").getValue();
            String location=formData.getField("location").getValue();
            String time=formData.getField("time").getValue();
            
            String email=formData.getField("email").getValue();
            String fName=formData.getField("firstName").getValue();
            String lName=formData.getField("lastName").getValue();
            String phone=formData.getField("phone").getValue();
            String other=formData.getField("other").getValue();
			
			//file processing
			FormDataBodyPart n = formData.getField("fileName");
			String fileName = n.getValue();
			FormDataBodyPart p = formData.getField("fileObject");
			int fileAttached=0;
			if((fileName==null)||(p==null)||(fileName.equals(""))||(fileName.equals("dummy")))
			{
				
			}
			else
			{
			InputStream fileStream = p.getValueAs(InputStream.class);
			File targetFile=new File(fileName);
            FileUtils.copyInputStreamToFile(fileStream,targetFile);
            
            //s3 file upload part
            AWSCredentials credentials = new BasicAWSCredentials("AKIAJZVJB3Y773NIB7EA", "0Gm8tcMfHD5JXojLPbxbUvtDDANmEyIwmHs9fSW4");
	        AmazonS3 s3client = new AmazonS3Client(credentials);
	        
	        PutObjectRequest por=new PutObjectRequest("nagifoundation", fileName, targetFile);
            por.setCannedAcl(CannedAccessControlList.PublicRead);
            s3client.putObject(por);
            fileAttached=1;
			}
            
            String issueType=formData.getField("issueType").getValue();
            ConnectionsClient client=new ConnectionsClient();
    		AmazonDynamoDBClient dynamo=client.getClient();
    		DynamoDBMapper mapper = new DynamoDBMapper(dynamo);
    		
            Animal animal=new Animal();
            if(fileAttached==1)
            {animal.setImage("https://s3.amazonaws.com/nagifoundation/"+fileName);}
            animal.setType(animalType);
            animal.setFeatures("sex: "+sex+", color: "+color+", breed: "+breed+", height: "+height+", collared: "+collared+", tagged: "+tagged);
            animal.setPrimary_key(Integer.parseInt(time));
            mapper.save(animal);
            
            User user=new User();
            user.setEmail(email);
            user.setFirstName(fName);	
            user.setLastName(lName);
            user.setPhoneNumber(phone);
            user.setDonorFlag("false");
            
            mapper.save(user);
            
            Issues issues=new Issues();
            issues.setPrimary_key(Integer.parseInt(time));
            issues.setPkAnimal(time);
            issues.setPkUser(email);
            issues.setLocation(location);
            issues.setDetails(other);
            issues.setType(issueType);
            
            mapper.save(issues);
            
		}
		catch(Exception e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			String result = "error"+exceptionAsString;
			return Response.status(200).entity(result).build();
		}
			finally {
			formData.cleanup();
			}
		String result = "THis is cool";
		return Response.status(200).entity(result).build();
	}
}
