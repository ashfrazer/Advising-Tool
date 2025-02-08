import java.sql.SQLException;
import java.util.List;

public class BackEnd {

    private DataAccessObject dataAccessObject;

    public BackEnd() {
        this.dataAccessObject = new DataAccessObject();
    }

    // Get subjects for Front End
    public List<String> getSubjects() throws SQLException {
        return dataAccessObject.getSubjects();
    }

    public List<String> getCredits() throws SQLException {
        return dataAccessObject.getCredits();
    }

    // Get courses by subject
    public List<String> getCourses(String subjectCode) throws SQLException {
        return dataAccessObject.getCourses(subjectCode);
    }
}