package edu.uic.ids517.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.uic.ids517.model.DataBean;
import edu.uic.ids517.model.DbaseAccessBean;
import edu.uic.ids517.model.MessageBean;
import edu.uic.ids517.model.DbUserInfo;

@ViewScoped
@ManagedBean(name = "actionBeanFile")
public class ActionBeanFile {

	private MessageBean messageBean;
	private DbaseAccessBean dbaseAccessBean;
	private DataBean dataBean;
	private ArrayList<DataBean> dataBeanArrayList;

	private char dataSeparator = ',';
	private UploadedFile uploadedFile;
	private String uploadedFileContents = null;
	private DbUserInfo dbUserInfo;
	private String fileLabel;
	private String fileName;
	private long fileSize;	
	private boolean fileImportError = true;
	private int numberRows;
	private boolean renderParseTable;
	String insertQuery;
	Result result;
	boolean showAll = false;
	boolean renderUploadGrid = true;
	private String message = "";
	private boolean fileImport = false; // to show or hide message

	public ActionBeanFile() {
		// constructor command here
	}

	public String processFileUpload() {
		
		// clear all message
		messageBean.clearMessage();
		
		// if label is empty use file name as label
		if ( isLabelExist(fileLabel) ) {
			return "fail";
		}		
		
		String columnNamesMeta = "";

		// prepare for uploading files
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();

		String path = context.getExternalContext().getRealPath("/temp");

		File tempFile = null;
		FileOutputStream fos = null;

		int nCols = 0;
		int n = 0;

		try {
			// get uploaded file
			fileName = new File(uploadedFile.getName()).getName();
			fileSize = uploadedFile.getSize();
			String fileContentType = uploadedFile.getContentType();

			tempFile = new File(path + "/" + "fileName");

			// write temp file to disk
			fos = new FileOutputStream(tempFile);
			fos.write(uploadedFile.getBytes());
			fos.close();

			// read data from temp file using scanner
			Scanner csvData = new Scanner(tempFile);
			String rowCsvData;
				
			// do data validation in the method csvDataValid in the same class
			if ( ! this.csvDataValid(csvData)) {
				return "fail";
			}	
			
			// continue to process uploaded file			
			// working with column names
			
			csvData = new Scanner(tempFile);
			
			// first row is column name
			rowCsvData = csvData.nextLine();
			columnNamesMeta = rowCsvData;
			
			String[] arrRowCsvData = rowCsvData.split(","); // assumed data separated by ,

			// prepare to save data to table csvTable
			dbaseAccessBean.setTableName("csvTable");
			nCols = arrRowCsvData.length;

			// lets build columns used in insert statement
			// get columns name from given table name
			List<String> columnsList = dbaseAccessBean.columnList(dbaseAccessBean.getTableName());

			String[] columnsCsvTable = new String[columnsList.size()];
			// convert column list to column array
			columnsCsvTable = columnsList.toArray(columnsCsvTable);

			int i = 0;
			
			// table_name column
			StringBuffer columnsFinal = new StringBuffer("(`" + columnsCsvTable[i + 1] + "`");

			// c1, c2, ... columns
			while (i <= nCols) {
				i++;
				columnsFinal.append(", `" + columnsCsvTable[i + 1] + "`");
			};

			// close column names in insert statement
			columnsFinal.append(")");
			
			int row_num = 1;

			// insert data csv except column name to table csvTable
			while (csvData.hasNext()) {

				// continue build insert statement
				StringBuffer insertFinal = new StringBuffer("");

				// save to database
				String sqlQuery = "INSERT INTO `"
						+ dbaseAccessBean.getTableName() + "` " + columnsFinal
						+ " VALUES ";

				// append data to insert sql query
				
				// split rowCsvData to array
				rowCsvData = csvData.nextLine();
				arrRowCsvData = rowCsvData.split(",");

				nCols = arrRowCsvData.length;

				// build insert value
				StringBuffer sb = new StringBuffer("('" + this.getFileLabel() + "','"
						+ row_num + "','"
						+ rowCsvData.replace(",", "','") + "')");
				insertFinal.append(sb.toString());

				sqlQuery += insertFinal;

				dbaseAccessBean.setQueryType("INSERT");
				dbaseAccessBean.executeSQL(sqlQuery);

				n++;
				row_num++;
			}

			csvData.close();
			
			numberRows = n;
			boolean fileImport = true;
			boolean renderParseTable = true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			messageBean.setErrorMessage("Error reading data from file "
					+ fileName + ", label: " + fileLabel);

			boolean fileImportError = true;

		} // end catch		

		// insert to table metadata
		insertToMetadataTable( fileLabel, columnNamesMeta, n);

		// display table
		dbaseAccessBean.setTableName("csvTable");
		
		dbaseAccessBean.setQueryType("SELECT");
		String sqlQuery = "SELECT * FROM " + dbaseAccessBean.getTableName();
		dbaseAccessBean.executeSQL(sqlQuery);
		dbaseAccessBean.generateResult();
		dbaseAccessBean.setSqlQuery(sqlQuery);
		
		// display success message
		messageBean.setMessage("Import csv file success.");
		messageBean.setShowMessage(true);

		return "success";

	}

