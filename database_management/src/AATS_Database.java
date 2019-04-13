import java.sql.*;

public class AATS_Database {
	public static void main (String[] args) {
		System.out.println("Execution started....");
		try {
			Class.forName(Driver.class.getName());	
			Connection con = DriverManager.getConnection("jdbc:mysql://160.153.75.104/AATS_Database","aats_admin","aats_admin123");
			Statement stmt = con.createStatement();
			
			// Drop tables
			stmt.execute("DROP TABLE IF EXISTS lectures");
			stmt.execute("DROP TABLE IF EXISTS students");
			stmt.execute("DROP TABLE IF EXISTS professors");
			stmt.execute("DROP TABLE IF EXISTS classes");
			
			// Drop views
			stmt.execute("DROP VIEW IF EXISTS student_course");
			stmt.execute("DROP VIEW IF EXISTS professor_course");
			stmt.execute("DROP VIEW IF EXISTS students_of_course");
			stmt.execute("DROP VIEW IF EXISTS students_in_class");
			stmt.execute("DROP VIEW IF EXISTS class_of_professor_in_time");
			
			// Drop function
			stmt.execute("DROP FUNCTION IF EXISTS check_if_professor");
			stmt.execute("DROP FUNCTION IF EXISTS check_if_student");
			
			// Create Tables
			// create student table
			stmt.execute("CREATE TABLE IF NOT EXISTS students (	ID INT NOT NULL, "+
																"name VARCHAR(40) NOT NULL , " +
																"surname VARCHAR(40) NOT NULL , " +
																"email VARCHAR(40) NOT NULL , " +
																"password VARCHAR(40) NOT NULL , " +
																"facevector VARCHAR(40) NOT NULL , " +
																"present BOOLEAN NOT NULL , " +
											                    "UNIQUE (`email`,`ID`), " +
											                    "PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
			// create professors table
			stmt.execute("CREATE TABLE IF NOT EXISTS professors (ID INT NOT NULL, "+
																"name VARCHAR(40) NOT NULL , " +
																"surname VARCHAR(40) NOT NULL , " +
																"email VARCHAR(40) NOT NULL , " +
																"password VARCHAR(40) NOT NULL , " +
											                    "UNIQUE (`email`,`ID`), " +
											                    "PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
			// create classes table
			stmt.execute("CREATE TABLE IF NOT EXISTS classes (	ID INT NOT NULL, "+
																"classID VARCHAR(40) NOT NULL , " +
																"location VARCHAR(40) NOT NULL , " +
																"day VARCHAR(40) NOT NULL , " +
																"time VARCHAR(40) NOT NULL , " +
																"makeup BOOLEAN NOT NULL, " +
											                    "UNIQUE (`location`, `day`, `time`), " +
											                    "PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
			// create lectures table
			stmt.execute("CREATE TABLE IF NOT EXISTS lectures (	ID INT NOT NULL, "+
																"classID INT NOT NULL , " +
																"studentID INT NOT NULL , " +
																"professorID INT NOT NULL , " +
																"attendance INT NOT NULL , " +
											                    "UNIQUE (`classID`, `studentID`, `professorID`), " +
											                    "PRIMARY KEY (`ID`) , " +
											                    "FOREIGN KEY (`classID`) REFERENCES classes (`ID`) ON DELETE CASCADE," +
											                    "FOREIGN KEY (`studentID`) REFERENCES students (`ID`) ON DELETE CASCADE, " +
											                    "FOREIGN KEY (`professorID`) REFERENCES professors (`ID`) ON DELETE CASCADE" +
																") ENGINE = InnoDB;");
			
			// Insert Data
			// Insert Students
			stmt.execute("INSERT INTO students VALUES ('21500009' , 'Alba' , 'Mustafaj', 'alba.mustafaj@ug.bilkent.edu.tr', 'alba123', '' , false)");
			stmt.execute("INSERT INTO students VALUES ('21503525' , 'Argert' , 'Boja', 'argert.boja@ug.bilkent.edu.tr', 'argert123', '', false)");
			stmt.execute("INSERT INTO students VALUES ('21500342' , 'Ndricim' , 'Rrapi', 'ndricim.rrapi@ug.bilkent.edu.tr', 'ndricim123', '', false)");
			stmt.execute("INSERT INTO students VALUES ('21500010' , 'Rubin' , 'Daija', 'rubin.daija@ug.bilkent.edu.tr', 'rubin123', '', false)");
			
			// Insert Professors
			stmt.execute("INSERT INTO professors VALUES ('1' , 'Selim' , 'Aksoy', 'aksoy@cs.bilkent.edu.tr', 'selim123')");
			stmt.execute("INSERT INTO professors VALUES ('2' , 'Eray' , 'Tuzun', 'tuzun@cs.bilkent.edu.tr', 'eray123')");
			stmt.execute("INSERT INTO professors VALUES ('3' , 'Ibrahim' , 'Korpeoglu', 'korpeoglu@cs.bilkent.edu.tr', 'ibrahim123')");
			stmt.execute("INSERT INTO professors VALUES ('4' , 'Haluk' , 'Altunel', 'altunel@bilkent.edu.tr', 'haluk123')");
			
			// Insert classes
			stmt.execute("INSERT INTO classes VALUES ('1' , 'CS458-1' , 'B204', 'Tuesday', '1540-1630', false)");
			stmt.execute("INSERT INTO classes VALUES ('2' , 'CS458-1' , 'B204', 'Tuesday', '1640-1730', true)");
			stmt.execute("INSERT INTO classes VALUES ('3' , 'CS458-1' , 'B204', 'Friday', '1340-1430', false)");
			stmt.execute("INSERT INTO classes VALUES ('4' , 'CS458-1' , 'B204', 'Friday', '1440-1530', false)");
			stmt.execute("INSERT INTO classes VALUES ('5' , 'CS453-1' , 'EA202', 'Tuesday', '1340-1430', false)");
			stmt.execute("INSERT INTO classes VALUES ('6' , 'CS453-1' , 'EA202', 'Tuesday', '1440-1530', false)");
			stmt.execute("INSERT INTO classes VALUES ('7' , 'CS453-1' , 'EA202', 'Friday', '1540-1630', false)");
			stmt.execute("INSERT INTO classes VALUES ('8' , 'CS453-1' , 'EA202', 'Friday', '1640-1730', false)");
			
			// Insert lectures
			stmt.execute("INSERT INTO lectures VALUES ('1' , '1' , '21500009', '4', '18')");
			stmt.execute("INSERT INTO lectures VALUES ('2' , '1' , '21503525', '4', '28')");
			stmt.execute("INSERT INTO lectures VALUES ('3' , '1' , '21500342', '4', '38')");
			stmt.execute("INSERT INTO lectures VALUES ('4' , '8' , '21500010', '1', '28')");
			stmt.execute("INSERT INTO lectures VALUES ('5' , '2' , '21503525', '4', '18')");
			stmt.execute("INSERT INTO lectures VALUES ('6' , '3' , '21503525', '4', '28')");
			stmt.execute("INSERT INTO lectures VALUES ('7' , '5' , '21503525', '2', '38')");
			stmt.execute("INSERT INTO lectures VALUES ('8' , '6' , '21503525', '2', '28')");
			
			// Create views
			// Create view for student and courses
			stmt.execute("CREATE VIEW student_course AS SELECT classes.ID AS ID, " + 
																			"classes.classID AS classID, " + 
																			"lectures.studentID AS studentID " + 
																			"FROM classes JOIN lectures " + 
																			"WHERE classes.ID = lectures.classID ");
			// Create view for professor and courses
			stmt.execute("CREATE VIEW professor_course AS SELECT classes.ID AS ID, " + 
																			"classes.classID AS classID, " + 
																			"lectures.professorID AS professorID " + 
																			"FROM classes JOIN lectures " + 
																			"WHERE classes.ID = lectures.classID ");
			
			// Create view for professor and courses
			stmt.execute("CREATE VIEW students_of_course AS SELECT classes.ID AS ID, " + 
																			"classes.classID AS classID, " + 
																			"lectures.studentID AS studentID, " +
																			"classes.day AS day, " +
																			"classes.time AS time " +
																			"FROM classes JOIN lectures " + 
																			"WHERE classes.ID = lectures.classID ");
			// create view for students present in class
			stmt.execute("CREATE VIEW students_in_class AS SELECT students.ID AS studentID, "+
																"students.facevector AS facevector, "+
																"classes.day AS day, "+
																"classes.time AS time, "+
																"classes.ID as classID, "+
																"lectures.professorID as professorID "+
																"FROM students JOIN classes JOIN lectures "+
																"WHERE students.ID = lectures.studentID "+
																"AND classes.ID = lectures.classID");
			
			// create view for students present in class
			stmt.execute("CREATE VIEW class_of_professor_in_time AS SELECT lectures.professorID AS professorID, "+
																"classes.classID AS classID, "+
																"classes.day AS day, "+
																"classes.time AS time "+
																"FROM classes JOIN lectures "+
																"WHERE classes.ID = lectures.classID");
						
			// QUERIES
			// Return name surname after user provides username and pasw
			System.out.println("Return name surname for ID and PASSWORD provided:");
			ResultSet rs = stmt.executeQuery("SELECT name, surname FROM students WHERE ID = '21503525' AND password='argert123'");
			while (rs.next())
				System.out.println("Name: " + rs.getString(1) + " Surname: " + rs.getString(2));
			
			// Return courses taken by student
			System.out.println("Return courses taken by student with ID provided: ");
			rs = stmt.executeQuery("SELECT DISTINCT classID FROM student_course WHERE studentID = '21503525'");
			while (rs.next())
				System.out.println(rs.getString(1));
			
			// Return courses taken by student
			System.out.println("Return courses given by professor with ID provided: ");
			rs = stmt.executeQuery("SELECT DISTINCT classID FROM professor_course WHERE professorID = '4'");
			while (rs.next())
				System.out.println(rs.getString(1));
			
			// Return students taking course in specific day and time 
			System.out.println("Return students takining course in specific day and time : ");
			rs = stmt.executeQuery("SELECT DISTINCT studentID FROM students_of_course WHERE day = 'Tuesday' and time = '1540-1630'");
			while (rs.next())
				System.out.println(rs.getString(1));
			
			// Return if student is present or not
			System.out.println("Return if student is present or not : ");
			rs = stmt.executeQuery("SELECT present from students where ID = '21503525'");
			while (rs.next())
				System.out.println(rs.getString(1));
			
			// Return if student is present or not
			System.out.println("Return students who take a course from professor : ");
			rs = stmt.executeQuery("SELECT studentID from lectures where professorID = '4' AND classID='1'");
			while (rs.next())
				System.out.println(rs.getString(1));
			
			// Return students ID and their face vector for specific course in day and time
			System.out.println("Return students IDs and their facevector for specific course and time : ");
			rs = stmt.executeQuery("SELECT studentID, facevector from students_in_class WHERE classID = '1' AND day='Tuesday' AND time='1540-1630'");
			while (rs.next())
				System.out.println(rs.getString(1));	
			
			// Return if student is present or not
			System.out.println("Return class from professor id and time : ");
			rs = stmt.executeQuery("SELECT DISTINCT classID from class_of_professor_in_time where professorID = '4' AND day='Tuesday' AND time='1540-1630'");
			while (rs.next())
				System.out.println(rs.getString(1));
					
			// Update attendance
			System.out.println("Update attendance of student provided ID of student professor and course: ");
			stmt.executeUpdate("UPDATE lectures SET attendance = (attendance + 1) WHERE studentID = '21503525' AND professorID = '4' AND classID = '1'");
			stmt.executeUpdate("UPDATE students SET present = true WHERE ID = '21503525'");
			
			// Update face vector of student
			stmt.executeUpdate("UPDATE students SET facevector = 'newvector' WHERE ID = '21503525'");
			
			// Check if id belongs to professor			
			stmt.executeUpdate("CREATE FUNCTION check_if_professor (id INT) RETURNS INTEGER " + 
							   "BEGIN " +
							   "DECLARE amount INTEGER; "+
							   "SELECT count(*) into amount "+
							   "from professors as P "+ 
							   "where P.ID = id ; "+
							   "RETURN amount; "+
							   "END ");
			
			// Check if id belongs to student			
			stmt.executeUpdate("CREATE FUNCTION check_if_student (id INT) RETURNS INTEGER " + 
							   "BEGIN " +
							   "DECLARE amount INTEGER; "+
							   "SELECT count(*) into amount "+
							   "from students as S "+ 
							   "where S.ID = id ; "+
							   "RETURN amount; "+
							   "END ");
			
			
			con.close();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
}
