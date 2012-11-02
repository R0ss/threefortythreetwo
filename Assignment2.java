// Credits: http://www.fankhausers.com/postgresql/jdbc/
//          http://jdbc.postgresql.org/documentation/83/

// Instructions:
// 1) Connect to "ssh dbsrv1.cdf.toronto.edu" using your cdf username and password
// 2) Download the JDBC driver (version 8.3-607 JDBC 4) from http://jdbc.postgresql.org/download.html and
//    copy jdbc jar file (using sftp) to dbsrv1 server.
// 3) On line 60 connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/csc343h-username", "username", "");
//    -leave as is the host and port number ("localhost:5432");
//    -replace "username" with your cdf username, where the fields "csc343h-username" is the database name and "username" is the username that will be used to login into the database
//    -password field must be set to empty ("");
// 4) Compile the code:
//         javac JDBCExample.java
// 5) Run the code:
//         java -cp /path-to-jdbc-directory/postgresql-8.3-607.jdbc4.jar:. JDBCExample
//    where postgresql-8.3-607.jdbc4.jar is jdbc jar file downloaded in step 2

import java.sql.*;

public class Assignment2 {

    Connection connection;

    public static final String SFIRSTNAME = "sfirstname";
    public static final String SLASTNAME = "slastname";
    public static final String SEX = "sex";
    public static final String AGE = "age";
    public static final String YEAROFSTUDY = "yearofstudy";
    public static final String DCODE = "dcode";
    public static final String DNAME = "dname";

    public Assignment2() {
        loadJDBCDriver();
    }

