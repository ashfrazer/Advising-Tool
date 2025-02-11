# Advising-Tool

**Please note that this is a WIP. The final goal of this project is to include more filters, more features, and a recommendation system.**

At this point, this is a simple database that contains information about courses at UCA from the Spring 2021 and Fall 2021 semesters.

I understand this isn't the most convenient way to set up the database. I am still working on improving this project to make it more convenient while also fighting the flu. 😭

Steps to run this project:

1) Clone the repository.
2) Update the `pw` variable for your specific postgres@localhost password.
3) Open the postgreSQL console. Enter the query:
   `CREATE TABLE courses (
    CRN SERIAL PRIMARY KEY,
    subj_code VARCHAR(4),
    crse_num VARCHAR(4),
    credit_hrs INT,
    semester VARCHAR(15),
    title VARCHAR(30),
    instr_name VARCHAR(25),
    meet_days1 VARCHAR(7),
    begin_time1 VARCHAR(15),
    end_time1 VARCHAR(15),
    meet_days2 VARCHAR(7),
    begin_time2 VARCHAR(15),
    end_time2 VARCHAR(15),
    schedule_desc VARCHAR(15),
    method VARCHAR(10),
    start_date VARCHAR(10),
    end_date VARCHAR(10),
    attribute VARCHAR(20)
   `
   This creates the *courses* table required for this project.
4) Right-click the CSV files under `src/main/resources/CSV`. There should be two: `FA21 Course Data.csv` and `SP21 Course Data.csv`. Select *import to database*. Ensure the schema is `public`, which should be found under your postgreSQL connection. Under "Table", select the arrow and choose *courses*.
5) Now, run the program normally and interact with the interface.
