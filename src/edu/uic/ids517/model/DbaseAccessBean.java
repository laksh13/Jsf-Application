package edu.uic.ids517.model;

import java.util.ArrayList;
//import java.util.Date;
import java.util.List;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
//import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import javax.servlet.*;

import edu.uic.ids517.model.MessageConstants;
import edu.uic.ids517.model.MessageBean;
import edu.uic.ids517.model.DbUserInfo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped

public class DbaseAccessBean { 

	private DbUserInfo dbUserInfo;
	private MessageBean messageBean;

	private String userName;
	private String password;
	private String host;
	private String url;
	private String dbSchema;
	private String dbms;
	private String jdbcDriver;

	private boolean status = false;

	private String queryType;
	private String sqlQuery;
	private String errorMessage;
	private String statusMessage;
	private String logoutMessage = "";

	private Connection connection;
	private DatabaseMetaData databaseMetaData;
	private Statement statement;
	private ResultSet resultSet, rs;
	private Result result;
	private int numberColumns;
	private int numberRows;
	private String tableName;
	private ArrayList<String>tableListNames;
	private ArrayList<String>columnNames;
	private ArrayList<String>selectedColumnNames;
	private boolean renderResult; 
	private ResultSet MetaDatarsmd;

	private static final String [] TABLE_TYPES={"TABLE", "VIEW"};

	public void init() throws ServletException {

	}

	public boolean connect() throws SQLException{	

		userName = dbUserInfo.getUserName();
		password = dbUserInfo.getPassword();
		host = dbUserInfo.getDbmsHost();
		dbSchema = dbUserInfo.getDatabaseSchema();

		dbms = dbUserInfo.getDbms();

		jdbcDriver = "com.mysql.jdbc.Driver";
		url = "jdbc:mysql://" + host + ":3306/" + dbSchema;

		/*if(dbms.equalsIgnoreCase("mysql"))
		{
			jdbcDriver = "com.mysql.jdbc.Driver";
			url = "jdbc:mysql://" + host + ":3306/" + dbSchema;
		}
		else if(dbms.equalsIgnoreCase("db2"))
		{
			jdbcDriver = "com.ibm.db2.jcc.DB2Driver";
			url = "jdbc:db2://" + host + ":50000/" + dbSchema;
		}
		else if(dbms.equalsIgnoreCase("oracle"))
		{
			jdbcDriver = "oracle.jdbc.driver.OracleDriver";
			url = "jdbc:oracle:thin:@" + host + ":1521/" + dbSchema;
		}
		else
		{
			System.err.println ("Invalid Database");
			return false;
		}*/

		try {
			//Load the jdbc driver
			Class.forName(jdbcDriver);

			//Get connection object to the database
			connection = DriverManager.getConnection(url, userName, password);

			//Create a statement object that can navigate forward and backwards and refreshes the database updates
			statement = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

			databaseMetaData = connection.getMetaData();
			status = true;

		} catch (ClassNotFoundException e) {messageBean.setMessage(MessageConstants.LOGIN_FAIL);

		status = false;
		} catch (SQLException e) {
			e.printStackTrace();
			status = false;
		}
		catch (Exception e) {
			e.printStackTrace();
			status = false;
		}

		return status;
	}

