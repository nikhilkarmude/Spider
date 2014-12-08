package comm;

import com.jaunt.*;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

class Process {

	int JOBID;
	int fkid;
	int minpkid;
	String ORIGINALINVENTORID = null;
	int ISREADYFORUPLOAD = 0;
	int ISUPLOAD = 0;
	int INVENTOREXISTS = 0;
	String ADMIN_REVIEW_TERMS = null;
	String DEPARTMENT = null;
	String TAGGEDIRISTERMS = null;
	int urlPosition = 0;
	int totalTechnologies;
	ArrayList<String> id = new ArrayList<String>();
	ArrayList<String> TITLE = new ArrayList<String>();
	ArrayList<String> UID = new ArrayList<String>();
	ArrayList<String> RESEARCHER = new ArrayList<String>();
	ArrayList<String> FIRSTNAME = new ArrayList<String>();
	ArrayList<String> LASTNAME = new ArrayList<String>();
	ArrayList<String> CONTACTPERSON = new ArrayList<String>();
	ArrayList<String> INVENTORID = new ArrayList<String>();
	ArrayList<String> DESCRIPTION = new ArrayList<String>();
	ArrayList<String> KEYWORDS = new ArrayList<String>();
	ArrayList<String> PDFLINK = new ArrayList<String>();
	ArrayList<String> linklist = new ArrayList<String>();
	ArrayList<String> mainlist = new ArrayList<String>();
	ArrayList<String> urllist = new ArrayList<String>();
	ArrayList<String> multipleinventors = new ArrayList<String>();
	ArrayList<Integer> multipleinventorscount = new ArrayList<Integer>();
	List<String> keyword_hashset = new ArrayList<String>();
	Set<String> oldsubterm_hashset = new HashSet<String>();
	Set<String> oldBroadterm_hashset = new HashSet<String>();
	FileWriter filewriter;
	PrintWriter out1;
	int counter = 0;
	// for db connectivity
	Connection connect = null;
	Statement statement = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	private boolean BS_Validation;
	private String Keyword_B;

	void take_links() throws FileNotFoundException, InterruptedException {

		String temp = "";
		try {
			// System.out.println("connecting to link..");
			UserAgent userAgent = new UserAgent();
			userAgent
					.visit("http://technologylicensing.research.ufl.edu/technologies?limit=50&offset=0&query=");
			Element html = userAgent.doc.findEach("<div class=\"technology\">")
					.findEvery("a"); // find the html element
			List<Node> childNodes = html.getChildNodes(); // retrieve the
			// immediate child
			// nodes as a List

			for (Node node : childNodes) { // iterate through the list of Nodes.
				if (node.getType() == Node.ELEMENT_TYPE) { // determine whether
					// the node is an
					// Element
					if (((Element) node).outerHTML().contains("Read More")) {
					} else {
						temp = (String) ((Element) node).outerHTML();
						temp = temp.substring(temp.indexOf("<a href=") + 9,
								temp.indexOf(">") - 1);
						// ////System.out.println(temp);
						linklist.add(temp);
						// ////System.out.println("element: " +
						// ((Element)node).outerHTML()); //print the element and
						// its content
					}
				}

			}
			// get pages link

			Element pagelinks = userAgent.doc.findEach("<div class=\"pages\">")
					.findEvery("a");
			; // find the html element
			List<Node> pagelinkchildNodes = pagelinks.getChildNodes(); // retrieve
			// the
			// immediate
			// child
			// nodes
			// as a
			// List

			for (Node node : pagelinkchildNodes) { // iterate through the list
				// of Nodes.
				if (node.getType() == Node.ELEMENT_TYPE) { // determine whether
					// the node is an
					// Element
					temp = (String) ((Element) node).outerHTML();
					temp = temp.substring(temp.indexOf("<a href=") + 9,
							temp.indexOf(">") - 1);
					// ////System.out.println(temp);
					mainlist.add(temp);
					// ////System.out.println("element: " +
					// ((Element)node).outerHTML()); //print the element and its
					// content
				}

			}
			// print list of links including pagelinks

			// linklist.clear();
			for (int i = 0; i < mainlist.size(); i++) {
				userAgent.visit(mainlist.get(i));
				Element html1 = userAgent.doc.findEach(
						"<div class=\"technology\">").findEvery("a"); // find
				// the
				// html
				// element
				List<Node> childNodes1 = html1.getChildNodes(); // retrieve the
				// immediate
				// child
				// nodes as a
				// List

				for (Node node : childNodes1) { // iterate through the list of
					// Nodes.
					if (node.getType() == Node.ELEMENT_TYPE) { // determine
						// whether
						// the node is
						// an
						// Element
						if (((Element) node).outerHTML().contains("Read More")) {
						} else {
							temp = (String) ((Element) node).outerHTML();
							temp = temp.substring(temp.indexOf("<a href=") + 9,
									temp.indexOf(">") - 1);
							if (temp.contains("="))
								temp = temp
										.substring(temp.lastIndexOf("=") + 2);

							// ////System.out.println(temp);
							linklist.add(temp);

							// ////System.out.println("element: " +
							// ((Element)node).outerHTML()); //print the element
							// and
							// its content
						}
					}

				}

				// ////System.out.println(mainlist.get(i));
				// extractinfo(linklist.get(i));
				// ////System.out.println();
			}
			// ////System.out.println(linklist.get(i));
			// //System.out.println(linklist.size());
			// //System.out.println("-------------------");

			// //System.out.println(mainlist.size());

			// extract information
			HashSet hs = new HashSet();
			hs.addAll(linklist);
			linklist.clear();
			linklist.addAll(hs);
			// write all the links of arraylist to text file
			PrintWriter out = null;
			try {
				out = new PrintWriter(new FileWriter("source_links.txt"));
				// System.out.println("file created..");
				for (String text : linklist) {
					out.println(text);

					// //System.out.println(text);
				}
			} catch (IOException e) {
				// System.err.println("Caught IOException: " + e.getMessage());

			} finally {
				if (out != null) {
					out.close();
				}
			}

		} catch (JauntException e) {
			// System.err.println(e);
		}

	}

