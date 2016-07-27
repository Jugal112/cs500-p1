package healthy_me;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

public class HealthyMeServlet extends HttpServlet {
	public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		out.println("<!DOCTYPE html><html>");
		out.println("<header><h1>Healthy Me</h1></header>");
		
		//Date Range 
		out.println("<form>"
		  + "Date From: <input type='date' name='from'><br>"
		  + "Date To: <input type='date' name='to'><br>"
		  + "<input type='submit' value='Submit'>"
		  + "</form>");
		
		//Heart Rate, BMI, Avg Calories Burned, Avg Daily Calorie Intake
		out.println("<form>"
		  + "First name: <input type='text' name='fname'><br>"
		  + "Last name: <input type='text' name='lname'><br>"
		  + "<input type='submit' value='Submit'>"
		  + "</form>");
		log(out, req.getQueryString());
		out.println(req.getQueryString());
		out.println("</html>");
		out.close();
	}
				
	public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {  
		doGet(inRequest, outResponse);  
	}
	
	public void log(PrintWriter out, String message) {
		out.println("<script>console.log('"+message+"');</script>");
	}
	
	
}
