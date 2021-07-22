
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
//import org.apache.lucene.queryparser.classic.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.analysis.query.*;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.PhraseQuery;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class hpoboSearch {
	
	public static  ArrayList<String> listeid = new ArrayList<String>();;
	public  ArrayList<String> listeidd = new ArrayList<String>();;
		
	public hpoboSearch(){
		listeid= new ArrayList<String>();
		listeidd= new ArrayList<String>();
	}
	 
		  //public static void main(String[] args) throws Exception {
	
		public void fonctionSearchHpobo(String field, String queryString) throws Exception{
			
		
		  
			  String index = "/media/jeyasoth1u/86562BD7562BC731/GMD/Index/HpOboIndex";
			  //String field = "Name";
			  String queries = null;
			  int repeat = 0;
			  boolean raw = false;
			  //String queryString = null;
			  int hitsPerPage = 10;
		  
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
		
	    IndexSearcher searcher = new IndexSearcher(reader);
	    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
	    
	    BufferedReader in = null;
	    if (queries != null) {
	    	in = new BufferedReader(new InputStreamReader(new FileInputStream(queries), "UTF-8"));
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
    	    
	    if (repeat > 0) {                           // repeat & time as benchmark
	    	Date start = new Date();
	    	for (int i = 0; i < repeat; i++) {
	    	     searcher.search(query, null, 100);
	    	}
	    	Date end = new Date();
	    	System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
	    }
	       int a=searcher.search(query, null, 1).totalHits;
	       System.out.println(a);
	    	doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);
	    	if (queryString != null) {
	    	    break;
	    	}
	    	
	    	
	    }
	    this.affectation();
	    reader.close();
	    }
		 
		  public void affectation(){
			  listeidd=listeid;
		  }
		  
		public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, int hitsPerPage, boolean raw, boolean interactive) throws IOException {
 
				   // Collect enough docs to show 5 pages
			TopDocs results = searcher.search(query, hitsPerPage);
			ScoreDoc[] hits = results.scoreDocs;
			
			int numTotalHits = results.totalHits;
			int start = 0;
			int end = Math.min(numTotalHits, hitsPerPage);
			while (true) {
				  if (end > hits.length) {
				  	System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
				  	System.out.println("Collect more (y/n) ?");
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
				  String path = doc.get("Name");
				  if (path != null) {
				  	System.out.println((i+1) + ". " + path);
				  	String title = doc.get("Name");
				  	if (title != null) {
				  		listeid.add(doc.get("id"));
				  		System.out.println("   Title: " + listeid.get(listeid.size()-1));
				  		//System.out.println("   Title: " + doc.get("id"));
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