	public boolean closeConnect(){
		try{

			if(status==true)
			{
			//	rs.close();
			//	statement.close();
				connection.close(); 
			}
			logoutMessage = "All Connections Closed";
		}
		catch(SQLException ex) {
			System.err.println("SQLException information");
			while (ex!=null) {
				System.err.println ("Error message: " + ex.getMessage());
				System.err.println ("SQLSTATE: " + ex.getSQLState());
				System.err.println ("Error Code: " + ex.getErrorCode());
				ex.printStackTrace();
				ex=ex.getNextException();     }
		}
		return status;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean executeSQL(String sqlQuery) {

		String queryType = sqlQuery.substring(0, 6);

		//Any database update query
		if(!queryType.equalsIgnoreCase ("SELECT")) {

			try {
				statement.executeUpdate(sqlQuery);
				setStatusMessage("Query executed:  " + sqlQuery);
				
				return true;
			}
			catch(SQLException e) {
				//	System.err.println("SQLState: " + ((SQLException)e).getSQLState());
				//	System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
				errorMessage = e.getMessage();
				return false;
			}
		}
		
		else if(queryType.equalsIgnoreCase("select") ) {
			try {
				resultSet = statement.executeQuery(sqlQuery);
				rs = resultSet;
				setStatusMessage("Query executed:  " + sqlQuery);
				// printException("SQL query executed:" + sqlQuery);
				return true;
			}
			catch(SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		setSqlQuery("test query");
		
		return true;
	}

	public boolean generateResult() {

		renderResult = true;

		try {
			result = ResultSupport.toResult(rs);
			ResultSetMetaData rsmd = rs.getMetaData();
			numberColumns = rsmd.getColumnCount();
			numberRows = result.getRowCount();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			renderResult = false;
			return false;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			renderResult = false;
			return false;
		}
		String columnNameList [] = result.getColumnNames();
		columnNames = new ArrayList<String>(numberColumns);
		for(int i=0; i<numberColumns; i++) {
			columnNames.add(columnNameList[i]);

		}
		return true;
	}

	public ResultSet ExecuteQuery(String query) throws SQLException {
		ResultSet rs = null;
		if (!status) {
			return rs;
		}
		PreparedStatement statement = connection.prepareStatement(query);

		rs = statement.executeQuery();
		
		setSqlQuery("tes query lagi");

		return rs;
	}



	public ArrayList <String> tableList() {
		ArrayList<String> tableList = null;
		try {

			rs = databaseMetaData.getTables(null, userName, null, TABLE_TYPES);
			rs.last();
			int numberRows = rs.getRow();

			tableList = new ArrayList<String>(numberRows); 

			rs.beforeFirst();

			String tableName="";
			if (rs != null) {
				while(rs.next()) {
					//Retrieve the table from the result set 
					tableName = rs.getString("TABLE_NAME"); 
					if (!dbms.equalsIgnoreCase("oracle") || tableName.length()<4)
						tableList.add(tableName);
					else if (!tableName.substring(0,4).equalsIgnoreCase("BIN$"))
						tableList.add(tableName);
				}
			}	

			this.setTableListNames(tableList);
			
			if ( ! tableList.isEmpty())
					this.setTableName(tableList.get(0));

		}
		catch (SQLException e) {
			System.err.println("SQLState: " + ((SQLException)e).getSQLState());
			System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
			System.err.println("Message: " + e.getMessage());

		}
		return tableList;
	}

	public List <String> columnList(String tableGiven) {
		ArrayList<String> columnList = null;
		try {
			rs = databaseMetaData.getColumns(null, userName, tableGiven, "%");
			rs.last();
			int numberRows = rs.getRow();

			columnList = new ArrayList<String>(numberRows); 

			rs.beforeFirst();

			String columnName="";
			if (rs != null) {
				while(rs.next()) {
					//Retrieve the table from the result set 
					columnName = rs.getString("COLUMN_NAME"); 
					columnList.add(columnName);
					/*if (!dbms.equalsIgnoreCase("oracle") || tableName.length()<4)
						tableList.add(tableName);
					else if (!tableName.substring(0,4).equalsIgnoreCase("BIN$"))
						tableList.add(tableName);*/
				}
			}
			this.setColumnNames(columnList);
		}
		catch (SQLException e) {
			System.err.println("SQLState: " + ((SQLException)e).getSQLState());
			System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
			System.err.println("Message: " + e.getMessage());

		}
		return columnList;
	}

	public DbUserInfo getDbUserInfo() {
		return dbUserInfo;
	}

	public void setDbUserInfo(DbUserInfo dbUserInfo) {
		this.dbUserInfo = dbUserInfo;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public ResultSet getRs() {
		return rs;
	}
	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
	public String getDbms() {
		return dbms;
	}
	public void setDbms(String dbms) {
		this.dbms = dbms;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public int getNumberColumns() {
		return numberColumns;
	}

	public void setNumberColumns(int numberColumns) {
		this.numberColumns = numberColumns;
	}

	public int getNumberRows() {
		return numberRows;
	}

	public void setNumberRows(int numberRows) {
		this.numberRows = numberRows;
	}

	public ArrayList<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(ArrayList<String> columnNames) {
		this.columnNames = columnNames;
	}

	public boolean isRenderResult() {
		return renderResult;
	}

	public void setRenderResult(boolean renderResult) {
		this.renderResult = renderResult;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public ArrayList<String> getTableListNames() {
		return tableListNames;
	}

	public void setTableListNames(ArrayList<String> tableListNames) {
		this.tableListNames = tableListNames;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public ArrayList<String> getSelectedColumnNames() {
		return selectedColumnNames;
	}

	public void setSelectedColumnNames(ArrayList<String> selectedColumnNames) {
		this.selectedColumnNames = selectedColumnNames;
	}
	
	public void reset() {
		this.setTableListNames(null);
		this.setTableName(null);
		this.setColumnNames(null);
		this.setNumberRows(0);
		this.setNumberRows(0);
	}

	public String getLogoutMessage() {
		return logoutMessage;
	}

	public void setLogoutMessage(String logoutMessage) {
		this.logoutMessage = logoutMessage;
	}
	
	public void createAllTables() {
		//messageBean.clearMessage();
		
		setQueryType("CREATE");
		executeSQL(
		"CREATE TABLE IF NOT EXISTS bmiTable (id INT NOT NULL AUTO_INCREMENT, bmiUser VARCHAR(50) NULL, units VARCHAR(20) NULL, weight DOUBLE NULL, height DOUBLE NULL, bmi DOUBLE NULL, bmiPrime DOUBLE NULL, bmiCategory VARCHAR(100) NULL, PRIMARY KEY (id))"
		); 
		executeSQL(
		"CREATE TABLE IF NOT EXISTS loanTable (id INT NOT NULL AUTO_INCREMENT, loanUser VARCHAR(50) NULL, purchasePrice DOUBLE NULL, downPayment DOUBLE NULL, interestRate DOUBLE NULL, term DOUBLE NULL, monthlyPayment DOUBLE NULL, totalPayment DOUBLE NULL, totalWithDownPayment DOUBLE NULL, totalInterest DOUBLE NULL, PRIMARY KEY (id))"
		);
		executeSQL(
		"CREATE TABLE IF NOT EXISTS csvMetadataTable ( table_name varchar(30) NOT NULL, column_names varchar(256) NOT NULL, metadata int(11) NOT NULL, PRIMARY KEY (table_name) )"		
		);
		executeSQL(
				"CREATE TABLE IF NOT EXISTS csvTable ( id int(11) NOT NULL AUTO_INCREMENT, table_name varchar(50) DEFAULT NULL, row_num int(11) DEFAULT NULL, c1 double DEFAULT NULL, c2 double DEFAULT NULL, c3 double DEFAULT NULL, c4 double DEFAULT NULL, c5 double DEFAULT NULL, c6 double DEFAULT NULL, c7 double DEFAULT NULL, c8 double DEFAULT NULL, PRIMARY KEY (id) )"		
		);
		executeSQL(
		"CREATE TABLE IF NOT EXISTS userTrackerTable ( id int(11) NOT NULL AUTO_INCREMENT, user_name varchar(50) DEFAULT NULL, start_time datetime DEFAULT NULL, end_time datetime DEFAULT NULL, ip_address varchar(50) DEFAULT NULL COMMENT 'table for store user login and logout activity', PRIMARY KEY (id) )"		
		);
	}


}

