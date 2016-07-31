package healthy_me;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.servlet.http.*;
/**
 *
 * An implementation of the Registrar.
 *
 * @author Julia Stoyanovich (stoyanovich@drexel.edu)
 *
 */
public class HealthyMe {

    private static Connection _conn = null;
    private static ResourceBundle _bundle;

    /**
     *
     * @param bundle - resource bundle that contains database connection information
     * @return
     */
    public String openDBConnection(String bundle) {
        _bundle = ResourceBundle.getBundle(bundle);
        return openDBConnection(
                _bundle.getString("dbUser"),
                _bundle.getString("dbPass"),
                _bundle.getString("dbSID"),
                _bundle.getString("dbHost"),
                Integer.parseInt(_bundle.getString("dbPort"))
        );
    }

    /**
     * Open the database connection.
     * @param dbUser
     * @param dbPass
     * @param dbSID
     * @param dbHost
     * @return
     */
    public String openDBConnection(String dbUser, String dbPass, String dbSID, String dbHost, int port) {

        String res="";
        if (_conn != null) {
            closeDBConnection();
        }

        try {
            _conn = DBUtils.openDBConnection(dbUser, dbPass, dbSID, dbHost, port);
            res = DBUtils.testConnection(_conn);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace(System.err);
        }
        return res;
    }