	void get_update() {
		UserAgent userAgent = new UserAgent();
		try {
			userAgent
					.visit("http://technologylicensing.research.ufl.edu/technologies?limit=50&offset=0&query=");
		} catch (ResponseException e) {
			e.printStackTrace();
		}

		Element pagelinks = userAgent.doc.findEach("<div class=\"response\">")
				.findEvery("em");
		List<Node> pagelinkchildNodes = pagelinks.getChildNodes();
		for (Node node : pagelinkchildNodes) {
			if (node.getType() == Node.ELEMENT_TYPE) {
				String temp = (String) ((Element) node).outerHTML();
				temp = temp.substring(temp.indexOf("of") + 3,
						temp.indexOf("technologies") - 1);

				totalTechnologies = Integer.parseInt(temp);
				System.out.println(totalTechnologies);

			}

		}
	}

	void read_links_from_file() throws InterruptedException, IOException {
		int counter = 1;
		int urlindex = 0;
		Scanner sc = new Scanner(new File("source_links.txt"));
		// System.out.println("File loaded..");
		// System.out.println("Executing please wait for few minutes......");

		while (sc.hasNext()) {
			counter++;
			urlindex++;
			System.out.println("urlindex: " + urlindex);
			// if(counter==5) break;
			if (counter % 100 == 0) {
				Thread.sleep(100);
				// System.out.println("thread slept");

			}
			extractinfo(sc.next(), urlindex);

		}

	}

