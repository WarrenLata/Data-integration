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

public class OmimOntoIndex{
	
    private OmimOntoIndex() {}
    
    static String indexPath = "/media/jeyasoth1u/86562BD7562BC731/GMD/Index/OmimOnto_Index";
    static File f = new File(indexPath);
	
	public static void main(String[] args) {
        String usage = "java org.apache.lucene.demo.IndexFiles"
                       + " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
                         + "This indexes the documents in DOCS_PATH, creating a Lucene index"
                        + "in INDEX_PATH that can be searched with SearchFiles";

        boolean create = true;
        /*for(int i=0;i<args.length;i++) {
            if ("-index".equals(args[i])) {
                indexPath = args[i+1];
                i++;
            } else if ("-docs".equals(args[i])) {
                docsPath = args[i+1];
                i++;
            } else if ("-update".equals(args[i])) {
                create = false;
            }
        }*/

        String docsPath = "/home/depot/2A/gmd/projet_2017-18/omim/omim_onto.csv";
        
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

         Directory dir = FSDirectory.open(f);
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
         indexDocs(writer,docDir);

         writer.close();

         Date end = new Date();
         System.out.println(end.getTime() - start.getTime() + " total milliseconds");

         } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() +
                        "\n with message: " + e.getMessage());
         }
    }

    static void indexDocs(IndexWriter writer, File file)
        throws IOException {
        // do not try to index files that cannot be read
        if (file.canRead()) {
            if (file.isDirectory()) {
                String[] files = file.list();
                // an IO error could occur
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        indexDocs(writer, new File(file, files[i]));
                    }
                }
            } else {

               
                try {

                    FileInputStream ips = new FileInputStream(file);
                    InputStreamReader ipsr = new InputStreamReader(ips);
                    BufferedReader br = new BufferedReader(ipsr);
                    String line;
                    int ligne=0;
                    while ((line=br.readLine())!=null){
                    	ligne = ligne + 1;
                        Document doc = new Document();

                    	String name ="";
                       String fields[] = line.split(",");
                        Field pathField = new StringField("path", file.getPath(), Field.Store.YES);
                        doc.add(pathField);
                       // doc.add(new TextField("Class ID", fields[0],Field.Store.YES));

                        int num =0;
                        int numero = 1;
                        if (fields[1].substring(0,1).equals("\"")){
                            String caractere = fields[1].substring(1,2);
                            numero = 2;
                            while (!caractere.equals("\"")){
                                if (numero == fields[num+1].length()) {
                                    num = num+1;
                                    numero = 0;
                                }
                                if (fields[num+1].length() !=0){
                                caractere = fields[num+1].substring(numero,numero+1);
                                numero = numero+1;
                                }
                                
                            }
                        }
                        
                        //System.out.println("f :" + fields[num] + "numero :"+num);
                        //System.out.println("CUI :" + fields[num+2] + "numero"+ num);

                        if (fields[num+2].length()>0){
                        	if (fields[num+2].substring(0,1).equals("\"")){
                        		 String caractere = fields[num+2].substring(1,2);
                            	numero = 2;
                            	while (!caractere.equals("\"")){
                                	if (numero == fields[num+2].length()) {
                                    	num = num+1;
                                    	numero = 0;
                                	}
                                	caractere = fields[num+2].substring(numero,numero+1);
                                	numero = numero+1;
                            	}
                        	}
                        }
                        
                        String identifiant[] = fields[0].split("/");
                        
                        
                      
                        if ((5+num)<fields.length){
                        	doc.add(new TextField("id", identifiant[identifiant.length-1],Field.Store.YES));
                        	doc.add(new TextField("Name", fields[1],Field.Store.YES));
                        	doc.add(new TextField("CUI", fields[5+num],Field.Store.YES));
                        	System.out.println("Ligne :"+ ligne);
                        	System.out.println("Name : " + fields[1] + " CUI : "+ fields[5+num] + "id" + identifiant[identifiant.length-1]);
                        }
                        //System.out.println("CUI :" + fields[5+num]);

                        if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                            //System.out.println("add");
                            writer.addDocument(doc);
                        } else {
                            System.out.println("updating " + file);
                            writer.updateDocument(new Term("/home/etudiants/jeyasoth1u/NAS_UL_ETUDIANTS/2018-2019/GMD/Projet/projet_gmd/Index/OmimOnto_Index", file.getPath()), doc);

                        }
                    }
                    br.close();

                }finally{
                }
            
            }
        }
    }
	
}