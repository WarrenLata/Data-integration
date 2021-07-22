import java.sql.*;
import java.util.HashMap;

public class SQLConnection {
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

			String myQuery = "SELECT * " +
					"FROM meddra_all_se  ;";
			Statement st = con.createStatement();
			ResultSet res = st.executeQuery(myQuery);
			HashMap<String, String> hash = new HashMap<String,String>();

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
							//hash.put(columnName,res.getInt(columnName));
							System.out.print(columnName+"="+res.getInt(columnName)+" ");
						}
						if(columnMap.get(columnName).equals(java.sql.Types.VARCHAR+"")){
							//hash.put(columnName,Integer.toString(res.getString(columnName)));
							System.out.print(columnName+"="+res.getString(columnName)+" ");
						}
					}
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
