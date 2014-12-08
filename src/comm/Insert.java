package comm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

class ConfigRead {
	static String DBUrl, userName, Password, UniversityURL, BaseURL;

	static void ReadConfigFile() {
		Properties prop = new Properties();
		InputStream input = null;
		try {

			input = new FileInputStream(new File("./config.properties"));
			// load a properties file
			prop.load(input);

			// get the property value and print it out
			DBUrl = prop.getProperty("DBurl");
			userName = prop.getProperty("userName");
			Password = prop.getProperty("password");
			UniversityURL = prop.getProperty("universityURL");
			BaseURL = prop.getProperty("baseURL");

		} catch (Exception ex) {
			System.out.println("Config file not found....");
			PrintWriter writer;
			try {
				File log = new File("Error.txt");
				System.out.println("Saving Error....");
				if (log.exists() == false) {
					
					log.createNewFile();
				}
				SimpleDateFormat dt = new SimpleDateFormat(
						"yyyy-mm-dd hh:mm:ss");
				writer = new PrintWriter(new BufferedWriter(new FileWriter(log,true)));
				writer.append("\nInsert Failed:" + dt.format(new Date()));
				writer.append("\nDetails:");
				writer.append("\n\tInsert.java  failed:\n \t \t"+ex.getLocalizedMessage());
				writer.append("\n\tReason config File not found!!");
				writer.append("\n######################################\n\n");
				writer.close();
				System.exit(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Done....");
				System.exit(0);
			}

		} finally {
			System.out.println("***********************");
			System.out.println("Database URL: " + DBUrl);
			System.out.println("\t Username: " + userName);
			System.out.println("\t Password: " + Password);
			System.out.println("University URL: " + UniversityURL);
			System.out.println("Base URL: " + BaseURL);

			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class InsertProcess {
	static {
		ConfigRead.ReadConfigFile();
	}

	ArrayList<String> fkPropertyID = new ArrayList<String>();
	ArrayList<String> fkTermID = new ArrayList<String>();
	String title = null;
	String pkPropertyID = null;

	void insertTerms() throws ClassNotFoundException, SQLException { /*
																	 * String
																	 * userName
																	 * =
																	 * "admin";
																	 * String
																	 * password
																	 * =
																	 * "admin";
																	 * String
																	 * url =
																	 * "jdbc:jtds:sqlserver://NIKHIL;instanceName=SQLEXPRESS"
																	 * ;
																	 * Class.forName
																	 * (
																	 * "net.sourceforge.jtds.jdbc.Driver"
																	 * );
																	 * Connection
																	 * connect1
																	 * =
																	 * DriverManager
																	 * .
																	 * getConnection
																	 * (url,
																	 * userName
																	 * ,password
																	 * );
																	 */
		// ******************** CIMES Live DB
		// CONNECTION***************************
		String userName = ConfigRead.userName;
		String password = ConfigRead.Password;
		String url = ConfigRead.DBUrl;// "jdbc:jtds:sqlserver://Mail1/ExpertNet2;instance=SQLEXPRESS";
		Class.forName("net.sourceforge.jtds.jdbc.Driver");
		Connection conn = DriverManager.getConnection(url, userName, password);
		// ******************** CIMES3 CONNECTION***************************
		// String userName = "expertnetWeb";
		// String password = "Kaliban01.";
		// String url =
		// "jdbc:jtds:sqlserver://cimes3.its.fsu.edu/ExpertNet2_dev;instance=SQLEXPRESS";
		// Class.forName("net.sourceforge.jtds.jdbc.Driver");
		// Connection conn = DriverManager.getConnection(url,
		// userName,password);
		System.out.println("Connected with database..");
		Statement stmt = conn.createStatement();
		stmt = conn.createStatement();

		// join property and proppertyHolding table on fkuniversityID and
		// UniversityUID
		String sql = "SELECT P.pkPropertyID, H.TaggedIrisTerms"
				+ " From dbo.property P inner join dbo.PropertyHolding H"
				+ "  on (P.universityUID = H.universituid) and (P.fkUniversityID = H.fkUniversityID) and (P.title = H.Title)";
		ResultSet rs = stmt.executeQuery(sql);

		System.out.println("Result after Join operation");
		while (rs.next()) {

			int pkPropertyID = rs.getInt("pkPropertyID");
			String terms = rs.getString("TaggedIrisTerms");

			System.out.println("pkProperty: " + pkPropertyID + " and  terms: "
					+ terms);
			if (terms.contains(","))
				for (String a : terms.split(",")) {
					System.out.println(pkPropertyID + "\t" + a.trim());
					fkPropertyID.add(String.valueOf(pkPropertyID));
					fkTermID.add(a.trim());

				}
			else {
				System.out.println(pkPropertyID + "\t" + terms);
				fkPropertyID.add(String.valueOf(pkPropertyID));
				fkTermID.add(terms.trim());

			}
		}// while
			// display the arraylists
		System.out.println("ArrayLists...");
		for (int i = 0; i < fkPropertyID.size(); i++)
			System.out.println(fkPropertyID.get(i) + "\t" + fkTermID.get(i));
		// insert Query for ProprtyResearchIris table
		System.out.println("Inserting records in PropertyReasearchIris table");
		for (int j = 0; j < fkPropertyID.size(); j++) {
			stmt = conn.createStatement();
			// int rowCount=0;
			sql = "insert into dbo.PropertyResearchIris values('"
					+ fkPropertyID.get(j) + "','" + fkTermID.get(j) + "')";
			stmt.executeUpdate(sql);

		}
		System.out.println("PropertyReasearchIris table updated with new rows");

	}
}

public class Insert {

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		// TODO Auto-generated method stub
		InsertProcess p = new InsertProcess();
		p.insertTerms();
	}

}
