package serikov.dmitrii.utils;

import static serikov.dmitrii.utils.Utils.errorMesage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import serikov.dmitrii.model.UserProfile;

/**
 * @author Dmitrii Serikov
 */
public class DBUtils {
	private static final Logger logger = LoggerFactory.getLogger(DBUtils.class);

	private final static String DEFAULT_MYSQL_PORT = "3306";

	private static Connection con = null;

	public static enum ConnectionType {
		REMOTE_DB, LOCAL_DB
	}

	public static boolean loadDriver() {
		// The newInstance() call is a work around for some
		// broken Java implementations
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			return true;
		} catch (Exception e) {
			logger.error(errorMesage(e));
			return false;
		}
	}

	private static boolean connectToRemoteDB() {
		// get app properties
		Map<String, String> appProperties = PropertyUtils.getAppProperties();
		String MYSQL_CLUSTER_IP = appProperties.get("MYSQL_CLUSTER_IP");
		String MYSQL_DATABASE = appProperties.get("MYSQL_DATABASE");
		String MYSQL_PORT = appProperties.get("MYSQL_PORT");
		if (MYSQL_PORT == null) {
			MYSQL_PORT = DEFAULT_MYSQL_PORT;
		}
		String MYSQL_USER = appProperties.get("MYSQL_USER");
		String MYSQL_PASSWORD = appProperties.get("MYSQL_PASSWORD");

		StringBuilder url = new StringBuilder();
		if (MYSQL_CLUSTER_IP != null) {
			logger.info("Trying to connect to remote database at " + MYSQL_CLUSTER_IP + "...");

			url.append("jdbc:mysql://").append(MYSQL_CLUSTER_IP).append(":").append(MYSQL_PORT).append("/")
					.append(MYSQL_DATABASE).append("?verifyServerCertificate=false").append("&useSSL=true")
					.append("&requireSSL=true");
			try (Connection con = DriverManager.getConnection(url.toString(), MYSQL_USER, MYSQL_PASSWORD)) {
				logger.info("Connection to the database established");
				return true;
			} catch (SQLException e) {
				logger.error("Connection to the database hasn't been established");
			}
		} else {
			logger.info("Cluster IP is not defined...");
		}
		return false;
	}

	private static boolean connectToLocalDB() {
		logger.info("Trying to connect to local database...");
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:" + DEFAULT_MYSQL_PORT + "/lister", "root",
				"root")) {
			logger.info("Connection to the database established");
			return true;
		} catch (SQLException ex2) {
			logger.error("Connection to the database hasn't been established");
		}
		return false;
	}

	public static boolean connect() {
		return connect(Arrays.asList(ConnectionType.REMOTE_DB));
	}

	public static boolean connect(List<ConnectionType> order) {
		logger.info("Attempting to connect to the database");

		if (order != null) {
			boolean result = false;
			for (ConnectionType connectionType : order) {
				switch (connectionType) {
				case REMOTE_DB:
					// trying to connect to the remote database
					result = connectToRemoteDB();
					break;
				case LOCAL_DB:
					// trying to connect to the local database
					result = connectToLocalDB();
					break;
				}
			}
			return result;
		}
		return false;
	}

	public static void disconnect() {
		try {
			con.close();
		} catch (SQLException e) {
			logger.error(errorMesage(e));
		}
	}

	public static boolean checkUserExistance(String username) {
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT UserName FROM users");
			while (rs.next()) {
				// check if 'username' equalsIgnoreCase to 'UserName' in 'users'
				// table
				if (username.equalsIgnoreCase(rs.getString("UserName"))) {
					rs.close();
					stmt.close();
					return true;
				}
			}
			rs.close();
			stmt.close();
			return false;
		} catch (SQLException e) {
			logger.error(errorMesage(e));
			return false;
		}
	}

	public static boolean checkListExistance(String username, String listname) {
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT UserName, ListName FROM lists WHERE UserName='" + username
					+ "' AND ListName='" + listname + "'");
			while (rs.next()) {
				// check if 'username' equalsIgnoreCase to 'UserName' in 'users'
				// table
				rs.close();
				stmt.close();
				return true;
			}
			rs.close();
			stmt.close();
			return false;
		} catch (SQLException e) {
			logger.error(errorMesage(e));
			return false;
		}
	}

	public static boolean createUser(String username, String password, String avatar) {
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO users(UserName, Password, Avatar) " + "VALUES('" + username + "', '"
					+ password + "', '" + avatar + "')");
			stmt.close();
			return true;
		} catch (SQLException e) {
			logger.error(errorMesage(e));
			return false;
		}
	}

	public static boolean checkCreds(String username, String password) {
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT UserName, Password FROM users");
			while (rs.next()) {
				// check if 'username' equalsIgnoreCase to 'UserName' in 'users'
				// table
				if (username.equalsIgnoreCase(rs.getString("UserName"))) {
					// check if 'password' equals to 'Password' in 'users' table
					if (password.equals(rs.getString("Password"))) {
						rs.close();
						stmt.close();
						return true;
					}
				}
			}
			rs.close();
			stmt.close();
			return false;
		} catch (SQLException e) {
			logger.error(errorMesage(e));
			return false;
		}
	}

	public static UserProfile getUserProfile(String username) {
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users");
			while (rs.next()) {
				// check if 'username' equalsIgnoreCase to 'UserName' in 'users'
				// table
				if (username.equalsIgnoreCase(rs.getString("UserName"))) {
					// return UserProfile object
					UserProfile userInfo = new UserProfile(rs.getString("UserName"), rs.getString("Avatar"));
					rs.close();
					stmt.close();
					return userInfo;
				}
			}
			rs.close();
			stmt.close();
			return null;
		} catch (SQLException e) {
			logger.error(errorMesage(e));
			return null;
		}
	}

	public static List<String> getLists(String username) {
		try {
			List<String> lists = new ArrayList<>();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM lists WHERE UserName='" + username + "' ORDER BY ListOrder");
			while (rs.next()) {
				// check if 'username' equalsIgnoreCase to 'UserName' in 'users'
				// table
				if (username.equalsIgnoreCase(rs.getString("UserName"))) {
					// logger.info(rs.getString("UserName") + ", " +
					// rs.getString("ListName"));
					lists.add(rs.getString("ListName"));
				}
			}
			rs.close();
			stmt.close();
			return lists;
		} catch (SQLException e) {
			logger.error(errorMesage(e));
			return null;
		}
	}

	public static boolean createList(String username, String listname, int listsSize) {
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO lists(UserName, ListName, ListOrder) " + "VALUES('" + username + "', '"
					+ listname + "', '" + listsSize + "')", Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			long listID = -1;
			if (rs.next()) {
				listID = rs.getLong(1);
			}
			stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO list_contents(ListID, DataRef) " + "VALUES('" + listID + "', '/data/"
					+ username + "_" + listname + ".dt')");
			rs.close();
			stmt.close();
			return true;
		} catch (SQLException e) {
			logger.error(errorMesage(e));
			return false;
		}
	}

	public static String renameList(String username, String oldListname, String listname) {
		try {
			Statement stmt = con.createStatement();
			int result = stmt.executeUpdate("UPDATE lists SET ListName='" + listname + "' WHERE UserName='" + username
					+ "' AND ListName='" + oldListname + "'");
			if (result != 1) {
				throw new SQLException("There is no rows to modify in table 'lists'");
			}
			updateListContentRef(username, listname);
			stmt.close();
			logger.info("The list with name '" + oldListname + "' was renamed to '" + listname + "'");
			return "/data/" + username + "_" + listname + ".dt";
		} catch (SQLException e) {
			logger.error(errorMesage(e));
			return null;
		}
	}

	public static boolean removeList(String username, String listname, int listsSize) {
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT ListOrder FROM lists WHERE UserName='" + username + "' AND ListName='" + listname + "'");
			String currentListOrder = null;
			while (rs.next()) {
				currentListOrder = rs.getString("ListOrder");
			}
			//
			logger.info("currentListOrder = " + currentListOrder);
			try {
				int intCurrentListOrder = Integer.parseInt(currentListOrder);
				if (intCurrentListOrder < 0 || intCurrentListOrder > listsSize - 1) {
					throw new Exception("List data integrity was violated");
				}
				int subtracter = 1;
				for (int i = intCurrentListOrder + 1; i < listsSize; i++) {
					ResultSet rs1 = stmt.executeQuery(
							"SELECT ListName FROM lists WHERE UserName='" + username + "' AND ListOrder='" + i + "'");
					String nextListName = null;
					while (rs1.next()) {
						nextListName = rs1.getString("ListName");
					}
					if (nextListName == null) {
						subtracter += 1;
						continue;
					}
					int result = stmt.executeUpdate("UPDATE lists SET ListOrder='" + (i - subtracter)
							+ "' WHERE UserName='" + username + "' AND ListName='" + nextListName + "'");
					if (result != 1) {
						throw new SQLException("There is no rows to delete from table 'lists'");
					}
				}
			} catch (Exception e) {
				throw new SQLException(e.getMessage());
			}
			// removing the current list from
			int result = stmt.executeUpdate(
					"DELETE FROM lists WHERE UserName='" + username + "' AND ListName='" + listname + "'");
			if (result != 1) {
				throw new SQLException("There is no rows to delete from table 'lists'");
			}
			stmt.close();
			logger.info("The record in the database was removed");
			return true;
		} catch (SQLException e) {
			logger.error(errorMesage(e));
			return false;
		}
	}

	public static String getListContentRef(String username, String listname) throws Exception {
		String DataRef = null;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery(
					"SELECT ListID FROM lists WHERE UserName='" + username + "' AND ListName='" + listname + "'");
			String listID = null;
			while (rs.next()) {
				listID = rs.getString("ListID");
			}
			rs.close();
			rs = stmt.executeQuery("SELECT DataRef FROM list_contents WHERE ListID='" + listID + "'");
			while (rs.next()) {
				DataRef = rs.getString("DataRef");
				// logger.info(DataRef);
			}
			stmt.close();
		} catch (SQLException e) {
			logger.error(errorMesage(e));
		}
		return DataRef;
	}

	private static void updateListContentRef(String username, String listname) throws SQLException {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(
				"SELECT ListID FROM lists WHERE UserName='" + username + "' AND ListName='" + listname + "'");
		String listID = null;
		while (rs.next()) {
			listID = rs.getString("ListID");
		}
		rs.close();
		stmt = con.createStatement();
		int result = stmt.executeUpdate("UPDATE list_contents SET DataRef='/data/" + username + "_" + listname
				+ ".dt' WHERE ListID='" + listID + "'");
		if (result != 1) {
			throw new SQLException("There is no rows to modify in table 'list_contents'");
		}
		stmt.close();
	}

	public static void changeListsOrder(String username, List<String> oldListTitles, List<String> newListIndexes) {
		try {
			logger.info(newListIndexes.toString());
			Statement stmt = con.createStatement();
			for (int i = 0; i < oldListTitles.size(); i++) {
				stmt.executeUpdate("UPDATE lists SET ListOrder='" + newListIndexes.get(i) + "' WHERE UserName='"
						+ username + "' AND ListName='" + oldListTitles.get(i) + "'");
			}
			stmt.close();
		} catch (SQLException e) {
			logger.error(errorMesage(e));
		}
	}
}
