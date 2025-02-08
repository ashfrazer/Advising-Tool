import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FrontEnd {
    private BackEnd backEnd;

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

            // Get credits
            //List<String> creditsList = backEnd.getCredits();

            // Initialize GUI
            startGUI(subjectsList, courseList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void startGUI(List<String> subjectList, List<String> courseList) {
        ////////////////////////////////////////////////
        //      Frame Configuration
        ////////////////////////////////////////////////

        // Initialize JFrame & options
        int WIDTH = 800;
        int HEIGHT = 600;

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
        JPanel north = new JPanel();
        main.add(north);
        north.setBackground(Color.RED);
        north.setLayout(new GridLayout(2, 3, 10, 10));

        /////// CENTER
        JPanel center = new JPanel();
        main.add(center);
        center.setBackground(Color.GREEN);

        /////// SOUTH
        JPanel south = new JPanel();
        main.add(south);
        south.setBackground(Color.BLUE);

        ////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////
        //      NORTH
        ////////////////////////////////////////////////


        /////// Subject Code

        // JComboBox for Subject codes
        String[] subjectArray = subjectList.toArray(new String[0]);
        JComboBox<String> subjectComboBox = new JComboBox<>(subjectArray);
        north.add(subjectComboBox);
        //subjectComboBox.setEditable(true);

        /////// Search

        // JComboBox for Search
        String[] courseArray = courseList.toArray(new String[0]);
        JComboBox<String> courseComboBox = new JComboBox<>(courseArray);
        north.add(courseComboBox);
        //courseComboBox.setEditable(true);
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

        /*
        JTextField search = new JTextField(20);
        north.add(search);
        search.setText("Search...");

        // Focus listener to reset text upon clicking
        search.addFocusListener(new java.awt.event.FocusAdapter() {
            // Empty if user clicks search bar
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (search.getText().equals("Search...")) {
                    search.setText("");
                    search.setForeground(Color.BLACK);
                }
            }
            // Reset if user clicks outside search bar
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (search.getText().isEmpty()) {
                    search.setText("Search...");
                    search.setForeground(Color.GRAY);
                }
            }

        });

         */

        /*
        /////// Credits

        // Find max value in credits
        int max = 0;

        for (String s : creditsList) {
            if (Integer.parseInt(s) > max) {
                max = Integer.parseInt(s);
            }
        }

        // Panel to hold Credits components
        JPanel creditsPanel = new JPanel();
        north.add(creditsPanel);

        // JSpinner for Credits
        SpinnerModel creditsModel = new SpinnerNumberModel(1, 1, max, 1);
        JSpinner creditsSpinner = new JSpinner(creditsModel);

        // Label JSpinner
        JLabel creditsLabel = new JLabel("Credit Hours:");

        // Add components to Credits Panel
        creditsPanel.add(creditsLabel);
        creditsPanel.add(creditsSpinner);
         */


        // Default Subject JComboBox to the first subject
        subjectComboBox.setSelectedIndex(0);

        frame.add(main);
        frame.setVisible(true);

        // To prevent focusing on other components
        SwingUtilities.invokeLater(() -> main.requestFocusInWindow());

        ////////////////////////////////////////////////
        //      Label & Field Creation
        ////////////////////////////////////////////////

        //JLabel
    }

    public static void main(String[] args) {
        FrontEnd frontEnd = new FrontEnd();
    }
}
