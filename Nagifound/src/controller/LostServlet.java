package controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LostServlet
 */
@WebServlet("/AddProduct")
public class LostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//Uri selectedImage;
    double latitude, longitude;
    String type,sex,color,breed, height,collared,tagged,location,time,email,
            firstName,lastName,phone,other,issueType;
    final int TAKE_PICTURE=0;
    //Uri imageUri;
    Date today;
    String fileTime="123454321";
    public static JSONArray retArray;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LostServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	    PrintWriter out=response.getWriter();
		try{
		 type ="man" ;
         sex = "male";
         color = "black";
         breed = "sdas";
         height ="1.2";
         collared ="true" ;
         tagged = "false";
         location = "";
         time = "123454321";
         email = "sab";
         firstName ="asdas" ;
         lastName = "asdas";
         phone = "123123";
         other = "dasd";
         issueType = "lost";

         String urlStr = "http://nagifound-pcqzft2why.elasticbeanstalk.com/nagi/postlostfound";
         URL url = new URL(urlStr);
         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         connection.setDoInput(true);
         connection.setDoOutput(true);
         connection.setRequestMethod("POST");

         MultipartHelper multipart = new MultipartHelper(connection);
         updateFields(multipart);
         //File imgFile = new File("test.jpg");
         //multipart.addFilePart(imgFile,"image/jpeg","fileObject");
         multipart.addStringPart(fileTime + ".jpg", "fileName");
         multipart.makeRequest();

         //get other results for display
         urlStr = "http://nagifound-pcqzft2why.elasticbeanstalk.com/nagi/getlostfound/";
         url = new URL(urlStr);
         connection = (HttpURLConnection) url.openConnection();
         connection.setDoInput(true);
         connection.setDoOutput(true);
         connection.setRequestMethod("POST");
         multipart = new MultipartHelper(connection);
         updateFields(multipart);
         multipart.addStringPart("found", "requestType");
         multipart.makeRequest();
     
         out.print(multipart.sb);
         request.setAttribute("json", multipart.sb);
         response.sendRedirect("Display.jsp");
         
         //retArray = new JSONArray(multipart.sb.toString());

	}
	catch(Exception e)
	{
		out.print("error");
	}
	}
	public void updateFields(MultipartHelper multipart) {
        multipart.addStringPart(type, "type");
        multipart.addStringPart(sex, "sex");
        multipart.addStringPart(color, "color");
        multipart.addStringPart(breed, "breed");
        multipart.addStringPart(height, "height");
        multipart.addStringPart(collared, "collared");
        multipart.addStringPart(tagged, "tagged");
        //converting location to lat/lng
       
        location= "static";
        multipart.addStringPart(location, "location");
        multipart.addStringPart(fileTime, "time");
        multipart.addStringPart(email, "email");
        multipart.addStringPart(firstName, "firstName");
        multipart.addStringPart(lastName, "lastName");
        multipart.addStringPart(phone, "phone");
        multipart.addStringPart(other, "other");
        multipart.addStringPart(issueType, "issueType");

        
        /*multipart.addStringPart("Cat", "type");
        multipart.addStringPart("male", "sex");
        multipart.addStringPart("red", "color");
        multipart.addStringPart("gumpy", "breed");
        multipart.addStringPart("1.3", "height");
        multipart.addStringPart("true", "collared");
        multipart.addStringPart("true", "tagged");
        multipart.addStringPart("mesa", "location");
        multipart.addStringPart("54321", "time");
        multipart.addStringPart("test@gmail.com", "email");
        multipart.addStringPart("Joe", "firstName");
        multipart.addStringPart("Doe", "lastName");
        multipart.addStringPart("11111111", "phone");
        multipart.addStringPart("none", "other");
        multipart.addStringPart(issueType, "issueType");
*/
    }
}
