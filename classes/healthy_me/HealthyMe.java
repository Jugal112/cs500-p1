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
            if (form.equals("user")) {
                if (action.equals("add")) {
                    User user = new User(
                            req.getParameter("name"),
                            Integer.parseInt(req.getParameter("age")),
                            Integer.parseInt(req.getParameter("height")),
                            Integer.parseInt(req.getParameter("weight")),
                            req.getParameter("gender")
                    );
                    registerUser(user);
                }
            }
            else if (form.equals("nutrition")) {
                if (action.equals("add")) {
                    Nutrition nutrition = new Nutrition(
                            req.getParameter("name"),
                            Integer.parseInt(req.getParameter("calories"))
                    );
                    registerNutrition(nutrition);
                }
            }
        } catch (NullPointerException ne) {
            ne.printStackTrace();
            out.println(ne);
        }
    }

    public User registerUser(User newUser) {
        try {
            int user_id = 1 + DBUtils.getIntFromDB(_conn, "select max(user_id) from Users");
            newUser.set_user_id(user_id);
            String query = String.format("insert into Users values (%s, '%s', %s, %s, %s, '%s');",
                    newUser.get_user_id(),
                    newUser.get_name(),
                    newUser.get_age(),
                    newUser.get_height(),
                    newUser.get_weight(),
                    newUser.get_gender()
            );
            DBUtils.executeUpdate(_conn, query);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
        return newUser;
    }

    public Nutrition registerNutrition(Nutrition newNutrition) {
        try {
            int meal_id = 1 + DBUtils.getIntFromDB(_conn, "select max(meal_id) from Nutrition");
            newNutrition.set_meal_id(meal_id);
            String query = String.format("insert into Nutrition values (%s, %s, '%s');",
                    newNutrition.get_meal_id(),
                    newNutrition.get_calories(),
					newNutrition.get_name()
            );
            DBUtils.executeUpdate(_conn, query);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
        return newNutrition;
    }

    /**
     * Update the student's GPA in the database.
     * @param sid
     * @param gpa
     * @return
     */

    public void setGPA(int sid, double gpa) {
        User user = null;
        try {
            int cnt = DBUtils.getIntFromDB(_conn, "select count(*) from Students where sid = " + sid);
            if (cnt == 0) {
                //return user;
            }
            String query = "update Students set gpa = " + gpa + " where sid = " + sid;
            DBUtils.executeUpdate(_conn, query);

            query = "select name, gpa from Students where sid = " + sid;
            Statement st = _conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();

            //user = new User(sid, rs.getString("name"), rs.getDouble("gpa"));

            rs.close();
            st.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }
        //return user;
    }

    /**
     * Get the complete roster of students.
     * @return
     */
    public ArrayList<User> getRoster() throws SQLException {

        ArrayList<User> roster = new ArrayList<User>();

        String query = "select * from Users";

        Statement st = _conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {

            int user_id = rs.getInt("user_id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            int height = rs.getInt("height");
            int weight = rs.getInt("weight");
            String gender = rs.getString("gender");
            User user = new User(name, age, height, weight, gender);
            user.set_user_id(user_id);

            roster.add(user);
        }

        rs.close();
        st.close();

        return roster;

    }

    public void addTermsDynamicSQL(String [] terms) {

        for (int i=0; i<terms.length; i++) {
            String term = terms[i];
            try {
                String query = "insert into Terms values ('" + term + "')";
                DBUtils.executeUpdate(_conn, query);
            } catch (SQLException sqle) {
                System.out.println("Insert into Terms failed for " + term);
            }
        }
    }

    public void addTermsPreparedStatement(String [] terms) {
        try {
            String query = "insert into Terms values ( ? )";
            DBUtils.executeUpdate(_conn, query, terms);
        } catch (SQLException sqle) {
            System.out.println(sqle.toString());
        }
    }

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
