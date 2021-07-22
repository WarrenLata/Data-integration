import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class DrugBankSearch {
	
public static  ArrayList<String> med_name = new ArrayList<String>();

	
	public DrugBankSearch(){
		med_name = new ArrayList<String>();
	}
	
	 static String indexPath = "/media/jeyasoth1u/86562BD7562BC731/GMD/Index/drugBank";
	    static File f = new File(indexPath);

	    public void fonctionSearchATC(String field,String queryString) throws Exception {
				  

				  //String queries = "/home/etudiants/jeyasoth1u/NAS_UL_ETUDIANTS/2018-2019/GMD/Projet/projet_gmd/Index/OmimOnto_Index/segments.gen";
				  String queries = null;
				  int repeat = 0;
				  boolean raw = false;
				  int hitsPerPage = 100;

			IndexReader reader = DirectoryReader.open(FSDirectory.open(f));
		    IndexSearcher searcher = new IndexSearcher(reader);
		    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

		    BufferedReader in = null;
		    if (queries != null) {
		    	in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
		    } else {
		    	in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		    }


		    QueryParser parser = new QueryParser(Version.LUCENE_40, field, analyzer);
		    while (true) {
		    if (queries == null && queryString == null) {                        // prompt the user
		    	System.out.println("Enter query: ");
		    }

		    String line = queryString != null ? queryString : in.readLine();
		    if (line == null || line.length() == -1) {
		    	break;
		    }
		    line = line.trim();
		    if (line.length() == 0) {
		    	break;
		    }
		    Query query = parser.parse(line);
		    //System.out.println("Searching for: " + query.toString(field));
		    if (repeat > 0) {                           // repeat & time as benchmark
		    	Date start = new Date();
		    	for (int i = 0; i < repeat; i++) {
		    	     TopDocs results = searcher.search(query, 100);
		 			 //ScoreDoc[] hits = results.scoreDocs;

					int numTotalHits = results.totalHits;
					System.out.println(numTotalHits + " total matching documents");
		    	}
		    	Date end = new Date();
		    	System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
		    }
		    	doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);
		    	if (queryString != null) {
		    	    break;
		    	}
		    }
		    reader.close();
		    }


			public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, int hitsPerPage, boolean raw, boolean interactive) throws IOException {

					   // Collect enough docs to show 5 pages
				TopDocs results = searcher.search(query, 5 * hitsPerPage);
				ScoreDoc[] hits = results.scoreDocs;

				int numTotalHits = results.totalHits;
				//System.out.println(numTotalHits + " total matching documents");
				int start = 0;
				int end = Math.min(numTotalHits, hitsPerPage);
				while (true) {
					  if (end > hits.length) {
					  	String line = in.readLine();
					  	if (line.length() == 0 || line.charAt(0) == 'n') {
					  		break;
					  	}
						hits = searcher.search(query, numTotalHits).scoreDocs;
					  }
					  end = Math.min(hits.length, start + hitsPerPage);
					  for (int i = start; i < end; i++) {
					  	 if (raw) {                              // output raw format
					  		 System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
					  		 continue;
					 	}
					  Document doc = searcher.doc(hits[i].doc);
					  String path = doc.get("indication");
					  if (path != null) {
					  	String title = doc.get("name");
					  	if (title != null) {
					  		if (!med_name.contains(doc.get("name"))){
					  			med_name.add(doc.get("name"));
					  		}
					  	}
					  } else {
					  		System.out.println((i+1) + ". " + "No path for this document");
					  }
					}
					if (!interactive || end == 0) {
						break;
					}
			
				}
			}
	

}