    /**
     * Close the database connection.
     */
    public void closeDBConnection() {
        try {
            DBUtils.closeDBConnection(_conn);
            System.out.println("Closed a connection");
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
    }

    /**
     * Register a new student in the database.
     * @param req
     * @return
     */
    public void executeForm(HttpServletRequest req, PrintWriter out) {
        try {
            String form = req.getParameter("form");
            String action = req.getParameter("action");

            ConditionParameters cp = new ConditionParameters();
            cp.set_first_name(req.getParameter("first_name"));
            cp.set_last_name(req.getParameter("last_name"));
            cp.set_date_x(req.getParameter("date_x"));
            cp.set_date_y(req.getParameter("date_y"));

            if (action.equals("add")) {
                if (form.equals("user")) {
                    User user = new User(
                            req.getParameter("first_name"),
                            req.getParameter("last_name"),
                            Integer.parseInt(req.getParameter("age"))
                    );
                    registerUser(user);
                }
                else if (form.equals("nutrition")) {
                    Nutrition nutrition = new Nutrition(
                            Integer.parseInt(req.getParameter("user_id")),
                            req.getParameter("food_name"),
                            req.getParameter("meal_type"),
                            Integer.parseInt(req.getParameter("calories")),
                            req.getParameter("date_x")
                    );
                    registerNutrition(nutrition);
                }
                else if (form.equals("body_stats")) {
                    out.println("doin body stats");
                    BodyStat bodyStat = new BodyStat(
                            Integer.parseInt(req.getParameter("user_id")),
                            Float.parseFloat(req.getParameter("height")),
                            Float.parseFloat(req.getParameter("weight")),
                            req.getParameter("date_x")
                    );
                    registerBodyStat(bodyStat);
                }
                else if (form.equals("activities")) {
                    Activity activity = new Activity(
                            Integer.parseInt(req.getParameter("user_id")),
                            req.getParameter("name"),
                            Integer.parseInt(req.getParameter("calories_burned")),
                            req.getParameter("date_x"),
                            req.getParameter("start_time"),
                            req.getParameter("end_time")
                    );
                    registerActivity(activity);
                }
            }
            else if (action.equals("retrieve")) {
                switch (form) {
                    case "bmi":
                        printBMI(out, cp);
                        break;
                    case "weight_difference":
                        printWeightChange(out, cp);
                        break;
                    case "calories_breakdown":
                        printCaloriesBreakdown(out, cp);
                        break;
                    case "calories_consumed":
                        printCaloriesConsumed(out, cp);
                        break;
                    case "calories_burned":
                        printCaloriesBurned(out, cp);
                        break;
                    case "average_steps":
                        printAvgSteps(out, cp);
                        break;
                    case "average_calories_burned":
                        printAvgCaloriesBurned(out, cp);
                        break;
                    case "average_calories_consumed":
                        printAvgCaloriesBurned(out, cp);
                        break;
                    case "max_hreat_rate":
                        printMaxHeartRate(out, cp);
                        break;
                    case "average_sleep_heart_rate":
                        printAvgSleepHeartRate(out, cp);
                        break;
                    case "average_resting_heart_rate":
                        printAvgRestingHeartRate(out, cp);
                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("Looks like something went wrong :(");
        }
    }

    public User registerUser(User newUser) {
        try {
            int user_id = 1 + DBUtils.getIntFromDB(_conn, "select max(user_id) from Users");
            newUser.set_user_id(user_id);
            String query = String.format("insert into Users values (%s, '%s', '%s', %s);",
                    newUser.get_user_id(),
                    newUser.get_first_name(),
                    newUser.get_last_name(),
                    newUser.get_age()
            );
            DBUtils.executeUpdate(_conn, query);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
        return newUser;
    }

    public Nutrition registerNutrition(Nutrition newNutrition) {
        try {
            int meal_id = 1 + DBUtils.getIntFromDB(_conn, "select max(meal_id) from need_Nutrition");
            newNutrition.set_meal_id(meal_id);
            String query = String.format("insert into need_Nutrition values (%s, %s, '%s', '%s', %s, '%s');",
                    newNutrition.get_meal_id(),
                    newNutrition.get_user_id(),
                    newNutrition.get_food_name(),
                    newNutrition.get_meal_type(),
                    newNutrition.get_calories(),
                    newNutrition.get_date_x()
            );
            DBUtils.executeUpdate(_conn, query);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
        return newNutrition;
    }

    public BodyStat registerBodyStat(BodyStat newBodystat) {
        try {
            int stat_id = 1 + DBUtils.getIntFromDB(_conn, "select max(stat_id) from have_BodyStats");
            newBodystat.set_stat_id(stat_id);
            String query = String.format("insert into have_BodyStats values (%s, %s, %s, %s, '%s');",
                    newBodystat.get_stat_id(),
                    newBodystat.get_user_id(),
                    newBodystat.get_height(),
                    newBodystat.get_weight(),
                    newBodystat.get_date_x()
            );
            DBUtils.executeUpdate(_conn, query);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
        return newBodystat;
    }

    public Activity registerActivity(Activity newActivity) {
        try {
            int activity_id = 1 + DBUtils.getIntFromDB(_conn, "select max(activity_id) from perform_Activities");
            newActivity.set_activity_id(activity_id);
            String query = String.format("insert into perform_Activities values (%s, %s, '%s', %s, '%s', '%s', '%s');",
                    newActivity.get_activity_id(),
                    newActivity.get_user_id(),
                    newActivity.get_name(),
                    newActivity.get_calories_burned(),
                    newActivity.get_date_x(),
                    newActivity.get_start_time(),
                    newActivity.get_end_time()
            );
            DBUtils.executeUpdate(_conn, query);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
        return newActivity;
    }

    public void printUsers(PrintWriter out) throws SQLException {
        out.println("<h2>User roster</h2>");
        out.println("<table>");
        out.println(toHTML("USER_ID", "FIRST_NAME", "LAST_NAME", "AGE"));

        String query = "select * from Users";

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {

            int user_id = rs.getInt("user_id");
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            int age = rs.getInt("age");
            User user = new User(first_name, last_name, age);
            user.set_user_id(user_id);

            out.println(user.toHTML());
        }

        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printBMI(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>BMI</h2>");
        out.println("<table>");
        out.println(toHTML("FIRST_NAME", "LAST_NAME", "DATE", "BMI"));
        String optional_condition = "";
        if (cp.get_date_x()!=null && !cp.get_date_x().isEmpty()) {
            optional_condition = String.format("and b.data_x = '%s'", cp.get_date_x());
        }

        String query = String.format(
                "select u.first_name, u.last_name, b.date_x, round(b.weight/(b.height*b.height), 3) as BMI \n" +
                        "from users u, have_bodystats b \n" +
                        "where u.user_id = b.user_id \n" +
                        "and u.first_name='%s' \n" +
                        "and u.last_name='%s'\n" +
                        "%s;",
                cp.get_first_name(),
                cp.get_last_name(),
                optional_condition
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            String date_x = rs.getString("date_x");
            float bmi = rs.getFloat("bmi");
            out.println(toHTML(first_name, last_name, date_x, bmi));
        }

        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printWeightChange(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Weight Change</h2>");
        out.println("<table>");
        out.println(toHTML("DIFFERENCE_IN_WEIGHT"));
        String query = String.format(
                "select b.weight - (select s.weight \n" +
                        "                  from have_bodystats s, users u \n" +
                        "                  where u.user_id = s.user_id \n" +
                        "                  and s.date_x = '%s' \n" +
                        "                  and u.first_name = '%s' \n" +
                        "                  and u.last_name = '%s') as difference_in_weight \n" +
                        "from have_bodystats b, users u \n" +
                        "where u.user_id = b.user_id \n" +
                        "and b.date_x = '%s' \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s';",
                cp.get_date_y(),
                cp.get_first_name(),
                cp.get_last_name(),
                cp.get_date_x(),
                cp.get_first_name(),
                cp.get_last_name()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String difference_in_weight = rs.getString("difference_in_weight");
            out.println(toHTML(difference_in_weight));
        }

        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printCaloriesBreakdown(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Calorie Breakdown</h2>");
        out.println("<table>");
        out.println(toHTML("FOOD_NAME", "MEAL_TYPE", "CALORIES"));
        String query = String.format(
                "select n.food_name, n.meal_type, n.calories \n" +
                        "from users u, need_nutrition n \n" +
                        "where u.user_id = n.user_id \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "and date_x = '%s';",
                cp.get_first_name(),
                cp.get_last_name(),
                cp.get_date_x()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String food_name = rs.getString("food_name");
            String meal_type = rs.getString("meal_type");
            int calories = rs.getInt("calories");
            out.println(toHTML(food_name, meal_type, calories));
        }

        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printCaloriesConsumed(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Calories Consumed</h2>");
        out.println("<table>");
        String optional_condition = "";
        if (cp.get_date_x()!=null && !cp.get_date_x().isEmpty()) {
            optional_condition = String.format("and n.date_x = '%s'", cp.get_date_x());
        }

        out.println(toHTML("FIRST_NAME", "LAST_NAME", "DATE_X", "TOTAL_CALORIES_CONSUMED"));
        String query = String.format(
                "select u.first_name, u.last_name, n.date_x, sum(n.calories) as total_calories_consumed \n" +
                        "from users u, need_nutrition n \n" +
                        "where u.user_id = n.user_id \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "%s \n" +
                        "group by u.first_name, u.last_name, n.date_x;",
                cp.get_first_name(),
                cp.get_last_name(),
                optional_condition
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String first_name = rs.getString("first_name");
            String last_name= rs.getString("last_name");
            String date_x= rs.getString("date_x");
            int total_calories_consumed = rs.getInt("total_calories_consumed");
            out.println(toHTML(first_name, last_name, date_x, total_calories_consumed));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printCaloriesBurned(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Calories Burned</h2>");
        out.println("<table>");

        String optional_condition = "";
        if (cp.get_date_x()!=null && !cp.get_date_x().isEmpty()) {
            optional_condition = String.format("and a.date_x = '%s'", cp.get_date_x());
        }

        out.println(toHTML("FIRST_NAME", "LAST_NAME", "DATE_X", "TOTAL_CALORIES_BURNED"));
        String query = String.format(
                "select u.first_name, u.last_name, s.date_x, sum(a.calories_burned)+sum(s.calories_burned) as total_calories_burned \n" +
                        "from users u, perform_activities a, walk_steps s \n" +
                        "where u.user_id = a.activity_id\n" +
                        "and u.user_id = s.step_id\n" +
                        "and a.date_x = s.date_x \n" +
                        "%s \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "group by u.first_name, u.last_name, s.date_x;",
                optional_condition,
                cp.get_first_name(),
                cp.get_last_name()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String first_name = rs.getString("first_name");
            String last_name= rs.getString("last_name");
            String date_x= rs.getString("date_x");
            int total_calories_consumed = rs.getInt("total_calories_burned");
            out.println(toHTML(first_name, last_name, date_x, total_calories_consumed));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printAvgSteps(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Average Steps</h2>");
        out.println("<table>");

        out.println(toHTML("AVG_STEPS"));
        String query = String.format(
                "select round(avg(s.num_steps), 2) as avg_steps \n" +
                        "from users u, walk_steps s \n" +
                        "where u.user_id = s.user_id \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "and s.date_x >= '%s' \n" +
                        "and s.date_x <= '%s'; ",
                cp.get_first_name(),
                cp.get_last_name(),
                cp.get_date_x(),
                cp.get_date_y()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int avg_steps = rs.getInt("avg_steps");
            out.println(toHTML(avg_steps));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printAvgCaloriesBurned(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Average Calories Burned</h2>");
        out.println("<table>");

        out.println(toHTML("DATE_X", "AVG_CALORIES_BURNED"));
        String query = String.format(
                "select a.date_x, round(avg(a.calories_burned), 2) as avg_calories_burned \n" +
                        "from users u, perform_activities a \n" +
                        "where u.user_id = a.user_id \n" +
                        "and a.date_x >= '%s' \n" +
                        "and a.date_x <= '%s' \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "group by a.date_x\n" +
                        "order by a.date_x;",
                cp.get_date_x(),
                cp.get_date_y(),
                cp.get_first_name(),
                cp.get_last_name()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String date_x = rs.getString("date_x");
            int avg_calories_burned = rs.getInt("avg_calories_burned");
            out.println(toHTML(date_x, avg_calories_burned));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printAvgCaloriesConsumed(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Average Calories Per Meal</h2>");
        out.println("<table>");

        out.println(toHTML("DATE_X", "AVG_CALORIES_PER_MEAL", "NUM_MEALS"));
        String query = String.format(
                "select n.date_x, round(avg(n.calories), 2) as avg_calories_per_meal, count(n.user_id) as num_meals \n" +
                        "from users u, need_nutrition n \n" +
                        "where u.user_id = n.user_id \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "and n.date_x >= '%s' \n" +
                        "and n.date_x <= '%s' \n" +
                        "group by n.date_x\n" +
                        "order by n.date_x; ",
                cp.get_first_name(),
                cp.get_last_name(),
                cp.get_date_x(),
                cp.get_date_y()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String date_x = rs.getString("date_x");
            int avg_calories_per_meal = rs.getInt("avg_calories_per_meal");
            int num_meals = rs.getInt("num_meals");
            out.println(toHTML(date_x, avg_calories_per_meal, num_meals));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printMaxHeartRate(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Maximum Active Heart Rate Per Day</h2>");
        out.println("<table>");

        out.println(toHTML("FIRST_NAME", "LAST_NAME", "DATE_X", "START_TIME", "END_TIME", "MAX_HEART_RATE"));
        String query = String.format(
                "select u.first_name, u.last_name, h.date_x, a.start_time, a.end_time, max(h.heart_rate) as max_heart_rate \n" +
                        "from users u, have_heartrate h, perform_activities a \n" +
                        "where u.user_id = h.user_id \n" +
                        "and u.user_id = a.user_id \n" +
                        "and a.date_x = h.date_x \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "and h.date_x >= '%s' \n" +
                        "and h.date_x <= '%s' \n" +
                        "and h.start_time >= a.start_time \n" +
                        "and h.end_time <= a.end_time \n" +
                        "group by u.first_name, u.last_name, h.date_x, a.start_time, a.end_time\n" +
                        "order by h.date_x;",
                cp.get_first_name(),
                cp.get_last_name(),
                cp.get_date_x(),
                cp.get_date_y()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            String date_x = rs.getString("date_x");
            String start_time = rs.getString("start_time");
            String end_time = rs.getString("end_time");
            int max_heart_rate = rs.getInt("max_heart_rate");
            out.println(toHTML(first_name, last_name, date_x, start_time, end_time, max_heart_rate));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printAvgSleepHeartRate(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Average Heart Rate During Sleep</h2>");
        out.println("<table>");

        out.println(toHTML("DATE_X", "START_TIME", "END_TIME", "AVG_HEART_RATE"));
        String query = String.format(
                "select h.date_x, s.start_time, s.end_time, round(avg(h.heart_rate), 2) as avg_heart_rate \n" +
                        "from users u, have_heartrate h, need_sleep s\n" +
                        "where u.user_id = h.user_id \n" +
                        "and u.user_id = s.user_id \n" +
                        "and s.date_x = h.date_x \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "and h.date_x >= '%s' \n" +
                        "and h.date_x <= '%s' \n" +
                        "and h.start_time >= s.start_time \n" +
                        "and h.end_time <= s.end_time \n" +
                        "group by u.first_name, u.last_name, h.date_x, s.start_time, s.end_time\n" +
                        "order by h.date_x;",
                cp.get_first_name(),
                cp.get_last_name(),
                cp.get_date_x(),
                cp.get_date_y()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String date_x = rs.getString("date_x");
            String start_time = rs.getString("start_time");
            String end_time = rs.getString("end_time");
            int avg_heart_rate = rs.getInt("avg_heart_rate");
            out.println(toHTML(date_x, start_time, end_time, avg_heart_rate));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public void printAvgRestingHeartRate(PrintWriter out, ConditionParameters cp) throws SQLException {
        out.println("<h2>Average Resting Heart Rate Per Day</h2>");
        out.println("<table>");

        out.println(toHTML("DATE_X", "AVG_RESTING_HEART_RATE"));
        String query = String.format(
                "select h.date_x, round(avg(h.heart_rate), 2) as resting_heart_rate \n" +
                        "from users u, have_heartrate h \n" +
                        "where u.user_id = h.user_id \n" +
                        "and u.first_name = '%s' \n" +
                        "and u.last_name = '%s' \n" +
                        "group by h.date_x \n" +
                        "order by h.date_x;",
                cp.get_first_name(),
                cp.get_last_name()
        );

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String date_x = rs.getString("date_x");
            int resting_heart_rate = rs.getInt("resting_heart_rate");
            out.println(toHTML(date_x, resting_heart_rate));
        }
        rs.close();
        st.close();
        out.println("<table>");
    }

    public String toHTML(Object... objects) {
        String html = "<tr>";
        for (int i=0; i< objects.length; i++) {
            html += String.format("<td>%s</td>", objects[i].toString());
        }
        html += "</tr>";
        return html;
    }

    /**
     * Get the complete roster of students.
     * @return
     */
//    public ArrayList<User> getRoster() throws SQLException {
//
//        ArrayList<User> roster = new ArrayList<User>();
//
//        String query = "select * from Users";
//
//        Statement st = _conn.createStatement();
//        ResultSet rs = st.executeQuery(query);
//
//        while (rs.next()) {
//
//            int user_id = rs.getInt("user_id");
//            String first_name = rs.getString("first_name");
//            String last_name = rs.getString("last_name");
//            int age = rs.getInt("age");
//            User user = new User(first_name, last_name, age);
//            user.set_user_id(user_id);
//
//            roster.add(user);
//        }
//
//        rs.close();
//        st.close();
//
//        return roster;
//
//    }
//
//    /**
//     * Update the student's GPA in the database.
//     * @param sid
//     * @param gpa
//     * @return
//     */
//
//    public void setGPA(int sid, double gpa) {
//        User user = null;
//        try {
//            int cnt = DBUtils.getIntFromDB(_conn, "select count(*) from Students where sid = " + sid);
//            if (cnt == 0) {
//                //return user;
//            }
//            String query = "update Students set gpa = " + gpa + " where sid = " + sid;
//            DBUtils.executeUpdate(_conn, query);
//
//            query = "select name, gpa from Students where sid = " + sid;
//            Statement st = _conn.createStatement();
//            ResultSet rs = st.executeQuery(query);
//            rs.next();
//
//            //user = new User(sid, rs.getString("name"), rs.getDouble("gpa"));
//
//            rs.close();
//            st.close();
//        } catch (SQLException sqle) {
//            sqle.printStackTrace(System.err);
//        }
//        //return user;
//    }

//    public void addTermsDynamicSQL(String [] terms) {
//        for (int i=0; i<terms.length; i++) {
//            String term = terms[i];
//            try {
//                String query = "insert into Terms values ('" + term + "')";
//                DBUtils.executeUpdate(_conn, query);
//            } catch (SQLException sqle) {
//                System.out.println("Insert into Terms failed for " + term);
//            }
//        }
//    }
//
//    public void addTermsPreparedStatement(String [] terms) {
//        try {
//            String query = "insert into Terms values ( ? )";
//            DBUtils.executeUpdate(_conn, query, terms);
//        } catch (SQLException sqle) {
//            System.out.println(sqle.toString());
//        }
//    }
//
//    public static void main (String args[]) {
//
//        if (args.length != 1) {
//            System.out.println("Not enough arguments: Registrar bundle");
//        }
//
//        Registrar reg = new Registrar();
//        try {
//
//            String response = reg.openDBConnection(args[0].trim());
//
//            System.out.println(response);
//
//            Student newStudent = reg.registerStudent(new Student("Julia"));
//            System.out.println("\nRegistered a new student: " + newStudent.toString());
//
//            newStudent = reg.setGPA(newStudent.getId(), 3.9);
//            System.out.println("\nUpdated GPA for student: " + newStudent.toString());
//
//            ArrayList<Student> roster = reg.getRoster();
//
//            System.out.println("\nPrinting the roster");
//            for (Student student : roster) {
//                System.out.println(student.toString());
//            }
//
//            String [] terms = {"Summer 2010", "Fall 2010", "Spring 2011", "Summer 2011"};
//            reg.addTermsDynamicSQL(terms);
//
//            String [] moreTerms = {"Summer 2012", "Fall 2012"};
//            reg.addTermsPreparedStatement(moreTerms);
//
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//        } catch (RuntimeException rte) {
//            rte.printStackTrace();
//        } finally {
//            reg.closeDBConnection();
//        }
//    }
}
