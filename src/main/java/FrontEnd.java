import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FrontEnd {
    private BackEnd backEnd;
    private JTable courseTable;
    private DefaultTableModel tableModel;

    public FrontEnd() {
        backEnd = new BackEnd();

        try {
            // Get subjects
            List<String> subjectsList = backEnd.getSubjects();

            // Get courses by subject
            List<String> courseList = new ArrayList<>();

            for (String subject : subjectsList) {
                courseList.addAll(backEnd.getCourses(subject));
            }

            // Get semesters
            List<String> semesterList = backEnd.getSemesters();

            // Initialize GUI
            startGUI(subjectsList, courseList, semesterList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void startGUI(List<String> subjectList, List<String> courseList, List<String> semesterList) {
        ////////////////////////////////////////////////
        //      Frame Configuration
        ////////////////////////////////////////////////

        // Initialize JFrame & options
        int WIDTH = 700;
        int HEIGHT = 400;

        JFrame frame = new JFrame("Course Navigator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(WIDTH, HEIGHT);

        ////////////////////////////////////////////////
        //      Panel Creation
        ////////////////////////////////////////////////

        /////// MAIN
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(Color.LIGHT_GRAY);

        // Create border
        main.setBorder(new EmptyBorder(10, 10, 10, 10));

        /////// NORTH
        HEIGHT = 50;
        JPanel north = new JPanel();
        main.add(north);
        north.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        north.setBackground(Color.LIGHT_GRAY);

        /////// CENTER
        JPanel center = new JPanel();
        main.add(center);

        /////// SOUTH
        JPanel south = new JPanel();
        main.add(south);

        ////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////
        //      NORTH
        ////////////////////////////////////////////////

        /////// Subject Code

        // Panel for Subject code
        JPanel subjectPanel = new JPanel();
        north.add(subjectPanel);

        // JLabel for Subject code
        JLabel subjectLabel = new JLabel("Subject:");
        subjectPanel.add(subjectLabel);

        // JComboBox for Subject codes
        String[] subjectArray = subjectList.toArray(new String[0]);
        JComboBox<String> subjectComboBox = new JComboBox<>(subjectArray);
        subjectPanel.add(subjectComboBox);

        /////// Course names

        // Panel for Course names
        JPanel coursePanel = new JPanel();
        north.add(coursePanel);

        // JLabel for Course names
        JLabel courseLabel = new JLabel("Course:");
        coursePanel.add(courseLabel);

        // JComboBox for Course names
        String[] courseArray = courseList.toArray(new String[0]);
        JComboBox<String> courseComboBox = new JComboBox<>(courseArray);
        coursePanel.add(courseComboBox);

        courseComboBox.setVisible(true);

        // Update JComboBox based on selected subject
        subjectComboBox.addActionListener(e -> {
            // Currently selected subject
            String selection = (String) subjectComboBox.getSelectedItem();

            try {
                // List of courses for selected subject
                List<String> coursesForSubject = backEnd.getCourses(selection);

                // Remove irrelevant courses
                courseComboBox.removeAllItems();

                // Add courses to JComboBox
                for (String course : coursesForSubject) {
                    courseComboBox.addItem(course.toUpperCase());
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        /////// Semester

        // Panel for Semesters
        JPanel semesterPanel = new JPanel();
        north.add(semesterPanel);

        // JLabel for Semesters
        JLabel semesterLabel = new JLabel("Semester:");
        semesterPanel.add(semesterLabel);

        // JComboBox for Semesters
        String[] semesterArray = semesterList.toArray(new String[0]);
        JComboBox<String> semesterComboBox = new JComboBox<>(semesterArray);
        semesterPanel.add(semesterComboBox);

        /////// FINAL CONFIGURATIONS

        // Default JComboBoxes to the first entry
        subjectComboBox.setSelectedIndex(0);
        courseComboBox.setSelectedIndex(0);
        semesterComboBox.setSelectedIndex(0);

        ////////////////////////////////////////////////
        //      CENTER
        ////////////////////////////////////////////////

        // Invisible search button to process filters (beautiful mistake)
        JButton searchButton = new JButton("Search");
        searchButton.setVisible(false);

        // When users apply filter, update table
        subjectComboBox.addActionListener(e -> searchButton.doClick());
        courseComboBox.addActionListener(e -> searchButton.doClick());
        semesterComboBox.addActionListener(e -> searchButton.doClick());

        // TableModel for Results
        WIDTH = 700;
        HEIGHT = 200;
        String[] columnNames = {"CRN", "Title", "Instructor", "Days_1", "Time_1", "Days_2", "Time_2"};
        tableModel = new DefaultTableModel(columnNames, 0) {
          @Override
          public boolean isCellEditable(int row, int column) {
              return false;
          }
        };
        courseTable = new JTable(tableModel);
        courseTable.setPreferredScrollableViewportSize(new Dimension(WIDTH, HEIGHT));
        JScrollPane scrollPane = new JScrollPane(courseTable);
        center.add(scrollPane);

        // Invisible search button logic
        searchButton.addActionListener(e -> {
            try {
                String subject = (String) subjectComboBox.getSelectedItem();
                String course = (String) courseComboBox.getSelectedItem();
                String semester = (String) semesterComboBox.getSelectedItem();
                List<Object[]> courses = backEnd.getDataForTable(subject, course, semester);

                tableModel.setRowCount(0);
                for (Object[] row : courses) {
                    tableModel.addRow(row);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        ////////////////////////////////////////////////
        //      SOUTH
        ////////////////////////////////////////////////

        // JTextArea for Course Details
        WIDTH = 700;
        HEIGHT = 100;
        JTextArea courseDetailArea = new JTextArea();
        south.add(courseDetailArea);
        courseDetailArea.setEditable(false);
        courseDetailArea.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        courseDetailArea.setLineWrap(true);
        courseDetailArea.setWrapStyleWord(true);

        // Default text
        courseDetailArea.setText("Select a course to see details...");

        // Update Course Details if user clicks a course
        courseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && courseTable.getSelectedRow() != -1) {
                int selectedRow = courseTable.getSelectedRow();

                // Get the selected course's data
                String crn = (String) courseTable.getValueAt(selectedRow, 0);
                String subject = (String) subjectComboBox.getSelectedItem();
                String course = (String) courseComboBox.getSelectedItem();
                String semester = (String) semesterComboBox.getSelectedItem();

                try {
                    // Get course details
                    List<String[]> courseDetails = backEnd.getCourseDetails(subject, course, semester);

                    // Format and add Course Details
                    for (String[] detail : courseDetails) {
                        // Match by CRN
                        if (detail[0].equals(crn)) {
                            // Update Text Area
                            String detailsText = "";

                            detailsText = "Course: " + detail[1] + "\n" + "Instructor: " + detail[2] + "\n" +
                                    "Credits: " + detail[3] + "\nSchedule: " + detail[4] + " " + detail[5] +
                                    "\nType: " + detail[6];;

                            // Set text to details
                            courseDetailArea.setText(detailsText);

                            break;
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        ////////////////////////////////////////////////
        //      FINAL FRAME CONFIGURATIONS
        ////////////////////////////////////////////////

        frame.add(main);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        // To prevent focusing on other components
        SwingUtilities.invokeLater(() -> main.requestFocusInWindow());
    }

    public static void main(String[] args) {
        FrontEnd frontEnd = new FrontEnd();
    }
}
