package projet_gmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.queryparser.classic.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.analysis.query.*;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class omintxtSearch {
		
	private omintxtSearch(){}
	 
		  public static void main(String[] args) throws Exception {
			  String usage ="Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/java/4_0/demo.html for details.";
			  if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
			  	System.out.println(usage);
			  	System.exit(0);
			  }
		  
			  String index = "/home/etudiants/lata1u/GMD/projet_gmd/drug_bank3";
			  String field = "CUI";
			  String queries = null;
			  int repeat = 0;
			  boolean raw = false;
			  String queryString = null;
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
	    System.out.println(query.toString());
	    System.out.println("Searching for: " + query.toString(field));
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
	    reader.close();
	    }
		  
		  
		public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, int hitsPerPage, boolean raw, boolean interactive) throws IOException {
 
				   // Collect enough docs to show 5 pages
			TopDocs results = searcher.search(query, hitsPerPage);
			ScoreDoc[] hits = results.scoreDocs;
			
			int numTotalHits = results.totalHits;
			System.out.println(numTotalHits + " total matching documents");
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
				  String path = doc.get("path");
				  if (path != null) {
				  	System.out.println((i+1) + ". " + path);
				  	String title = doc.get("title");
				  	if (title != null) {
				  		System.out.println("   Title: " + doc.get("title"));
				  	}
				  } else {
				  		System.out.println((i+1) + ". " + "No path for this document");
				  }
				}
				if (!interactive || end == 0) {
					break;
				}
				if (numTotalHits >= end) {
					boolean quit = false;
					while (true) {
				  		System.out.print("Press ");
				  		if (start - hitsPerPage >= 0) {
				  			System.out.print("(p)revious page, ");  
				  		}
				  		if (start + hitsPerPage < numTotalHits) {
				  			System.out.print("(n)ext page, ");
				  		}
				  		System.out.println("(q)uit or enter number to jump to a page.");
					  	String line = in.readLine();
				  		if (line.length() == 0 || line.charAt(0)=='q') {
				  			quit = true;
				  			break;
				  		}
				  		if (line.charAt(0) == 'p') {
				  			start = Math.max(0, start - hitsPerPage);
				  			break;
				  		} else if (line.charAt(0) == 'n') {
				  			if (start + hitsPerPage < numTotalHits) {
				  				start+=hitsPerPage;
				  			}
				  			break;
				  		} else {
				  			int page = Integer.parseInt(line);
				  			if ((page - 1) * hitsPerPage < numTotalHits) {
				  				start = (page - 1) * hitsPerPage;
				  	             break;
				  			} else {
				  				System.out.println("No such page");
				  			}
				  		}
				  	}
					if (quit) break;
				 	end = Math.min(numTotalHits, start + hitsPerPage);
				}
			}
		}

}