	void extractinfo(String passedLink, int urlindex) throws IOException {
		try {
			 //System.out.println(passedLink);

			int index = 0;
			ArrayList<String> arr = new ArrayList<String>();
			// ArrayList<String> des = new ArrayList<String>();
			ArrayList<String> mgr_arr = new ArrayList<String>();
			ArrayList<String> keywords_arr = new ArrayList<String>();
			urllist.add(passedLink);
			UserAgent userAgent = new UserAgent();
			userAgent.visit(passedLink); // open HTML from a String.
			String bodyText = (String) userAgent.doc.innerHTML();
			Element title = userAgent.doc.findFirst("<title>");
			// get keywords and inventors in array list
			Elements researcher = userAgent.doc.findEach(
					"<dd class=\"inventor\">").findEvery("a");
			for (Element author : researcher) {
				// ////System.out.println("researchers: "+author.innerHTML()+"\n------\n");

				arr.add(author.innerHTML());

			}// print arraylist
				// get inventor id from <class="inventor" id="inventor_blabla">
				// String inventor="inventor_";
			int inventorIndex = bodyText.indexOf("inventor_");
			int inventorIndexlast = bodyText.indexOf("\">", inventorIndex + 9);
			String inventorId = bodyText.substring(inventorIndex + 9,
					inventorIndexlast);// System.out.println("inventor id: " +
										// inventorId);
			 //System.out.println("new inventor id"+inventorId);
			Element inventors = userAgent.doc.findEach(
					"<dd class=\"inventor\">").findEvery("a"); // find the html
			// element
			List<Node> inventorNodes = inventors.getChildNodes(); // retrieve
			// the //
			// immediate
			// child
			for (Node node : inventorNodes) { // iterate through the list of
				// Nodes.
				if (node.getType() == Node.ELEMENT_TYPE) { // determine whether
					// the node is an
					// Element
					//System.out.println("element-----------------: " +					 ((Element)node).outerHTML()); //print the element and its
					// content
					String inventorsid = ((Element) node).outerHTML();
					inventorsid = inventorsid.substring(
							inventorsid.indexOf("inventors/") + 10,
							inventorsid.indexOf("\">"));
					String temp = Integer.toString(urlindex) + "*"
							+ inventorsid;
					 //System.out.println("I'm back----------------"+temp);

					multipleinventors.add(temp);
					// Dump_to_Textfile(inventorsid, urlPosition);

				}

			}
			 //System.out.println("WordList: " + arr);
			 //System.out.println("wordlist size"+arr.size());
			multipleinventorscount.add(arr.size());
			String titleString = title.getText();
			String uid = title.getText();

			uid = uid.substring(uid.indexOf("University") - 12,
					uid.lastIndexOf("University") - 2);
			uid = uid.replaceAll("[^0-9]", "");
			id.add(uid);
			titleString = titleString.replace(titleString.substring(
					titleString.indexOf("University") - 11,
					titleString.lastIndexOf("Licensing") + 9), "");
			//System.out.println("Title: " + titleString);
			//System.out.println("UId: " + uid);

			TITLE.add(titleString);
			UID.add(uid);
			// processing words--------------------------

			if (arr.contains("Marketing PDF"))
				index = arr.indexOf("Marketing PDF");
			else
				index = arr.size();

			for (int k = 0; k < arr.size(); k++) {
				String pp = ((String) (arr.get(k)));
				if (pp.contains("Patent") == true) {// ////System.out.println("index of patentmmmmmm"+k);

					index = k;
					break;
				}

			}
			index = 0;
			// ////System.out.println(index+"of patent");
			RESEARCHER.add(arr.get(index));
			//System.out.println("Researcher: " + arr.get(index));
			//System.out.println("index of patent is"+arr.indexOf("Patent"));

			String abc = ((String) arr.get(index)).trim();
			 //System.out.println("STRING ABC:"+abc);

			 String[] Name = abc.split(" ");

			//System.out.println("arr list:"+arr);

			// String name = "";
			// for (int i = 0; i < Name.length - 1; i++)
			// name = name + " " + Name[i];
			// //System.out.println("First Name: " + name);
			FIRSTNAME.add(Naming.NameFname(abc));
			//System.out.println("Last Name: " + Name[Name.length - 1]);

			LASTNAME.add(Naming.NameLname(abc));
			// conctact person email/name
			Elements manager = userAgent.doc.findEach("<dd class=\"manager\">")
					.findEvery("a");
			for (Element author : manager) {
				// ////System.out.println("researchers: "+author.innerHTML()+"\n------\n");

				mgr_arr.add(author.innerHTML());

			}
			String contactPerson = (String) mgr_arr.get(0);
			if (contactPerson.contains("Denise")) {
				contactPerson = "Denise LaGasse";
			}
			//System.out.println("Contact Person: " + contactPerson);
			String contactPersonEid = getContactPersonEID(contactPerson);
			if(contactPersonEid==null)
			{
				contactPersonEid="No Email Id";
			}
			//System.out.println("contact person email id:" +			 contactPersonEid);
			CONTACTPERSON.add(contactPersonEid);
			// get inventor id from <class="inventor" id="inventor_blabla">
			// String inventor="inventor_";
			int inventorIndex1 = bodyText.indexOf("inventor_");
			String inventorId1 = bodyText.substring(inventorIndex1 + 9,
					inventorIndex1 + 16);
			inventorId1 = inventorId1.replaceAll("[^0-9]", "");
			 //System.out.println("inventor id: " + inventorId1);
			INVENTORID.add(inventorId1);
			// get description from <p> to<div id="nouvant-portfolio-footer">
			int pIndex = bodyText.indexOf("<p>");
			int div_nouvantIndex = bodyText
					.indexOf("<div id=\"nouvant-portfolio-footer\">");
			String descriptionText = bodyText.substring(pIndex,
					div_nouvantIndex);
			String descriptionText_with_no_Tags = removeTags(descriptionText);
			 //System.out.println("Description:\n" +			 descriptionText_with_no_Tags);
			// ////System.out.println();
			DESCRIPTION.add(descriptionText.replaceAll("</div>", ""));// .add(descriptionText_with_no_Tags);
			// display keywords
			 //System.out.print("Keywords: ");

			Elements keywords = userAgent.doc.findEach(
					"<dd class=\"categories\">").findEvery("a");
			for (Element author : keywords) {
				//System.out.println("researchers: "+author.innerHTML()+"\n------\n");
				keywords_arr.add(author.innerHTML());

			}
			 //System.out.println(keywords_arr);

			String keywords1 = "";
			for (int i = 0; i < keywords_arr.size(); i++) {
				keywords1 = keywords1 + keywords_arr.get(i) + ", ";
				//System.out.print(arr.get(i) + ", ");

			}
			keywords1 = keywords1.trim();
			// System.out.println("keywords1: "+keywords1);
			if (keywords1 != "")
				keywords1 = keywords1.substring(0, keywords1.length() - 1);
			 //System.out.println(keywords1);
			KEYWORDS.add(keywords1);

			if (uid.contains(" ") || uid.contains("-"))
				uid.replace(" ", "");
			uid = uid.trim();

			 //System.out.println("uid..."+uid+"...");

			String pd = "http://apps.research.ufl.edu/otl/pdf/marketing/" + uid
					+ ".pdf";
			 //System.out.println("Pdf link"+pd);

			PDFLINK.add(pd);

			 //System.out.println("----------------Record added----------------------------");
			arr.clear();
		} catch (JauntException e) {
			// //System.err.println(e);

		}

	}

