

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.index.IndexWriterConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.util.Date;

/** Index all text files under a directory. */
public class hpoboIndex {

	private hpoboIndex() {
	}

	static final File INDEX_DIR = new File("/media/jeyasoth1u/86562BD7562BC731/GMD/Index/HpOboIndex");

	/** Index all lines of a text file (path of the file is args[0]). */
	public static void main(String[] args) {
		// args[0]="/home/etudiants/lata1u/GMD/projet_gmd/omin.txt";

		boolean create = true;

		if (INDEX_DIR.exists()) {
			System.out.println("Cannot save index to '" + INDEX_DIR + "' directory, please delete it first");
			System.exit(1);
		}

		final File file = new File("/home/depot/2A/gmd/projet_2017-18/hpo/hp.obo");
		if (!file.exists() || !file.canRead()) {
			System.out.println(
					"File '" + file.getAbsolutePath() + "' does not exist or is not readable, please check the path");
			System.exit(1);
		}

		Date start = new Date();
		try {
			Directory directory = FSDirectory.open(INDEX_DIR);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);

			if (create) {
				config.setOpenMode(OpenMode.CREATE);
			} else {
				config.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}

			IndexWriter writer = new IndexWriter(directory, config);


			System.out.println("Indexing to directory '" + INDEX_DIR + "'...");
			indexDoc(writer, file);
			writer.close();

			Date end = new Date();
			System.out.println(end.getTime() - start.getTime() + " total milliseconds");

		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}
		
	}

	static void indexDoc(IndexWriter writer, File file) throws IOException {
	
	int eltCount = 0;
    if (file.canRead() && !file.isDirectory()) {
	  // each line of the file is a new document
	  try{
		InputStream 	  ips  = new FileInputStream(file); 
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader    br   = new BufferedReader(ipsr);
		String line;
		//initialization
		String id               = "";
		String name      = "";
		//String description=""
		String synonyms         = "";
		 
		//String effect ="";
		while ((line=br.readLine())!=null){
		
			// new drug 
			if(line.startsWith("[Term]")){
				line=br.readLine();
				int point= line.indexOf(':');
				
				
				id+=line.substring(point+2, line.length());
				
			}
			if(line.startsWith("synonym")){

				int fin=line.substring(10).indexOf('"');
				for(int i=10;i<line.length();i++){
					if(line.charAt(i)=='"'){
						fin=i;
					}
				}
				 synonyms=line.substring(10,fin);
				
				 
	
			}
			
			if(line.startsWith("name")){

				//name = "\""+line.substring(6,line.length())+"\"";
				name = line.substring(6,line.length());

		
			}
			
					
	
			
			if(line.startsWith("xref")){
			

			    Document doc = new Document();
			    doc.add(new StoredField("id", id));
			    doc.add(new TextField("Synonyms",  synonyms, 		Field.Store.YES)); 
			    doc.add(new TextField("Name",  name, 		Field.Store.YES)); 

			    System.out.println("Id : "+ id + "Name : " + name + "Synonyms :"+ synonyms);

			    if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
					//System.out.println("adding " + file);
					 writer.addDocument(doc);
				}else{
					System.out.println("updating " + file);
					 writer.updateDocument(new Term("path", file.getPath()), doc);
				}
				
				eltCount++;
				//clean values
				id               = "";
				name      = "";
				synonyms         = "";
				
			}
			;
		}
		
	  	br.close(); 
	  }catch (Exception e){
		System.out.println(e.toString());
	  }
    }
  }
}
