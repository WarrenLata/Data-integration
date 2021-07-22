
import java.sql.*;
import java.util.HashMap;
/*import org.apache.lucene.analysis.Analyzer;
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
import org.apache.lucene.util.Version;*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;



public class hboAnnotation {
	
	 public static hpoboSearch myhpo;
	 public static ArrayList<String> disease_idd_OMIM;
	 public static ArrayList<String> disease_idd_ORPHA;

	 public ArrayList<String> disease_id_OMIM;
	 public ArrayList<String> disease_id_ORPHA;

	 
	 public hboAnnotation(hpoboSearch myhpo){
		 this.myhpo= myhpo ;
		 this.disease_idd_OMIM= new ArrayList<String>();
		 this.disease_idd_ORPHA= new ArrayList<String>();
		 this.disease_id_OMIM= new ArrayList<String>();
		 this.disease_id_ORPHA= new ArrayList<String>();
	 }
	 
	//private String DBPath = "Chemin aux base de donnée SQLite";
    //private Connection connection = null;
    //private Statement statement = null;
 
    
	 
	 
	 
	 
   // public static void main(String[] args) throws SQLException{
	 
	 public void fonctionHboAnnotation() throws SQLException{
        try {
        	
        	for(int i=0;i< myhpo.listeidd.size();i++){
        	String DBPath = "/home/depot/2A/gmd/projet_2017-18/hpo/hpo_annotations.sqlite";
            Connection connection = null;
             Statement statement = null;

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
            statement = connection.createStatement();
           //System.out.println("Connexion a " + DBPath + " avec succès");
           System.out.println("Signe : " + myhpo.listeidd.get(i));

			String myQuery = "SELECT  disease_db, disease_id, col_11 FROM phenotype_annotation  WHERE sign_id = "+"'"+myhpo.listeidd.get(i)+"'"+";";		
           //String myQuery = "SELECT  * FROM phenotype_annotation  WHERE sign_id = "+"'"+myhpo.listeidd.get(i)+"'"+";";
			ResultSet res = statement.executeQuery(myQuery);
			HashMap<String, String> nMap = new HashMap<String, String>();
			while (res.next()) {
				ResultSetMetaData rsmd = res.getMetaData();
				int nbOfColumns = rsmd.getColumnCount();
				HashMap<String, String> columnMap = new HashMap<String, String>();
				for(int c= 1; c<nbOfColumns; c++){
					columnMap.put(rsmd.getColumnLabel(c),rsmd.getColumnType(c)+"" );
					//System.out.println("colonne : "+rsmd.getColumnLabel(c));
				}
				while (res.next()){
					for(String columnName : columnMap.keySet()){
						if(columnMap.get(columnName).equals(java.sql.Types.INTEGER+"")){
							
							System.out.print(columnName+"="+res.getInt(columnName)+" ,");
						}
						if(columnMap.get(columnName).equals(java.sql.Types.VARCHAR+"")){
							if(columnName.equals("disease_id")){
							if (res.getString("disease_db").equals("OMIM")){
								this.disease_id_OMIM.add(res.getString(columnName));
							}else {
								this.disease_id_ORPHA.add(res.getString(columnName));
							}
							}
							System.out.print(columnName+"="+res.getString(columnName)+", ");
						}
					}

					System.out.println("");
				}
				//System.out.println("\n");
			}
			System.out.println("_________________");
			res.close();
			statement.close();
			connection.close();
        	}
        	
            
        } catch (ClassNotFoundException notFoundException) {
            notFoundException.printStackTrace();
            System.out.println("Erreur de connexion");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println("Erreur de connexion");
        }
        this.affectation();
    }
	 
	 
	 public void affectation(){
		 disease_idd_OMIM=disease_id_OMIM;
		 disease_idd_ORPHA=disease_id_ORPHA;
	  }
	  
 
  
}
	
    
    
	


