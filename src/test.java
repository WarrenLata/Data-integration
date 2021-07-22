

import java.sql.*;
import java.util.HashMap;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class test {
	static String HOST = "neptune.telecomnancy.univ-lorraine.fr";
	static String DATABASE = "gmd";
	static String LOGIN = "gmd-read";
	static String PWD = "esial";
	static String DB_SERVER = "jdbc:mysql://neptune.telecomnancy.univ-lorraine.fr:3306/";
	static String DRIVER = "com.mysql.jdbc.Driver";
	public static void main(String[] args) throws SQLException {
		try {
			Class.forName(DRIVER);
			
			Connection con = DriverManager.getConnection(DB_SERVER+DATABASE, LOGIN, PWD);

			String myQuery = "SELECT cui, stitch_compound_id1, stitch_compound_id2, cui_of_meddra_term FROM meddra_all_se;";
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery(myQuery);
			HashMap<Integer, String> nMap = new HashMap<Integer, String>();
			while (res.next()) {
				ResultSetMetaData rsmd = res.getMetaData();
				int nbOfColumns = rsmd.getColumnCount();
				HashMap<String, String> columnMap = new HashMap<String, String>();
				for(int c= 1; c<nbOfColumns; c++){
					columnMap.put(rsmd.getColumnLabel(c),rsmd.getColumnType(c)+"" );
				}
				while (res.next()){
					for(String columnName : columnMap.keySet()){
						if(columnMap.get(columnName).equals(java.sql.Types.INTEGER+"")){
							System.out.print(columnName+"="+res.getInt(columnName)+" ");
						}
						if(columnMap.get(columnName).equals(java.sql.Types.VARCHAR+"")){
							System.out.print(columnName+"="+res.getString(columnName)+" ");
						}
					}
					
					//nMap.put(res.getInt("meddra_id"),res.getString("cui"));
					System.out.println("\n");
				}
				System.out.println("");
			}
			res.close();
			st.close();
			con.close();
		}
		catch (ClassNotFoundException e){
			System.out.println("Could not load JDBC driver");

		}
		catch(SQLException e){
			System.out.println("SQLException information");

		}
	}

}

