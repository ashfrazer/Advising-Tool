import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Abstracts the database and interactions to separate concerns between application logic and database (DAO design ptrn)
public class DataAccessObject {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String pw = "post"; // ADJUST TO YOUR PASSWORD

    private static DataSource createDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName("postgres");
        dataSource.setUser(user);
        dataSource.setPassword(pw);
        return dataSource;
    }

    private List<String> runQuery(String query) throws SQLException {
        List<String> result = new ArrayList<>();
        DataSource ds = createDataSource();
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                result.add(rs.getString(1));
            }
        }
        return result;
    }
    // Create courses table
    public void createTable() throws SQLException {
        String query = "CREATE TABLE courses (\n" +
                "    CRN SERIAL PRIMARY KEY,\n" +
                "    subj_code VARCHAR(4),\n" +
                "    crse_num VARCHAR(4),\n" +
                "    credit_hrs INT,\n" +
                "    semester VARCHAR(15),\n" +
                "    title VARCHAR(30),\n" +
                "    instr_name VARCHAR(25),\n" +
                "    meet_days1 VARCHAR(7),\n" +
                "    begin_time1 VARCHAR(15),\n" +
                "    end_time1 VARCHAR(15),\n" +
                "    meet_days2 VARCHAR(7),\n" +
                "    begin_time2 VARCHAR(15),\n" +
                "    end_time2 VARCHAR(15),\n" +
                "    schedule_desc VARCHAR(15),\n" +
                "    method VARCHAR(10),\n" +
                "    start_date VARCHAR(10),\n" +
                "    end_date VARCHAR(10),\n" +
                "    attribute VARCHAR(20)\n" +
                ");";
        runQuery(query);
    }

    // Get subject codes
    public List<String> getSubjects() throws SQLException {
        String query = "SELECT DISTINCT subj_code FROM courses ORDER BY subj_code";
        return runQuery(query);
    }

    // Get credit hour options
    public List<String> getCredits() throws SQLException {
        String query = "SELECT DISTINCT credit_hours FROM courses";
        return runQuery(query);
    }

    // Get courses for subject code
    public List<String> getCourses(String subjectCode) throws SQLException {
        String query = "SELECT DISTINCT title FROM courses WHERE subj_code = '" + subjectCode + "' ORDER BY title";
        return runQuery(query);
    }

    // Get semesters
    public List<String> getSemesters() throws SQLException {
        String query = "SELECT DISTINCT semester FROM courses";
        return runQuery(query);
    }

    // Get meeting days
    public List<String> getDays() throws SQLException {
        String query = "SELECT DISTINCT meet_days1 FROM courses";
        return runQuery(query);
    }

    // Get Table data
    public List<Object[]> getDataForTable(String subject, String course, String semester) throws SQLException {
        List<Object[]> courseData = new ArrayList<>();
        String query = "SELECT crn_key, title, instructor_name, meet_days1, meet_days2, begin_time1, end_time1, " +
                "begin_time2, end_time2 FROM courses WHERE subj_code = ? AND title = ? AND semester = ?;";

        try (Connection conn = createDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, subject);
            stmt.setString(2, course);
            stmt.setString(3, semester);


            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Add results to row array
                Object[] row = new Object[7];
                row[0] = rs.getString("crn_key");
                row[1] = rs.getString("title");
                row[2] = rs.getString("instructor_name");
                row[3] = rs.getString("meet_days1");
                row[5] = rs.getString("meet_days2");

                // initialize strings for formatted times
                String time1 = "";
                String time2 = "";

                // Format Time_1
                if (rs.getString("begin_time1") != null || rs.getString("end_time1") != null) {
                    String beginTime = rs.getString("begin_time1");
                    String endTime = rs.getString("end_time1");

                    beginTime = formatTime(beginTime);
                    endTime = formatTime(endTime);

                    time1 = beginTime + "-" + endTime;
                }

                // Format Time_2
                if (rs.getString("begin_time2") != null || rs.getString("end_time2") != null) {
                    String beginTime = rs.getString("begin_time2");
                    String endTime = rs.getString("end_time2");

                    beginTime = formatTime(beginTime);
                    endTime = formatTime(endTime);

                    time2 = beginTime + "-" + endTime;
                }

                // Add formatted times to array
                row[4] = time1;
                row[6] = time2;

                // Add row to list
                courseData.add(row);
            }
        }

        return courseData;
    }

    // Formats time into HH:MM
    private String formatTime(String time) {
        if (time.length() > 3) {
            return time.substring(0, 2) + ":" + time.substring(2);
        } else {
            return time.substring(0, 1) + ":" + time.substring(1);
        }
    }

    // Get Course details
    public List<String[]> getCourseDetails(String subject, String course, String semester) throws SQLException {
        List<String[]> courseDetails = new ArrayList<>();
        String query = "SELECT crn_key, title, instructor_name, credit_hours, schedule_desc, meet_days1, meet_days2, " +
                "begin_time1, end_time1, begin_time2, end_time2 FROM courses WHERE subj_code = ? AND title = ? " +
                "AND semester = ?;";

        try (Connection conn = createDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, subject);
            stmt.setString(2, course);
            stmt.setString(3, semester);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // initialize strings for formatted times
                String time1 = "";
                String time2 = "";

                // Format Time_1
                if (rs.getString("begin_time1") != null || rs.getString("end_time1") != null) {
                    String beginTime = rs.getString("begin_time1");
                    String endTime = rs.getString("end_time1");

                    beginTime = formatTime(beginTime);
                    endTime = formatTime(endTime);

                    time1 = beginTime + "-" + endTime;
                }

                // Format Time_2
                if (rs.getString("begin_time2") != null || rs.getString("end_time2") != null) {
                    String beginTime = rs.getString("begin_time2");
                    String endTime = rs.getString("end_time2");

                    beginTime = formatTime(beginTime);
                    endTime = formatTime(endTime);

                    time2 = beginTime + "-" + endTime;
                }

                String schedule1 = rs.getString("meet_days1") + " " + time1;
                String schedule2 = rs.getString("meet_days2") + " " + time2;

                String[] courseArray = new String[7];
                courseArray[0] = rs.getString("crn_key");
                courseArray[1] = rs.getString("title");
                courseArray[2] = rs.getString("instructor_name");
                courseArray[3] = rs.getString("credit_hours");

                // If schedule is empty, set to empty string to prevent "null" in course details
                if (!schedule1.trim().equalsIgnoreCase("null")) {
                    courseArray[4] = schedule1;
                }
                else {
                    courseArray[4] = " ";
                }
                if (!schedule2.trim().equalsIgnoreCase("null")) {
                    courseArray[5] = schedule2;
                }
                else {
                    courseArray[5] = " ";
                }
                courseArray[6] = rs.getString("schedule_desc");

                // Add course to courseDetails
                courseDetails.add(courseArray);
            }
        }
        return courseDetails;
    }
}
