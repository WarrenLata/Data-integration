import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.Date;

public class DrugBankIndex {

    private DrugBankIndex() {}

    public static void main(String[] args) {


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

        String docsPath = "/home/etudiants/jeyasoth1u/Bureau/fulldatabase.xml";
        String indexPath = "/media/jeyasoth1u/86562BD7562BC731/GMD/Index/drugBank";



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
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            } else {
                // Add new documents to an existing index:
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
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
                    String toxicity="";
                    int numTox=0;

                    String indication="";
                    int numInd=0;

                    String line1 ="";
                    String line2 ="";
                    String name="";
                    
                    while ((line=br.readLine())!=null){
                        Document doc = new Document();

                    	//System.out.println("numTox " +numTox);
                        Field pathField = new StringField("path", file.getPath(), Field.Store.YES);
                        doc.add(pathField);
                        line1 = line;
                        line2 = line;
                        
                        if (line.startsWith("  <toxicity>")) {
                        	line1 = line.substring(12,line.length());
                            numTox = 1;
                        }
                        if (numTox==1){
                        	if (line.indexOf("/")>0){
                        		if(line.contains("</toxicity>")){
                        			numTox=2;
                        			line1 = line1.substring(0,line1.indexOf("<"));
                        		}
                        	}
                    		toxicity = toxicity+line1;
                        }
                        
                        if (line.startsWith("  <name>")) {
                        	name = line.substring(8,line.length()-7);
                        }
                        
                        

                    
                        
                        if (line.startsWith("  <indication>")) {
                        	line2 = line.substring(14,line.length());
                            numInd = 1;
                        }
                        if (numInd==1){
                        		if(line.contains("</indication>")){
                        			numInd=2;
                        			line2 = line2.substring(0,line2.indexOf("<"));
                        		}
                    		indication = indication+line2;
                        }
                        

                        if (numTox==2) {
                            doc.add(new TextField("toxicity", toxicity, Field.Store.YES));
                        	//System.out.println("toxicity : " +toxicity);
                            numTox = 0;
                            toxicity = "";
                        }else if (numInd==2) {
                            doc.add(new TextField("indication", indication, Field.Store.YES));
                        	System.out.println("indication : "+indication);
                        	doc.add(new TextField("name", name, Field.Store.YES));
                        	System.out.println("name : "+name);
                            numInd =0;
                            indication="";

                        }

                        if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
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