	public String processDataImport() throws SQLException {
	

		return "SUCCESS";
	}

	public String allTables() {
		dbaseAccessBean.setQueryType("select");
		dbaseAccessBean
				.executeSQL("SELECT table_name FROM information_schema.tables WHERE table_type = "
						+ " 'base table'AND table_schema = '"
						+ dbUserInfo.databaseSchema + "'");
		dbaseAccessBean.generateResult();
		result = dbaseAccessBean.getResult();
		showAll = true;

		return "success";
	}

	public String allUserTables() {
		dbaseAccessBean.setQueryType("select");
		dbaseAccessBean
				.executeSQL("SELECT table_name FROM information_schema.tables WHERE table_type = "
						+ " 'base table'AND table_schema = '"
						+ dbUserInfo.databaseSchema
						+ "' AND "
						+ "table_name NOT LIKE '%user%' AND table_name NOT LIKE '%logs%'");
		dbaseAccessBean.generateResult();
		result = dbaseAccessBean.getResult();
		showAll = true;

		return "success";
	}

	public String processShowData() {
		messageBean.clearMessage();		

		dbaseAccessBean.setQueryType("SELECT");
		String tableName = "csvTable";
		dbaseAccessBean.setTableName(tableName);
		
		ArrayList<String> mSelectedColumnsList = dataBean.getCsvTableSelectedColumnNames();
		ArrayList<String> mColumnsList = dataBean.getCsvTableColumnNames();
		String selectedDataset = dataBean.getSelectedDataset();
		
		String sqlQuery = "";
		if (mSelectedColumnsList != null) {
			System.out.println("selected column list is not null");
			sqlQuery = "SELECT " + StringUtils.join(mSelectedColumnsList, ',')
					+ " FROM " + tableName 
					+ " WHERE `table_name`='" + selectedDataset + "'";

		} else {
			System.out.println("selected columnlist null");
			sqlQuery = "SELECT " + StringUtils.join(mColumnsList, ',')
					+ " FROM " + tableName
					+ " WHERE `table_name`='" + selectedDataset + "'";
		}
		System.out.println("Query:"+sqlQuery);
		dbaseAccessBean.executeSQL(sqlQuery);
		dbaseAccessBean.generateResult();
		dbaseAccessBean.setSqlQuery(sqlQuery);
		
		// set other properties of dbaseAccessBean for getStatistics operation
		//dbaseAccessBean.setColumnNames(mColumnsList);
		dbaseAccessBean.setSelectedColumnNames(mSelectedColumnsList);
		dbaseAccessBean.setTableName(tableName);
		
		// copy data result set and result to dataBean
		dataBean.setResultSet(dbaseAccessBean.getResultSet());
		dataBean.setResult(dbaseAccessBean.getResult());
		dataBean.setRenderResult(true);

		return "success";
	}

