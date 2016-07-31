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
        if (!_message.startsWith("Servus")) {
            out.println("<h1>Database connection failed to open " + _message + "</h1>");
        } else {
            if (req.getParameterMap().containsKey("form")) {
                _healthyme.executeForm(req, out);
            }
        }

        //Personal Information
        out.println("<form>"
                + "<h2>Add User</h2>"
                + "<input type='hidden' name='form' value='user'>"
                + "First Name: <input type='text' name='first_name'><br>"
                + "Last Name: <input type='text' name='last_name'><br>"
                + "Age: <input type='number' name='age'><br>"
                + "<input type='submit' name='action' value='add'>"
                + "</form><br>");

        //Body Stats
        out.println("<form>"
                + "<h2>Add Body Stat</h2>"
                + "<input type='hidden' name='form' value='body_stats'>"
                + "User ID: <input type='number' name='user_id'><br>"
                + "Height: <input type='number' step='any' name='height'>inches<br>"
                + "Weight: <input type='number' step='any' name='weight'>lbs<br>"
                + "Date: <input type='date' name='date_x'><br>"
                + "<input type='submit' name='action' value='add'>"
                + "</form><br>");

        //Activities
        out.println("<form>"
                + "<h2>Add Activity</h2>"
                + "<input type='hidden' name='form' value='activities'>"
                + "User ID: <input type='number' name='user_id'><br>"
                + "Exercise Name: <input type='text' name='name'><br>"
                + "Calories Burned: <input type='number' name='calories_burned'>cal(s)<br>"
                + "Date: <input type='date' name='date_x'><br>"
                + "Start Time: <input type='time' name='start_time'><br>"
                + "End Time: <input type='time' name='end_time'><br>"
                + "<input type='submit' name='action' value='add'>"
                + "</form><br>");

        //Nutrition
        out.println("<form>"
                + "<h2>Add Nutrition</h2>"
                + "<input type='hidden' name='form' value='nutrition'>"
                + "User ID: <input type='number' name='user_id'><br>"
                + "Food Name: <input type='text' name='food_name'><br>"
                + "Meal Type: <select name='meal_type'>"
                + "<option value=breakfast>Breakfast</option>"
                + "<option value=lunch>Lunch</option>"
                + "<option value=dinner>Dinner</option>"
                + "</select><br>"
                + "Calories: <input type='number' name='calories'>cal(s)<br>"
                + "Date: <input type='date' name='date_x'><br>"
                + "<input type='submit' name='action' value='add'>"
                + "</form><br>");

        //BMI
        out.println("<form>"
                + "<h2>Look at BMI Stats</h2>"
                + "<input type='hidden' name='form' value='bmi'>"
                + "First Name: <input type='text' name='first_name'><br>"
                + "Last Name: <input type='text' name='last_name'><br>"
                + "Date (optional): <input type='date' name='date_x'><br>"
                + "<input type='submit' name='action' value='retrieve'>"
                + "</form><br>");

        //Difference in Weight
        out.println("<form>"
                + "<h2>Look at Weight Change</h2>"
                + "<input type='hidden' name='form' value='weight_difference'>"
                + "First Name: <input type='text' name='first_name'><br>"
                + "Last Name: <input type='text' name='last_name'><br>"
                + "Date: <input type='date' name='date_x'><br>"
                + "Date: <input type='date' name='date_y'><br>"
                + "<input type='submit' name='action' value='retrieve'>"
                + "</form><br>");

        //Calories Breakdown
        out.println("<form>"
                + "<h2>Look at Breakdown of Calories</h2>"
                + "<input type='hidden' name='form' value='calories_breakdown'>"
                + "First Name: <input type='text' name='first_name'><br>"
                + "Last Name: <input type='text' name='last_name'><br>"
                + "Date: <input type='date' name='date_x'><br>"
                + "<input type='submit' name='action' value='retrieve'>"
                + "</form><br>");

        //Calories Consumed
        out.println("<form>"
                + "<h2>Look at Calories Consumed Per Day</h2>"
                + "<input type='hidden' name='form' value='calories_consumed'>"
                + "First Name: <input type='text' name='first_name'><br>"
                + "Last Name: <input type='text' name='last_name'><br>"
                + "Date (optional): <input type='date' name='date_x'><br>"
                + "<input type='submit' name='action' value='retrieve'>"
                + "</form><br>");

        //Calories Burned
        out.println("<form>"
                + "<h2>Look at Calories Burned Per Day</h2>"
                + "<input type='hidden' name='form' value='calories_burned'>"
                + "First Name: <input type='text' name='first_name'><br>"
                + "Last Name: <input type='text' name='last_name'><br>"
                + "Date (optional): <input type='date' name='date_x'><br>"
                + "<input type='submit' name='action' value='retrieve'>"
                + "</form><br>");

        //Average Steps
        out.println("<form>"
                + "<h2>Look at Average Daily Steps between Dates</h2>"
                + "<input type='hidden' name='form' value='average_steps'>"
                + "First Name: <input type='text' name='first_name'><br>"
                + "Last Name: <input type='text' name='last_name'><br>"
                + "Date From: <input type='date' name='date_x'><br>"
                + "Date To: <input type='date' name='date_y'><br>"
                + "<input type='submit' name='action' value='retrieve'>"
                + "</form><br>");

        //Average Calories Burned
        out.println("<form>"
                + "<h2>Look at Average Calories Burned between Dates</h2>"
                + "<input type='hidden' name='form' value='average_calories_burned'>"
                + "First Name: <input type='text' name='first_name'><br>"
                + "Last Name: <input type='text' name='last_name'><br>"
                + "Date From: <input type='date' name='date_x'><br>"
                + "Date To: <input type='date' name='date_y'><br>"
                + "<input type='submit' name='action' value='retrieve'>"
                + "</form><br>");

        //Average Calories Consumed
        out.println("<form>"
                + "<h2>Look at Average Calories Consumed Per Meal between Dates</h2>"
                + "<input type='hidden' name='form' value='average_calories_consumed'>"
                + "First Name: <input type='text' name='first_name'><br>"
                + "Last Name: <input type='text' name='last_name'><br>"
                + "Date From: <input type='date' name='date_x'><br>"
                + "Date To: <input type='date' name='date_y'><br>"
                + "<input type='submit' name='action' value='retrieve'>"
                + "</form><br>");

        //Max Heart Rate
        out.println("<form>"
                + "<h2>Look at Maximum Heart Rate between Dates</h2>"
                + "<input type='hidden' name='form' value='max_heart_rate'>"
                + "First Name: <input type='text' name='first_name'><br>"
                + "Last Name: <input type='text' name='last_name'><br>"
                + "Date From: <input type='date' name='date_x'><br>"
                + "Date To: <input type='date' name='date_y'><br>"
                + "<input type='submit' name='action' value='retrieve'>"
                + "</form><br>");

        //Average Sleep Heart Rate
        out.println("<form>"
                + "<h2>Look at Average Heart Rate during Sleep between Dates</h2>"
                + "<input type='hidden' name='form' value='average_sleep_heart_rate'>"
                + "First Name: <input type='text' name='first_name'><br>"
                + "Last Name: <input type='text' name='last_name'><br>"
                + "Date From: <input type='date' name='date_x'><br>"
                + "Date To: <input type='date' name='date_y'><br>"
                + "<input type='submit' name='action' value='retrieve'>"
                + "</form><br>");

        //Average Resting Heart Rate
        out.println("<form>"
                + "<h2>Look at Average Resting Heart Rate</h2>"
                + "<input type='hidden' name='form' value='average_resting_heart_rate'>"
                + "First Name: <input type='text' name='first_name'><br>"
                + "Last Name: <input type='text' name='last_name'><br>"
                + "<input type='submit' name='action' value='retrieve'>"
                + "</form><br>");

        out.println("</body>");
        log(out, req.getQueryString());
        out.println("</html>");
        out.close();
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
