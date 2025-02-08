import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Abstracts the database and interactions to separate concerns between application logic and database
public class DataAccessObject {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String pw = "post";

    private static DataSource createDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName("postgres");
        dataSource.setUser("postgres");
        dataSource.setPassword("post");
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
}
