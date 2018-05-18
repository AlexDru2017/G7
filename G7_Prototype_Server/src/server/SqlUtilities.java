package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import resources.Question;

/**
 * 
 * @author Alex
 *
 */
public class SqlUtilities {

	// region Constants

	public final static String SELECT_All_FROM_Questions = "SELECT * FROM Questions;";

	public final static String Login_SELECT_User_From_Users = "SELECT * FROM Users WHERE idUsers=? AND passWord=?;";

	// region Public Methods

	// end region -> Constants

	@SuppressWarnings("deprecation")
	public static Connection connection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			/* handle the error */
		}
		try {
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://group7project.c2ntivjwkagb.eu-central-1.rds.amazonaws.com:3306/group7db", "app",
					"project7");
			System.out.println("SQL connection succeed");
			return conn;
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return null;
	}

	/**
	 * returns the whole table of questions for the table view
	 */
	public static ArrayList<Question> getQuestions() throws SQLException {
		ArrayList<Question> questions = new ArrayList<Question>();
		Statement statement = SqlUtilities.connection().createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Questions;");
		while (rs.next()) {
			questions.add(
					new Question(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
		}
		rs.close();
		return questions;
	}
	
	/*
	 * currently changes only the correct answer field in the DB but will be
	 * expanded to whole question
	 */
	public boolean editQuestion(String questionID, String newCorrectAnswer) throws SQLException {
		Statement statement = SqlUtilities.connection().createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Questions;");
		while (rs.next()) {
			/* if the question exists */
			if (rs.getString(1).equals(questionID)) {
				/* if the new correct answer is the same as the old one */
				if (rs.getString(5).equals(newCorrectAnswer)) {
					System.out.println("This is the same answer");
					rs.close();
					return false;
				}
				/* if the new answer is not one of the options */
				else if (!rs.getString(4).contains(newCorrectAnswer)) {
					System.out.println("The new answer is not one of the given options");
					rs.close();
					return false;
				} else {
					statement.executeUpdate("UPDATE Questions SET correctAnswer='" + newCorrectAnswer
							+ "' WHERE questionID='" + questionID + "';");
					rs.close();
					return true;
				}
			}
		}
		rs.close();
		System.out.println("The question doesn't exist");
		return false;
	}

	// end region -> Public Methods

} /* end of class */