	String removeTags(String a) {
		int ptr1 = 0, ptr2 = 0;
		// find first index of ptr1 and ptr2
		while (a.contains(">")) {
			ptr1 = a.indexOf("<");
			ptr2 = a.indexOf(">");
			String t = a.substring(ptr1, ptr2 + 1);
			if (t.contains("/"))
				a = a.replace(t, " ");
			else
				a = a.replace(t, "");
			ptr1 = ptr2 = 0;
		}

		return a;
	}

	static String DBUrl, userName, Password, UniversityURL, BaseURL;
	static {
		ReadConfigFile();
	}

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

				if (log.exists() == false) {
					System.out.println("We had to make a new file.");
					log.createNewFile();
				}
				SimpleDateFormat dt = new SimpleDateFormat(
						"yyyy-mm-dd hh:mm:ss");
				writer = new PrintWriter(new BufferedWriter(new FileWriter(log,
						true)));
				writer.append("\nInsert Failed:" + dt.format(new Date()));
				writer.append("\nDetails:");
				writer.append("\n\tCrawler UF failed:");
				writer.append("\n\tReason config File not found!!");
				writer.append("\n######################################\n\n");
				writer.close();
				System.exit(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	void readDataBase() throws Exception {
		try {

			// ******************** CIMES Live DB
			// CONNECTION***************************
			// String userName = "expertnetWeb";
			// String password = "Kaliban";
			// String url =
			// "jdbc:jtds:sqlserver://Mail1/ExpertNet2;instance=SQLEXPRESS";
			// Class.forName("net.sourceforge.jtds.jdbc.Driver");
			// Connection conn = DriverManager.getConnection(url, userName,
			// password);
			//
			// // ******************** CIMES3
			// CONNECTION***************************
//			 String userName = "expertnetWeb";
//			 String password = "Kaliban01.";
//			 String url =
//			 "jdbc:jtds:sqlserver://cimes3.its.fsu.edu/ExpertNet2_dev;instance=SQLEXPRESS";
			 Class.forName("net.sourceforge.jtds.jdbc.Driver");
			 Connection conn = DriverManager.getConnection(DBUrl, userName,Password);

			//
			// ******************** CIMES Live DB
			// CONNECTION***************************
//			String userName1 = userName;// "expertnetWeb";
//			String password = Password;// "Kaliban";
//			String url = DBUrl;// "jdbc:jtds:sqlserver://Mail1/ExpertNet2;instance=SQLEXPRESS";
//			Class.forName("net.sourceforge.jtds.jdbc.Driver");
//			Connection conn = DriverManager.getConnection(url, userName1,
//					password);

			System.out.println("Connected database successfully...");
			// statement = conn.createStatement();
			// System.out.println(totalTechnologies);
			// System.out.println("Inserting records into the table...");
			statement = conn.createStatement();
			System.out.println("totalTechnologies: " + totalTechnologies);
			// ********************************************************************

			for (urlPosition = 0; urlPosition < totalTechnologies; urlPosition++) {
				Timestamp tsCreated = getTimeInMillis();
				Timestamp tsModified = getTimeInMillis();

				String sql = "INSERT INTO dbo.propertyHolding " + "VALUES ('"
						+ DESCRIPTION.get(urlPosition).replaceAll("'", "''")
						+ "','"
						+ DEPARTMENT
						+ "' , '"
						+ urllist.get(urlPosition)
						+ "','"
						+ CONTACTPERSON.get(urlPosition).replaceAll("'", "''")
						+ "',7,'"
						+ TITLE.get(urlPosition).replaceAll("'", "''")
						+ "','"
						+ ADMIN_REVIEW_TERMS
						+ "','"
						+ TAGGEDIRISTERMS
						+ "','"
						+ KEYWORDS.get(urlPosition).replaceAll("'", "''")
						+ "',"
						+ ORIGINALINVENTORID
						+ ",'"
						+ UID.get(urlPosition)
						+ "','"
						+ FIRSTNAME.get(urlPosition).replaceAll("'", "''")
						+ "','"
						+ LASTNAME.get(urlPosition).replaceAll("'", "''")
								.replaceAll("&#39;", "''") + "','"
						+ INVENTORID.get(urlPosition).replaceAll("'", "''")
						+ "'," + INVENTOREXISTS + "," + ISREADYFORUPLOAD + ","
						+ ISUPLOAD + ",'" + tsCreated + "','" + tsModified
						+ "','"
						+ PDFLINK.get(urlPosition).replaceAll("'", "''") + "',"
						+ JOBID + ")";
				statement.executeUpdate(sql);
				System.out.println("Record no: " + (urlPosition + 1)
						+ " inserted");

			}

			String sql = "Select Min(pkID) from dbo.propertyHolding where fkUniversityID="
					+ 7 + " ";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				minpkid = rs.getInt(1);
				// System.out.println("number of rows= "+minpkid);

			}
			int ctr;
			int pointer = 0;
			String tnew = null;
			String universityUID = null;
			int universituid = 0;
			ctr = multipleinventorscount.get(pointer);
			System.out.println("Size of multple inventorslist is: "
					+ multipleinventors.size());
			for (int i = 0; i < multipleinventors.size(); i++) {
				String fkUniversityUID = "7";
				String t = multipleinventors.get(i);
				// System.out.println("inventor: "+t);
				if (t.charAt(t.length() - 1) == 'd')
					if (t.charAt(t.length() - 2) == '-') {
						// System.out.println("yup");
						t = t.substring(0, t.length() - 2) + "d";
						System.out.println("modified string: " + t);
					}
				// get fkid
				if (ctr < 2) {
					fkid = minpkid;
					pointer++;
					if (pointer < multipleinventorscount.size()) {
						ctr = 1 + multipleinventorscount.get(pointer);
						minpkid++;
					}
				} else
					fkid = minpkid;
				// System.out.println("fkid: "+fkid);
				// get universityuid

				sql = "Select universituid from dbo.propertyHolding where pkID="
						+ fkid + " ";
				rs = statement.executeQuery(sql);
				while (rs.next()) {
					universituid = rs.getInt(1);

				}
				universityUID = String.valueOf(universituid);
				// get names
				//System.out.println("fkid: " + fkid);
				fkUniversityUID = "7";
				String name[] = Naming.invFname(t);
				String fname = name[0];
				String lname = name[1];
				String inventorUID = name[2];
				// String lname=Naming.invFname(t);

				// String inventorUID=Naming.invUID(t);
				int OriginalInvID = 0;
				int inventorExists = INVENTOREXISTS;
				int jobid = JOBID;
				//System.out.println("fname: " + fname);
				//System.out.println("lname: " + lname);
				//System.out.println("inventorUID: " + inventorUID);
				String sql1 = "INSERT INTO dbo.inventorHolding " + "VALUES ('"
						+ fkid + "','" + fkUniversityUID + "','"
						+ universityUID + "','" + inventorUID + "','" + fname
						+ "','" + lname + "','','" + inventorExists + "','"
						+ OriginalInvID + "','" + jobid + "')";
				statement.executeUpdate(sql1);
				System.out.println("new Inventor inserted");
				ctr--;
			}
			// //update the fkid and universityuid
			// int ptr=0;int count;int uidptr=0;
			// for(int i=0;i<multipleinventors.size();i++)
			// { count=multipleinventorscount.get(ptr);
			// for(int j=0;j<count;j++)
			// {
			// //String sql="Update dbo.inventorHolding"+"SET fkId="+minpkid+""
			// PreparedStatement update = conn.prepareStatement
			// ("UPDATE dbo.inventorsHolding SET fkID = ?, universityUID = ?");
			// update.setInt(1, minpkid);
			// update.setString(2, UID.get(uidptr));
			// // update.executeUpdate();
			// }
			// minpkid++;uidptr++;
			// ptr++;
			//
			// }

			// ***************multiple inventors inserted********************

			System.out.println("Database updated with new record");

		} catch (SQLException ex) {
			// handle any errors
			System.out.println(urllist.get(urlPosition));
			System.out.println("SQLstack trace: " + ex.getStackTrace());
			System.out.println("SQLlocalized message: "
					+ ex.getLocalizedMessage());
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} finally {
			close();
		}

	}

