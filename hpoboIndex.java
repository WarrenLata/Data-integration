
package projet_gmd;

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

	static final File INDEX_DIR = new File("drug_bank3");

	/** Index all lines of a text file (path of the file is args[0]). */
	public static void main(String[] args) {
		// args[0]="/home/etudiants/lata1u/GMD/projet_gmd/omin.txt";

		boolean create = true;

		if (INDEX_DIR.exists()) {
			System.out.println("Cannot save index to '" + INDEX_DIR + "' directory, please delete it first");
			System.exit(1);
		}

		final File file = new File("/home/etudiants/lata1u/GMD/projet_gmd/hp.obo");
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
				// Create a new index in the directory, removing any
				config.setOpenMode(OpenMode.CREATE);
			} else {
				config.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}

			IndexWriter writer = new IndexWriter(directory, config);

			// IndexWriter writer = new IndexWriter(FSDirectory.open(INDEX_DIR),
			// new IndexWriterConfig(Version.LUCENE_40, new
			// StandardAnalyzer(Version.LUCENE_40)) );
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
		String genericName      = "";
		//String description=""
		String synonyms         = "";
		String brandNames       = "";
		String description      = "";
		String indication       = "";
		String pharmacology     = "";
		String drugInteractions = "";
		 
		//String effect ="";
		while ((line=br.readLine())!=null){
		
			// new drug 
			if(line.startsWith("[Term]")){
				line=br.readLine();
				//line.split(" ");
				int point= line.indexOf(':');
				System.out.println("voici "+line.substring(point+1, line.length()));
				
				
				id+=line.substring(point+2, line.length()-1)+" ";
				
			}
			if(line.startsWith("synonym")){
				 //description+=line+"";
				//int point= line.indexOf('"');
				System.out.println(line.substring(10));
				int fin=line.substring(10).indexOf('"');
				//int fin= line.substring(10).indexOf('"');
				for(int i=10;i<line.length();i++){
					if(line.charAt(i)=='"'){
						fin=i;
					}
				}
				 description=line.substring(10,fin);
				
				 
				 System.out.println(line.substring(10,fin));
						 //System.out.println(" ici2 "+line.substring(10).toString());;
				 //System.out.println(" ici "+ line.substring(10, fin-1));
				//int crochet= line.indexOf("[");
			
				//description+=line.substring(7,crochet-2);
				// System.out.println(description);
				//line.split("");
					/*while((line=br.readLine())!=null){
						if(line.equals("")){
							break;
						}else{
							description+=line+"";
						}
					}ù*/
								
			}
			if(line.equals("CLINICAL FEATURES")){
				line.split("");
				while((line=br.readLine())!=null){
					if(line.equals("")){
						break;
					}else{
						synonyms+=line+" ";
					}
				}				
			}
			if(line.equals("*FIELD* CS")){
				//.split("");
				while(!line.equals("*FIELD* CD")){
					line.split("");
					if(line.equals("*FIELD* CD")){
						break;
					}else{
						pharmacology+=line+" ";
					}
				}				
			}
			
					
	
			
			if(line.startsWith("xref")){
			
				//write the index
				// make a new, empty document
			    Document doc = new Document();
			    //add 3 fields to it
			    doc.add(new StoredField("id", id)); // stored not indexed
			    //System.out.println(new StoredField("id", id));
			    //doc.add(new TextField("Generic_Name", genericName.toLowerCase(), 		Field.Store.YES)); // indexed and stored
			    //doc.add(new TextField("Synonyms",     synonyms, 		Field.Store.YES)); // indexed
			    //doc.add(new TextField("Brand_Names",  brandNames, 		Field.Store.YES)); // indexed
			    doc.add(new TextField("Description",  description, 		Field.Store.NO)); // indexed
			    System.out.println(new TextField("Description", description,Field.Store.NO));
			    //doc.add(new TextField("Indication",   indication, 		Field.Store.YES)); // indexed
			    //doc.add(new TextField("Pharmacology", pharmacology, 	Field.Store.YES)); // indexed
			    //doc.add(new TextField("Drug_Interactions",drugInteractions,Field.Store.NO)); // indexed
			    //System.out.println(id+" "+genericName);
			    if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
					System.out.println("adding " + file);
					 writer.addDocument(doc);
				}else{
					System.out.println("updating " + file);
					 writer.updateDocument(new Term("path", file.getPath()), doc);
				}
				
				eltCount++;
				//clean values
				id               = "";
				genericName      = "";
				synonyms         = "";
				brandNames       = "";
				description      = "";
				indication       = "";
				pharmacology     = "";
				drugInteractions = "";
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
