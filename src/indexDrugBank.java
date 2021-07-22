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

public class indexDrugBank {

    private indexDrugBank() {
    }

    public static void main(String[] args) {
        String usage = "java org.apache.lucene.demo.IndexFiles"
                + " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
                + "This indexes the documents in DOCS_PATH, creating a Lucene index"
                + "in INDEX_PATH that can be searched with SearchFiles";
        String indexPath = "index"; /*à changer*/
        String docsPath = null; /*à changer*/
        boolean create = true;
        /*for (int i = 0; i < args.length; i++) {
            if ("-index".equals(args[i])) {
                indexPath = args[i + 1];
                i++;
            } else if ("-docs".equals(args[i])) {
                docsPath = args[i + 1];
                i++;
            } else if ("-update".equals(args[i])) {
                create = false;
            }
        }
        */

        docsPath = "/home/etudiants/jeyasoth1u/Bureau/fulldatabase.xml";
        indexPath = "/media/jeyasoth1u/86562BD7562BC731/GMD/Index/drugBank";

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
                iwc.setOpenMode(OpenMode.CREATE);
            } else {
                iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
            }

            IndexWriter writer = new IndexWriter(dir, iwc);
            indexDocs(writer, docDir);

            writer.close();

            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");

        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
        }
    }

    static void indexDocs(IndexWriter writer, File file) throws IOException {
        if (file.canRead()) {
            if (file.isDirectory()) {
                String[] files = file.list();
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

                    while ((line = br.readLine()) != null) {
                        Document doc = new Document();

                        String fields[] = line.split(">");
                        Field pathField = new StringField("path", file.getPath(), Field.Store.YES);
                        doc.add(pathField);
                        doc.add(new LongField("modified", file.lastModified(), Field.Store.NO));

                        if (fields[0].equals("indication")) {
                            doc.add(new TextField("Indication", fields[1], Field.Store.YES));
                        }
                        else if (fields[0].equals("toxicity")) {
                            doc.add(new TextField("Toxicity", fields[1], Field.Store.YES));
                        }
                        
                        if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
    						System.out.println("adding " + file);
    						 writer.addDocument(doc);
    					}else{
    						System.out.println("updating " + file);
    						 writer.updateDocument(new Term("path", file.getPath()), doc);
    					}
                    }
                    br.close();
                } finally {
                }
            }
        }
    }
}