	String UpperCase_name(String name) {
		String source = name;
		StringBuffer res = new StringBuffer();

		String[] strArr = source.split(" ");
		for (String str : strArr) {
			char[] stringArray = str.trim().toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			str = new String(stringArray);

			res.append(str).append(" ");
			// System.out.println("modified string is: "+str);

		}

		// System.out.print("Result: " + res.toString().trim());
		return res.toString().trim();

	}

	String getContactPersonEID(String contactPerson) {
		if (contactPerson == "Denise LaGassé") {
			contactPerson = "Denise LaGasse";
			// System.out.println("name changed");

		}
		ArrayList<String> directory = new ArrayList<String>();
		HashMap<String, String> dir = new HashMap<String, String>();

		try {
			// //System.out.println("printing");

			UserAgent userAgent = new UserAgent();
			userAgent.visit("http://www.research.ufl.edu/otl/contact.html");
			Elements tables = userAgent.doc.findEach("table").findEach(
					"<a href>");
			for (Element table : tables) {
				// //System.out.println(table.outerHTML());

				directory.add(table.outerHTML());

			}
			// process directory and store in it hash map
			for (Object a : directory) {
				String temp = (String) a;
				temp = temp.replace("&nbsp;", " ");
				// //System.out.println(temp);

				int ptr1 = temp.indexOf(":") + 1;
				int ptr2 = temp.lastIndexOf("\"");
				String eid = temp.substring(ptr1, ptr2);

				int ptr3 = temp.indexOf(">") + 1;
				int ptr4 = temp.lastIndexOf("<");
				String name = temp.substring(ptr3, ptr4);

				// //System.out.print(name+"\t");
				// //System.out.println(eid);

				dir.put(name, eid);

			}
			// displaying values of hashMap
			// //System.out.println("Values of hashmap");

			Iterator<String> itm = dir.keySet().iterator();
			while (itm.hasNext()) {
				String key = contactPerson + " ";
				// //System.out.print("key: "+key+"\t");

				return (String) dir.get(key);

			}

		} catch (ResponseException e) {
			// System.out.println(e);

		}
		return null;

	}