	public String processFileDownload() {
		
		String dataset = dataBean.getSelectedDataset();

		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();

		FileOutputStream fos = null;

		String path = fc.getExternalContext().getRealPath("/temp");
		System.out.println("Export path:"+path);
		String tableName = dbaseAccessBean.getTableName();
		
		String fileNameBase = "";
		System.out.println("dataset:"+dataset);
		System.out.println("TableName:"+tableName);
		if (dataset.isEmpty()) {
			fileNameBase = tableName + ".csv";
		} else {
			fileNameBase = dataset + ".csv";
		}

		String fileName = path + "/" + "UserName" + "_" + fileNameBase;
		System.out.println("fileName:"+fileName);

		File f = new File(fileName);

		// get data from table
		processShowData();
		
		// check for selected dataset and columns
		if (! this.isDatasetAndColumnNamesSelected()) {
			return "fail";
		}

		Result result = dbaseAccessBean.getResult();
		Object[][] sData = result.getRowsByIndex(); // sData[indexRow][indexColumn]

		String columnNames[] = result.getColumnNames();
		int columnCount = columnNames.length;
		int rowCount = result.getRowCount();
		StringBuffer sb = new StringBuffer();
		
		// write data to file
		
		try {
			fos = new FileOutputStream(fileName);

			for (int i = 0; i < columnCount; i++) {
				sb.append( dataBean.getAliasColumn( columnNames[i].toString() ) );
				
				// append comma if not on last column
				if (i != (columnCount-1) ) {
					sb.append(",");
				}
			}

			sb.append("\n");

			fos.write(sb.toString().getBytes());
			
			
			for (int iRow = 0; iRow < rowCount; iRow++) {
				
				sb = new StringBuffer();
				
				for (int iCol = 0; iCol < columnCount; iCol++) {
					
					// append data to string buffer
					if (sData[iRow][iCol] == null) {
						sb.append(" "); // prevent null pointer on empty data
					} else {
						sb.append(sData[iRow][iCol].toString());
					}
					
					// append coma if not on last column
					if (iCol != (columnCount-1) ) {
						sb.append(",");
					}
				}
				
				sb.append("\n");
				fos.write(sb.toString().getBytes());
			}
			
			fos.flush();
			fos.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generatedcatchblock
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generatedcatchblock
			e.printStackTrace();
		}
		
		// prepare to download file csv
		String mimeType = ec.getMimeType(fileName);
		System.out.println("mimetype:"+mimeType);

		FileInputStream in = null;
		byte b;

		ec.responseReset();
		ec.setResponseContentType(mimeType);
		ec.setResponseContentLength((int) f.length());
		ec.setResponseHeader("Content-Disposition", "attachment;filename=\""
				+ fileNameBase + "\"");

		try {

			in = new FileInputStream(f);
			OutputStream output = ec.getResponseOutputStream();
			while (true) {
				b = (byte) in.read();
				if (b < 0)
					break;
				output.write(b);
			}
		} catch (IOException e) {
			// TODO Auto-generatedcatchblock
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		fc.responseComplete();
		return "SUCCESS";

	}
	
	public String processFileDownloadXml() throws TransformerException {
		
		String dataset = dataBean.getSelectedDataset();

		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();

		FileOutputStream fos = null;

		String path = fc.getExternalContext().getRealPath("/temp");
		String tableName = dbaseAccessBean.getTableName();
		
		String fileNameBase = "";
		if (dataset.isEmpty()) {
			fileNameBase = tableName + ".xml";
		} else {
			fileNameBase = dataset + ".xml";
		}

		String fileName = path + "/" + "UserName" + "_" + fileNameBase;

		File f = new File(fileName);

		// get data from table
		processShowData();
		
		// check for selected dataset and columns
		if (! this.isDatasetAndColumnNamesSelected()) {
			return "fail";
		}

		Result result = dbaseAccessBean.getResult();
		Object[][] sData = result.getRowsByIndex(); // sData[indexRow][indexColumn]

		String columnNames[] = result.getColumnNames();
		int columnCount = columnNames.length;
		int rowCount = result.getRowCount();
		StringBuffer sb = new StringBuffer();
		
		// write data to file
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("dataset");
			rootElement.setAttribute("label", dataset);
			doc.appendChild(rootElement);
			
			// write data
			for (int iRow = 0; iRow < rowCount; iRow++) {
				
				// add every column data as child element
				Element dataElement = doc.createElement( "data" );
				dataElement.setAttribute("row", Integer.toString( (iRow + 1) ) );
				rootElement.appendChild( dataElement );
				
				for (int iCol = 0; iCol < columnCount; iCol++) {

					
					// append data to string buffer
					if (sData[iRow][iCol] == null) {
						
						// add every column data as child element
						Element colsElement = doc.createElement( dataBean.getAliasColumn( columnNames[iCol].toString() ).trim() );
						colsElement.appendChild( doc.createTextNode( " " ) );
						dataElement.appendChild( colsElement );
						
					
					} else {						
						
						// add every column data as child element
						Element colsElement = doc.createElement( dataBean.getAliasColumn( columnNames[iCol].toString() ).trim() );
						colsElement.appendChild( doc.createTextNode( sData[iRow][iCol].toString() ) );
						dataElement.appendChild( colsElement );						
					
					}
					
				}				
				
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult xmlResult = new StreamResult( f );
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, xmlResult);
			
			
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}		
				
		// prepare to download file csv
		String mimeType = ec.getMimeType(fileName);

		FileInputStream in = null;
		byte b;

		ec.responseReset();
		ec.setResponseContentType(mimeType);
		ec.setResponseContentLength((int) f.length());
		ec.setResponseHeader("Content-Disposition", "attachment;filename=\""
				+ fileNameBase + "\"");

		try {

			in = new FileInputStream(f);
			OutputStream output = ec.getResponseOutputStream();
			while (true) {
				b = (byte) in.read();
				if (b < 0)
					break;
				output.write(b);
			}
		} catch (IOException e) {
			// TODO Auto-generatedcatchblock
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		fc.responseComplete();
		return "SUCCESS";

	}
	
	private boolean csvDataValid(Scanner csvData) {
		// notes: we only check for data only not for column line, so row 1 will be data 1 on line 2 csv file.
		
		boolean valid = true;
		int numRow = 0;
		String errorMessage = "";
		
		csvData.next(); // first line is columns name	
		csvData.nextLine();
		numRow++;
		
		while (csvData.hasNext()) {
		
			
			// split rowCsvData to array
			String rowCsvData = csvData.nextLine();
			String[] arrRowCsvData = rowCsvData.split(",");
			
			// check for every data in a row
			try {
				for (int i = 0; i < arrRowCsvData.length; i++ ) {
					 Double.parseDouble( arrRowCsvData[i] );
				}	       
		        
		    } catch (NumberFormatException ignore) {
		        if (errorMessage.equals("")) {
		        	errorMessage += "invalid data in " + "row " + numRow;
		        } else {
		        	errorMessage += "row " + numRow;
		        }
		    	
		        errorMessage += ", ";
		        valid = false;
		    }
			
			numRow++;			
		}
		
		// show error message is not valid
		if (!valid) {
			this.messageBean.setErrorMessage(errorMessage);
			this.messageBean.setShowErrorMessage(true);
		}
		
		return valid;
	}

	private void insertToMetadataTable(String dataset, String columnNames, int row) {
		
		// prepare to save data to table csvTable
		String  tableName = "csvMetadataTable";
		
		// build query for columns names
				
		// get columns name from given table name
		List<String> columnsList = dbaseAccessBean.columnList( tableName );
		
		String[] columnsCsvTable = new String[columnsList.size()];
		// convert column list to column array
		columnsCsvTable = columnsList.toArray(columnsCsvTable);

		int i = 0;
		
		// table_name column
		StringBuffer columnsFinal = new StringBuffer("(`" + columnsCsvTable[i] + "`");
		i++;
		// c1, c2, ... columns
		while (i < columnsList.size()) {	
			
			columnsFinal.append(", `" + columnsCsvTable[i] + "`");
			i++;
		};

		// close column names in insert statement
		columnsFinal.append(")");
		String newVar ="";
		newVar = "num_row ,"+columnNames;
		
		// build query for data
		StringBuffer dataFinal = new StringBuffer("");
		dataFinal.append("('" + dataset + "', '" + newVar + "', " + row + ")");
		
		
		String sqlQuery = "INSERT INTO `"
				+ tableName + "` " + columnsFinal
				+ " VALUES " + dataFinal;
		
		dbaseAccessBean.setQueryType("INSERT");
		dbaseAccessBean.executeSQL(sqlQuery);
		
	}
	
	private boolean isLabelExist(String label) {
		boolean labelExist = false;
		String  tableName = "csvMetadataTable";
		
		String sqlQuery = "SELECT table_name from `"
				+ tableName + "` WHERE table_name='" + label + "'";
		
		dbaseAccessBean.setQueryType("SELECT");
		dbaseAccessBean.executeSQL(sqlQuery);
		
		// get resultset to check
		ResultSet rs = dbaseAccessBean.getResultSet();
		try {
			if (rs.next() ) {
				labelExist = true;
				this.messageBean.setErrorMessage( label + " filelabel already exist in database!");
				this.messageBean.setShowErrorMessage(true);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return labelExist;
		
	}	

	/**
	 * @return the messageBean
	 */
	public MessageBean getMessageBean() {
		return messageBean;
	}

	/**
	 * @param messageBean
	 *            the messageBean to set
	 */
	public void setMessageBean(MessageBean messageBean) {
		this.messageBean = messageBean;
	}

	/**
	 * @return the dbaseAccessBean
	 */
	public DbaseAccessBean getDbaseAccessBean() {
		return dbaseAccessBean;
	}

	/**
	 * @param dbaseAccessBean
	 *            the dbaseAccessBean to set
	 */
	public void setDbaseAccessBean(DbaseAccessBean dbaseAccessBean) {
		this.dbaseAccessBean = dbaseAccessBean;
	}

	/**
	 * @return the uploadedFile
	 */
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	/**
	 * @param uploadedFile
	 *            the uploadedFile to set
	 */
	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	/**
	 * @return the fileLabel
	 */
	public String getFileLabel() {
		return fileLabel;
	}

	/**
	 * @param fileLabel
	 *            the fileLabel to set
	 */
	public void setFileLabel(String fileLabel) {
		this.fileLabel = fileLabel;
	}

	/**
	 * @return the fileImport
	 */
	public boolean isFileImport() {
		return fileImport;
	}

	/**
	 * @param fileImport
	 *            the fileImport to set
	 */
	public void setFileImport(boolean fileImport) {
		this.fileImport = fileImport;
	}

	/**
	 * @return the fileImportError
	 */
	public boolean isFileImportError() {
		return fileImportError;
	}

	/**
	 * @param fileImportError
	 *            the fileImportError to set
	 */
	public void setFileImportError(boolean fileImportError) {
		this.fileImportError = fileImportError;
	}

	/**
	 * @return the numberRows
	 */
	public int getNumberRows() {
		return numberRows;
	}

	/**
	 * @param numberRows
	 *            the numberRows to set
	 */
	public void setNumberRows(int numberRows) {
		this.numberRows = numberRows;
	}

	/**
	 * @return the renderParseTable
	 */
	public boolean isRenderParseTable() {
		return renderParseTable;
	}

	/**
	 * @param renderParseTable
	 *            the renderParseTable to set
	 */
	public void setRenderParseTable(boolean renderParseTable) {
		this.renderParseTable = renderParseTable;
	}

	public DbUserInfo getDbUserInfo() {
		return dbUserInfo;
	}

	public void setDbUserInfo(DbUserInfo dbUserInfo) {
		this.dbUserInfo = dbUserInfo;
	}

	public ArrayList<DataBean> getDataBeanArrayList() {
		return dataBeanArrayList;
	}

	public void setDataBeanArrayList(ArrayList<DataBean> dataBeanArrayList) {
		this.dataBeanArrayList = dataBeanArrayList;
	}

	public DataBean getDataBean() {
		return dataBean;
	}

	public void setDataBean(DataBean dataBean) {
		this.dataBean = dataBean;
	}
	
	private boolean isDatasetAndColumnNamesSelected() {
		boolean selected = true;
		
		if (dbaseAccessBean.getResult() == null) {
			
			messageBean.setErrorMessage("There is no data to download!");
			messageBean.setShowErrorMessage(true);
			selected = false;
		}
		
		if ( dataBean.getCsvTableColumnNames().isEmpty() 
				|| dataBean.getCsvTableSelectedColumnNames().isEmpty() ) {
			
			messageBean.setErrorMessage("At least a dataset and a column must be selected!");
			messageBean.setShowErrorMessage(true);
			
			selected = false;
		}
		
		return selected;
	}

}
