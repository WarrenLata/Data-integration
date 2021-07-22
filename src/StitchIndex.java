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

public class StitchIndex {

    private StitchIndex() {}

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

       // String docsPath = "/home/depot/2A/gmd/projet_2017-18/stitch/chemical.sources.v5.0.tsv";
        String docsPath = "/home/depot/2A/gmd/projet_2017-18/stitch/chemical.sources.v5.0.tsv";
        String indexPath = "/media/jeyasoth1u/86562BD7562BC731/GMD/Index/Stitch_Index";

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
        // do not try to index files that cann ot be read
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
                    
                    int numeroLigne = 0;
                    while ((line=br.readLine())!=null) {
                    	Document doc = new Document();
                        String fields[] = line.split("\t");
                        if (numeroLigne > 9 ) {
                          while(fields[2].equals("ATC")){
                            Field pathField = new StringField("path", file.getPath(), Field.Store.YES);
                            doc.add(pathField);
                            doc.add(new TextField("stitch1", fields[0], Field.Store.YES));
                            doc.add(new TextField("stitch2", fields[1], Field.Store.YES));
                            doc.add(new TextField("type source", fields[2], Field.Store.YES));
                            doc.add(new TextField("code source", fields[3], Field.Store.YES));
                            System.out.println("stitch1 : "+fields[0] +"stitch2 : "+fields[1]+"type source : "+fields[2]+"code : "+fields[3]);

                            if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
                               // System.out.println("add");
                                writer.addDocument(doc);
                                //for (int i=0 ; i<fields.length()
                                fields[0] = "";
                                fields[1] = "";
                                fields[2] = "";
                                fields[3] = "";
                            } else {
                                System.out.println("updating " + file);
                                writer.updateDocument(new Term("/home/etudiants/jeyasoth1u/NAS_UL_ETUDIANTS/2018-2019/GMD/Projet/projet_gmd/Index/Stitch_Index", file.getPath()), doc);
                                fields[0] = "";
                                fields[1] = "";
                                fields[2] = "";
                                fields[3] = "";
                            }
                          }
                        }
                        numeroLigne = numeroLigne + 1;
                        for (int i=0; i<fields.length ; i++){
                        	fields[i] ="";
                        }
                    }
                    br.close();

                }finally{
                }

            }
        }
    }

}
