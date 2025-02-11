import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class BackEnd {

    private DataAccessObject dataAccessObject;

    public BackEnd() {
        this.dataAccessObject = new DataAccessObject();
    }

    // Get subjects
    public List<String> getSubjects() throws SQLException {
        return dataAccessObject.getSubjects();
    }

    // Get credits
    public List<String> getCredits() throws SQLException {
        return dataAccessObject.getCredits();
    }

    // Get courses by subject
    public List<String> getCourses(String subjectCode) throws SQLException {
        return dataAccessObject.getCourses(subjectCode);
    }

    // Get semesters
    public List<String> getSemesters() throws SQLException {
        return dataAccessObject.getSemesters();
    }

    // Get days
    public List<String> getDays() throws SQLException {
        return dataAccessObject.getDays();
    }

    // Get Table data
    public List<Object[]> getDataForTable(String subject, String course, String semester) throws SQLException {
        return dataAccessObject.getDataForTable(subject, course, semester);
    }

    // Get Course details
    public List<String[]> getCourseDetails(String subject, String course, String semester) throws SQLException {
        return dataAccessObject.getCourseDetails(subject, course, semester);
    }
}