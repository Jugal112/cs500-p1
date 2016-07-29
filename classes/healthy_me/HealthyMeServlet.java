package healthy_me;

import java.io.*;
import java.util.ArrayList;
import java.sql.SQLException;
import javax.servlet.http.*;
import javax.servlet.*;

public class HealthyMeServlet extends HttpServlet {

    private HealthyMe _healthyme;
    private String _message;

    public void init() throws ServletException {
        _healthyme = new HealthyMe();
        _message = _healthyme.openDBConnection("PgBundle");
    }

    public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        //_healthyme.executeForm(req);
        out.println("<!DOCTYPE html><html>");
        out.println("<head><title>Healthy Me</title></head>");
        out.println("<body>");
        out.println("<h1>Healthy Me</h1>");
        _healthyme.executeForm(req, out);
        //Date Range
        out.println("<form>"
                + "<h2>Enter Dates Here</h2>"
                + "<input type='hidden' name='form' value='dates'>"
                + "Date From: <input type='date' name='from'><br>"
                + "Date To: <input type='date' name='to'><br>"
                + "<input type='submit' value='Submit'>"
                + "</form><br>");

        //Personal Information
        out.println("<form>"
                + "<h2>Enter Personal Information Here</h2>"
                + "<input type='hidden' name='form' value='user'>"
                + "Name: <input type='text' name='name'><br>"
                + "Age: <input type='number' name='age'><br>"
                + "Height in inches: <input type='number' name='height'>inches<br>"
                + "Weight in lbs: <input type='number' name='weight'>lbs<br>"
                + "<input type='radio' name='gender' value='M'> Male<br>"
                + "<input type='radio' name='gender' value='F'> Female<br>"
                + "<input type='radio' name='gender' value='O'> Other<br>"
                + "<input type='submit' name='action' value='add'>"
                + "</form><br>");

        //Heart Rate, BMI, Avg Calories Burned, Avg Daily Calorie Intake
        out.println("<form>"
                + "<h2>Enter Personal Information Here</h2>"
                + "<input type='hidden' name='form' value='results'>"
                + "Resting Heart Rate: <input type='text' name='rhr'><br>"
                + "BMI: <input type='text' name='bmi'><br>"
                + "Avg Calories Burned: <input type='text' name='acb'><br>"
                + "Avg Daily Calories Intake: <input type='text' name='adc'><br>"
                + "Avg Steps Per Day: <input type='text' name='asd'><br>"
                + "<input type='submit' value='Submit'>"
                + "</form><br>");

        //Nutrition
        out.println("<form>"
                + "<h2>Enter Nutrition Here</h2>"
                + "<input type='hidden' name='form' value='nutrition'>"
                + "Food Name: <input type='text' name='name'><br>"
                + "Calories: <input type='number' name='calories'>cal(s)<br>"
                + "<input type='submit' name='action' value='add'>"
                + "</form><br>");

        //Activities
        out.println("<form>"
                + "<h2>Enter Activity Here</h2>"
                + "<input type='hidden' name='form' value='activities'>"
                + "Calories Burned: <input type='number' name='fname'>cal(s)<br>"
                + "Exercise Type: <input type='text' name='name'><br>"
                + "Avg Heart Rate: <input type='number' name='ahr'>bpm<br>"
                + "Date: <input type='date' name='date'><br>"
                + "Time: <input type='time' name='time'><br>"
                + "<input type='submit' name='action' value='add'>"
                + "</form><br>");

        if (!_message.startsWith("Servus")) {
            out.println("<h1>Database connection failed to open " + _message + "</h1>");
        } else {
            printStudentRoster(out);
        }

        out.println("</table>");
        out.println("</body>");
        log(out, req.getQueryString());
        out.println("</html>");
        out.close();
    }

    public void printStudentRoster(PrintWriter out) {
        out.println("<h2>User roster</h2>");
        out.println("<table>");
        try {
            ArrayList<User> roster = _healthyme.getRoster();
            for (int i=0; i<roster.size(); i++) {
                User user = (User) roster.get(i);
                out.println(user.toHTML());
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace(out);
        }

    }

    public void doPost(HttpServletRequest inRequest, HttpServletResponse outResponse) throws ServletException, IOException {
        doGet(inRequest, outResponse);
    }

    public void log(PrintWriter out, String message) {
        out.println("<script>console.log('"+message+"');</script>");
    }

    public void destroy() {
        _healthyme.closeDBConnection();
    }
}