    // Load JDBC driver
    private boolean loadJDBCDriver() {
        try {
            Class.forName("org.postgresql.Driver");
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean connectDB(String URL, String username, String password) {
        try {
            connection = DriverManager.getConnection(URL, username, password);
            if (connection != null) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean disconnectDB() {
        try {
            if (connection == null) {
                return true;
            } else if (connection.isClosed() == false) {
                connection.close();
                return true;
            } else {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean insertStudent(int sid, String lastName, String firstName, int age, String sex, String dname, int yearOfStudy) {
        try {
            if (isInsertStudentConditionOK(dname, sex, yearOfStudy) == false) {
                System.out.println("isInsertStudentCondition false on " + dname + " " + sex + " " + yearOfStudy);
                return false;
            }

            String dcode = getFirstDcode(dname);

            return insertNewStudent(sid, lastName, firstName, age, sex, dcode, yearOfStudy) == 1;
        } catch (SQLException e) {
            // e.printStackTrace();
            return false;
        }
    }

    private int insertNewStudent(int sid, String lastName, String firstName, int age, String sex, String dcode, int yearOfStudy) {
        String query = "INSERT INTO student " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement sql = null;
        int result;
        try {
            sql = connection.prepareStatement(query);

            sql.setInt(1, sid);
            sql.setString(2, lastName);
            sql.setString(3, firstName);
            sql.setString(4, sex);
            sql.setInt(5, age);
            sql.setString(6, dcode);
            sql.setInt(7, yearOfStudy);

            result = sql.executeUpdate();

            return result;
        } catch (SQLException e) {
//            e.printStackTrace();
            return 0;
        } finally {
            closeStatement(sql);
        }
    }

    private boolean isInsertStudentConditionOK(String dname, String sex, int yearOfStudy) throws SQLException {
        try {
            if (isDepartmentExist(dname) == false) {
                System.out.println("isInsertStudentConditionOK.isDepartmentExist false on " + dname);
                return false;
            }
            if (sex.equals("M") == false && sex.equals("m") == false &&
				sex.equals("F") == false && sex.equals("f") == false) {
                System.out.println(sex);
                System.out.println("isInsertStudentConditionOK: M or F");
                return false;
            }
            if (yearOfStudy < 1 || yearOfStudy > 5) {
                System.out.println("isInsertStudentConditionOK.yearOfStudy");
                return false;
            }
            return true;
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    private boolean isDepartmentExist(String dname) throws SQLException {
        try {
            return getFirstDcode(dname) != null;
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    // Return dcode of the first department row matching dname.
    // In theory there could be multiple dcodes with the same dname.
    private String getFirstDcode(String dname) throws SQLException {
        String query = "SELECT * " + "FROM department d " + "WHERE d.dname = ?";
        PreparedStatement sql = null;
        ResultSet result = null;
        try {
            sql = connection.prepareStatement(query);
            sql.setString(1, dname);
            result = sql.executeQuery();
            if (result.next()) {
                return result.getString(DCODE);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException();
        } finally {
            closeStatement(sql);
            closeResultSet(result);
        }
    }

    private void closeStatement(Statement sql) {
        try {
            if (sql != null) {
                sql.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeStatement(PreparedStatement sql) {
        try {
            if (sql != null) {
                sql.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeResultSet(ResultSet result) {
        try {
            if (result != null) {
                result.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getStudentCount(String dname) {
        String query = "SELECT COUNT(*) " + "FROM department d, student s " + "WHERE d.dname = ? AND d.dcode = s.dcode ";
        PreparedStatement sql = null;
        ResultSet result = null;
        try {
            sql = connection.prepareStatement(query);
            sql.setString(1, dname);
            result = sql.executeQuery();
            if (result.next()) {
                return result.getInt("COUNT");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            return -1;
        } finally {
            closeStatement(sql);
            closeResultSet(result);
        }
    }

    public String getStudentInfo(int sid) {
        String query = "SELECT * " +
                    "FROM student s, department d " +
                    "WHERE s.sid = ? AND s.dcode = d.dcode";
        PreparedStatement sql = null;
        ResultSet result = null;
        try {
            sql = connection.prepareStatement(query);
            sql.setInt(1, sid);
            result = sql.executeQuery();
            if (result.next()) {
                return  result.getString(SFIRSTNAME).trim() + ":" +
                        result.getString(SLASTNAME).trim() + ":" +
                        result.getString(SEX).trim() + ":" +
                        result.getInt(AGE) + ":" +
                        result.getInt(YEAROFSTUDY) + ":" +
                        result.getString(DNAME).trim();
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            closeStatement(sql);
            closeResultSet(result);
        }
    }

    /*
     * Changes the department name to the department name supplied (newName).
     * Accepts the dcode and new department name as Strings (in that order).
     * Returns true if the change was successful, false otherwise.
     */
    public boolean chgDept(String dcode, String newName) {
        Statement sql = null;
        String sqlText = "UPDATE department        " +
                        "SET dname = '" + newName + "'" +
                        "WHERE dcode = '" + dcode + "'";
        try {
            sql = connection.createStatement();
            return sql.executeUpdate(sqlText) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeStatement(sql);
        }
    }

    public boolean deleteDept(String dcode){
        if (deleteStudentDcode(dcode) ||
			deleteInstructorDcode(dcode) ||
			deleteCourseDcode(dcode) ||
			deleteDepartmentDcode(dcode)){
			return true;
		} else {
			return false;
		}
    }

	private boolean deleteDepartmentDcode(String dcode){
		PreparedStatement sql = null;
        String sqlText = "DELETE FROM department " +
                        "WHERE dcode = ?";
        try {
            sql = connection.prepareStatement(sqlText);
			sql.setString(1, dcode);
            return sql.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeStatement(sql);
        }
	}

	private boolean deleteCourseDcode(String dcode){
		PreparedStatement sql = null;
        String sqlText = "DELETE FROM course " +
                        "WHERE dcode = ?";
        try {
            sql = connection.prepareStatement(sqlText);
			sql.setString(1, dcode);
            return sql.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeStatement(sql);
        }
	}

	private boolean deleteStudentDcode(String dcode){
		PreparedStatement sql = null;
        String sqlText = "DELETE FROM student " +
                        "WHERE dcode = ?";
        try {
            sql = connection.prepareStatement(sqlText);
			sql.setString(1, dcode);
            return sql.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeStatement(sql);
        }
	}

	private boolean deleteInstructorDcode(String dcode){
		PreparedStatement sql = null;
        String sqlText = "DELETE FROM instructor " +
                        "WHERE dcode = ?";
        try {
            sql = connection.prepareStatement(sqlText);
			sql.setString(1, dcode);
            return sql.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeStatement(sql);
        }
	}

    public String listCourses(int sid) {
        String studentCourseInfo = "";
        String sqlText = "SELECT C.cname AS cname, D.dname AS dname, CS.semester AS semester," +
                            "CS.year AS year, SC.grade AS grade " +
                        "FROM student S, studentCourse SC, courseSection CS, course C, department D " +
                        "WHERE C.cid = CS.cid " +
			                "AND C.dcode = D.dcode " +
                            "AND S.sid = SC.sid " +
                            "AND SC.csid = CS.csid " +
                            "AND S.sid = '" + sid + "'";
        Statement sql = null;
        ResultSet rs = null;
        try {
            sql = connection.createStatement();
            rs = sql.executeQuery(sqlText);
            if (rs != null) {
                // If it skips the first row, we might have to change it to a
                // do-while loop
                while (rs.next()) {
                    studentCourseInfo += "#" + rs.getString("cname").trim() + ":" + rs.getString("dname").trim() + ":" + rs.getString("semester").trim() + ":" + rs.getInt("year") + ":" + rs.getInt("grade");
                }
                // Close the resultset
                rs.close();
            }
            if (studentCourseInfo.isEmpty()){
                return "";
            } else {
                // Remove first pound sign
                return studentCourseInfo.substring(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            closeStatement(sql);
            closeResultSet(rs);
        }
    }

    public String query7() {
        String query7Info = "";
        String sqlText =
                "SELECT cname, year, semester, grade                  " +
                "FROM PopularCourses P, ComsciAverages CA             " +
                "WHERE P.csid = CA.csid AND (                         " +
                "   CA.grade = (                                      " +
                "        SELECT MAX(C1.grade)                         " +
                "        FROM PopularCourses P1, ComsciAverages C1    " +
                "        WHERE P1.csid = C1.csid                      " +
                "   ) OR CA.grade = (                                 " +
                "        SELECT MIN(C1.grade)                         " +
                "        FROM PopularCourses P1, ComsciAverages C1    " +
                "        WHERE P1.csid = C1.csid                      " +
                "   )                                                 " +
                ")";
        Statement sql = null;
        ResultSet rs = null;
        try {
            dropViewQuery7();
            createViewQuery7();

            sql = connection.createStatement();
            rs = sql.executeQuery(sqlText);
            if (rs != null) {
                // If it skips the first row, we might have to change it to a
                // do-while loop
                while (rs.next()) {
                    query7Info += "#" +
                                rs.getString("cname").trim() + ":" +
                                rs.getInt("year") + ":" +
                                rs.getInt("semester") + ":" +
                                rs.getFloat("grade");
                }
                // Close the resultset
                rs.close();
            }

            dropViewQuery7();

            if (query7Info.isEmpty()){
                return "";
            } else {
                // Remove first pound sign
                return query7Info.substring(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            closeStatement(sql);
            closeResultSet(rs);
        }
    }

    private void createViewQuery7() throws SQLException {
        String sqlText1, sqlText2, sqlText3;

        //Create tables
        sqlText1 = "CREATE VIEW ComsciCoursesWithStudentGrades AS(                  "
                 + "SELECT CS.csid, CS.year, CS.semester, SC.sid, SC.grade, C.cname "
                 + "FROM CourseSection CS, StudentCourse SC, Department D, Course C "
                 + "WHERE CS.csid = SC.csid                                         "
                 + "AND CS.cid = C.cid                                              "
                 + "AND CS.dcode = D.dcode                                          "
                 + "AND D.dname = 'Computer Science')                               ";

        sqlText2 = "CREATE VIEW ComsciAverages AS(              "
                 + "SELECT C.csid, AVG(C.grade) AS grade        "
                 + "FROM ComsciCoursesWithStudentGrades C       "
                 +  "GROUP BY C.csid)                           ";

        sqlText3 = "CREATE VIEW PopularCourses AS(                  "
                 + "SELECT csid, cname, year, semester, COUNT(sid)  "
                 + "FROM ComsciCoursesWithStudentGrades             "
                 + "GROUP BY csid, cname, year, semester            "
                 + "HAVING COUNT(sid) >= 3)                         ";

        Statement sql = connection.createStatement();
        sql.executeUpdate(sqlText1);
        sql.executeUpdate(sqlText2);
        sql.executeUpdate(sqlText3);

        closeStatement(sql);
    }

    private void dropViewQuery7() throws SQLException {
        String sqlText1, sqlText2, sqlText3;

        // Drop tables
        sqlText1 = "DROP VIEW IF EXISTS PopularCourses CASCADE";
        sqlText2 = "DROP VIEW IF EXISTS ComsciAverages CASCADE";
        sqlText3 = "DROP VIEW IF EXISTS ComsciCoursesWithStudentGrades CASCADE";

        Statement sql = connection.createStatement();
        sql.executeUpdate(sqlText1);
        sql.executeUpdate(sqlText2);
        sql.executeUpdate(sqlText3);

        closeStatement(sql);
    }

    /*
     * Increases the grades of all the students who took a course in the course
     * section identified by csid by 10
     */
    public boolean updateGrades(int csid) {
        Statement sql = null;
        Statement sql2 = null;
        ResultSet rs = null;
        int newGrade;
        String sqlText = "SELECT * "
                       + "FROM studentCourse "
                       + "WHERE csid = " + csid;
        try {
            sql = connection.createStatement();
            rs = sql.executeQuery(sqlText);
            if (rs != null) {
                sql2 = connection.createStatement();
                // If it skips the first row, we might have to change it to a
                // do-while loop
                while (rs.next()) {
                    newGrade = (int) Math.min(rs.getInt("grade") * 1.1, 100);
                    sqlText = "UPDATE studentCourse      "
                            + " SET grade = " + newGrade
                            + " WHERE sid = " + rs.getString("sid")
                            + "     AND csid = '" + rs.getString("csid") + "'";
                    sql2.executeUpdate(sqlText);
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResultSet(rs);
            closeStatement(sql);
        }
    }

    public boolean updateDB(){
        PreparedStatement sql = null;
        String sqlText = "INSERT INTO femaleStudents (" +
        		"     SELECT s.sid, s.sfirstname AS fname, s.slastname AS lname" +
        		"     FROM student s, department d" +
        		"     WHERE s.sex = 'F'" +
        		"         AND s.yearofstudy = 4" +
        		"         AND s.dcode = d.dcode" +
        		"         AND d.dname = 'Computer Science'" +
        		")";
        try {
            dropFemaleStudentsTable();
            createFemaleStudentsTable();
            sql = connection.prepareStatement(sqlText);
            return sql.executeUpdate() >= 0;
        } catch (SQLException e){
            return false;
        } finally {
            closeStatement(sql);
        }
    }

    private void dropFemaleStudentsTable() throws SQLException {
        String sqlText = "DROP TABLE IF EXISTS femaleStudents";
        Statement sql = connection.createStatement();
        sql.executeUpdate(sqlText);
        closeStatement(sql);
    }

    private void createFemaleStudentsTable() throws SQLException {
        String sqlText = "CREATE TABLE femaleStudents (" +
        		"     sid     INT," +
        		"     fname   CHAR(20)," +
        		"     sname   CHAR(20)" +
        		");";
        Statement sql = connection.createStatement();
        sql.executeUpdate(sqlText);
        closeStatement(sql);
    }

    public static void main(String[] argv) {
        Assignment2 a2 = new Assignment2();
        if (a2.connectDB(argv[0], argv[1], argv[2]) == false) {
            System.out.println("a2.connectDB fail");
            return;
        } else {
            System.out.println("connectDB OK");
        }

        try {
            if (a2.isDepartmentExist("Computer Science")) {
                System.out.println("CSC exists");
            } else {
                System.out.println("CSC doesn't exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

		//        if (a2.insertStudent(662, "Wincester", "Sandra", 2000, "F", "Computer Science", 2)){
		//            System.out.println("insertStudent OK");
		//        } else {
		//            System.out.println("insertStudent FAIL");
		//        }

        System.out.println("getStudentCount: " + a2.getStudentCount("Computer Science"));

        System.out.println("getStudentInfo: 666: " + a2.getStudentInfo(666));

        if (a2.chgDept("MAT", "Mathemagick")){
            System.out.println("chgDept MAT to Mathemagick OK");
        } else {
            System.out.println("chgDept MAT to Mathemagick FAIL");
        }

        System.out.println("listCourses: 666: " + a2.listCourses(666));
        System.out.println("listCourses: 9999: " + a2.listCourses(9999));
        System.out.println("listCourses: 664: " + a2.listCourses(664));

        System.out.println("Query7: " + a2.query7());

        if (a2.deleteDept("CSC")){
            System.out.println("deleteDept OK");
        } else {
            System.out.println("deleteDept FAIL");
        }

		if (a2.deleteCourseDcode("CSC")){
            System.out.println("deleteCourseDcode OK");
        } else {
            System.out.println("deleteCourseDcode FAIL");
        }

        if (a2.updateGrades(1003)){
            System.out.println("updateGrades 1003 OK");
        } else {
            System.out.println("updateGrades 1003 FAIL");
        }

        if (a2.updateDB()){
            System.out.println("updateDB OK");
        } else {
            System.out.println("updateDB FAIL");
        }

        if (a2.disconnectDB() == false) {
            System.out.println("a2.disconnectDB fail");
        } else {
            System.out.println("disconnectDB OK");
        }

        /*
         * Soft code username, password.
         */
    }
}
