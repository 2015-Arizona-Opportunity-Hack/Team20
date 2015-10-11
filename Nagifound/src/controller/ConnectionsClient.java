package controller;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
/**
 * CSE 6331 Cloud Computing
 * Programming assignment 3
 * AWS, Web, Databases
 * Module: Connections Util
 *
 */
public class ConnectionsClient {
	private AmazonDynamoDBClient client;

	public ConnectionsClient()
	{
		AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();	
		AmazonDynamoDBClient dynamo = new AmazonDynamoDBClient(credentialsProvider);
		dynamo.setEndpoint("dynamodb.us-west-2.amazonaws.com");
		this.setClient(dynamo);
	}
	public AmazonDynamoDBClient getClient() {
		if(this.client==null)
		{
			new ConnectionsClient();
		}
		
		return client;
	}

	public void setClient(AmazonDynamoDBClient client) {
		this.client = client;
	}

}