	void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			int pkid = resultSet.getInt("pkid");
			String Description = resultSet.getString("Description");
			String infoURL = resultSet.getString("infoURL");
			String conctactEmail = resultSet.getString("conctactEmail");
			int fkUniversityID = resultSet.getInt("fkUniversityID");
			String Title = resultSet.getString("Title");
			String BroadSubject = resultSet.getString("BroadSubject");
			String Keywords = resultSet.getString("Keywords");
			int Originalrow = resultSet.getInt("Originalrow");
			String universityuid = resultSet.getString("universityuid");
			String FirstName = resultSet.getString("FirstName");
			String LastName = resultSet.getString("LastName");
			int InventoryExists = resultSet.getInt("InventoryExists");
			int isReadyForUpload = resultSet.getInt("isReadyForUpload");
			int isUploaded = resultSet.getInt("isUploaded");
			// //System.out.println("Description: "+Description);
			// //System.out.println("infoURL: "+infoURL);
			// //System.out.println("conctact Email: "+conctactEmail);
			// //System.out.println("fkUniversityID: "+fkUniversityID);
			// //System.out.println("Title: "+Title);
			// //System.out.println("BroadSubject: "+BroadSubject);
			// //System.out.println("Keywords: "+Keywords);
			// //System.out.println("Originalrow: "+Originalrow);
			// //System.out.println("Universityuid: "+universityuid);
			// //System.out.println("FirstName: "+FirstName);
			// //System.out.println("LastName: "+LastName);
			// //System.out.println("InventoryExists: "+InventoryExists);
			// //System.out.println("isReadyForUpload: "+isReadyForUpload);
			// //System.out.println("isUploaded: "+isUploaded);

		}
	}

	// You need to close the resultSet
	void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}

	void send_mail() {
		final String username = "nikhilkarmude00@gmail.com";
		final String password = "";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("nikhilkarmude00@gmail.com"));// from-email@gmail.com
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("nvk13@my.fsu.edu"));// to-email@gmail.com
			message.setSubject("Testing Subject");
			String msg = "<strong>Hello World!</strong>";

			message.setText("Hi There," + "\n\n This is testing mail!");
			message.setContent(msg, "text/html;charset=utf-8");
			Transport.send(message);

			// System.out.println("Mail sent");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

	static java.sql.Timestamp getTimeInMillis() {
		// System.out.println("in date function");

		java.util.Date today = new java.util.Date();
		// System.out.println(new Timestamp(today.getTime()));
		return new Timestamp(today.getTime());

	}

}

public class UFCrawl {

	public static void main(String args[]) throws Exception {
		Process p = new Process();
		p.get_update();
		p.getTimeInMillis();
		System.out.println("Recieved Update...");
		p.take_links();
		System.out.println("Extracting Links...");
		System.out.println("Processing Data...");
		p.read_links_from_file();
		System.out.println("Data Process Completed...");
		System.out.println("Inserting Records in Database...");

		p.readDataBase();
		// Translation.main(new String []{""});

		System.out.println("**********Spider Application Run Completed!!***********");

//		 p.extractinfo("http://technologylicensing.research.ufl.edu/technologies/13458_engineered-tissue-for-treating-tmj-disorder-more-effectively",
//		6);

	}

}