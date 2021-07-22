import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexATC {

	   private IndexATC() {}
		
	
	     	
	     	public static void main(String[] args) {
	             String usage = "java org.apache.lucene.demo.IndexFiles"
	                            + " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
	                              + "This indexes the documents in DOCS_PATH, creating a Lucene index"
	                             + "in INDEX_PATH that can be searched with SearchFiles";
	             String indexPath = "index";
	             String docsPath = null;
	             boolean create = true;
	             
	             
	             docsPath = "/home/depot/2A/gmd/projet_2017-18/atc/br08303.keg";
	             indexPath = "/media/jeyasoth1u/86562BD7562BC731/GMD/Index/ATC_Index1";
	             
	             if (docsPath == null) {
	                 System.err.println("Usage: " + usage);
	                 System.exit(1);
	             }

	             final File docDir = new File(docsPath);
	             if (!docDir.exists() || !docDir.canRead()) {
	                 System.out.println("Document directory '" + docDir.getAbsolutePath() + "' does not exist or is not readable, please check the path");
	                 System.exit(1);
	             }

	             Date start = new Date();
	              try {
	              System.out.println("Indexing to directory '" + indexPath + "'...");

	              Directory dir = FSDirectory.open(new File(indexPath));
	              Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
	              IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);

	              if (create) {
	               // Create a new index in the directory, removing any
	               // previously indexed documents:
	                  iwc.setOpenMode(OpenMode.CREATE);
	              } else {
	                // Add new documents to an existing index:
	                  iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
	              }

	              IndexWriter writer = new IndexWriter(dir, iwc);
	              indexDocs_ATC(writer,docDir);

	              writer.close();

	              Date end = new Date();
	              System.out.println(end.getTime() - start.getTime() + " total milliseconds");

	              } catch (IOException e) {
	                 System.out.println(" caught a " + e.getClass() +
	                             "\n with message: " + e.getMessage());
	              }
	         }

	         public static void indexDocs_ATC(IndexWriter writer, File file) throws IOException {
	             // do not try to index files that cannot be read
         		int eltCount = 0;

	             if (file.canRead()) {
	                 if (file.isDirectory()) {
	                     String[] files = file.list();
	                     // an IO error could occur
	                     if (files != null) {
	                         for (int i = 0; i < files.length; i++) {
	                             indexDocs_ATC(writer, new File(file, files[i]));
	                         }
	                     }
	                 } else {
	                    
	                	 try{
	                			InputStream 	  ips  = new FileInputStream(file); 
	                			InputStreamReader ipsr = new InputStreamReader(ips);
	                			BufferedReader    br   = new BufferedReader(ipsr);
	                			String line;
	                			//initialization
	                			String codeATC   = "";
	                			String name      = "";
	                			String category = "";
	                			int i = 0;
            				    

	                			while ((line=br.readLine())!=null){
	                				Document doc = new Document();
	                				
	                				if(line.startsWith("E")){
	                					String[] fields = line.split("        ");
	                					name = fields[1].substring(7,fields[1].length());
	                					category = "E";
	                					codeATC = fields[1].substring(0,7);
	                				
	                				
	              	                				    //add 3 fields to it
	                				    doc.add(new TextField("categorie", category,Field.Store.YES));
	                				    doc.add(new TextField("codeATC", codeATC,Field.Store.YES));
	                				    doc.add(new TextField("name", name,Field.Store.YES));
	                					System.out.println("codeATC : " + codeATC + ", categorie : " + category + ", Name : " + name);
	                				}
	                				    //System.out.println("codeATC 136" + codeATC);
	                				    
	                				    if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
	                						//System.out.println("adding " + file);
	                						 writer.addDocument(doc);
	                						 category = "";
	                						 codeATC ="";
	                						 name ="";
	                					}else{
	                						System.out.println("updating " + file);
	                						 writer.updateDocument(new Term("path", file.getPath()), doc);
	                						 category = "";
	                						 codeATC ="";
	                						 name ="";
	                					}
	                				    category = "";
               						 codeATC ="";
               						 name ="";
	                					
	                					eltCount++;
	                				}		    
	                				
	                			
	                			
	                		  	br.close(); 
	                		  }catch (Exception e){
	                			System.out.println(e.toString());
	                		  }
	                	    }
	                	  }
	                 
	                 }

	         
		
	         	
	
}
