import java.util.ArrayList;

public class LieAinterface {
	
	 ArrayList<ArrayList<String>> liste= new ArrayList<ArrayList<String>>();
	public static void parse(String parse1,ArrayList<ArrayList<String>> liste){
		int firstOu=0;
		int posEt=0;
	     while(parse1.charAt(firstOu)=='o' && parse1.charAt(firstOu+1)=='u' && firstOu<parse1.length()){
	    	 firstOu++;
	     }
	     while(parse1.charAt(firstOu)=='e' && parse1.charAt(firstOu+1)=='t' && posEt<parse1.length() ){
	    	 posEt++;
	     }
	     if(firstOu==0){
	    	 fonctionEt(parse1,liste);
	     }
	     else{
	    	fonctionEt(parse1.substring(0,firstOu+1),liste);
	    	parse(parse1.substring(firstOu+3),liste);
	     }
	}
	
	public static void fonctionEt(String text,ArrayList<ArrayList<String>> liste){
		
		ArrayList<String> ListeEt= new ArrayList<String>();
		int a=0;
		String query="";
		ArrayList<Integer> listEt= new ArrayList<Integer>();
		for(int i=0;i<text.length()-1;i++){
			if(text.charAt(i)=='e' && text.charAt(i+1)=='t'){
				listEt.add(i);
			}
		}
		for(int i=0;i<listEt.size();i++){
			String partieA="";
			String partieB="";
			a=listEt.get(i);
			//System.out.println(a);
			char fin='a';
			int posEt=a;
			a=a-2;
			
			int b=a;
			while(fin!=' ' && a!=0){
				
				fin=text.charAt(a);			
				a=a-1;
			}
						
			if(a==0){
				//System.out.println(text.substring(a,b+1));
			partieA=text.substring(a,b+1);
			}
			if(a!=0){
				partieA=text.substring(a+2,b+1);
			}
			b=posEt+3;
			fin='a';
			while(fin!=' ' && b!=text.length()){
				fin=text.charAt(b);
				System.out.println(fin);
				b=b+1;
			}
			
			partieB=text.substring(posEt+2,b);
			ListeEt.add(partieA);
			ListeEt.add(partieB);
			System.out.println(partieA + ""+ partieB);
			
		}
		liste.add(ListeEt);
	}
	public static void main(String[] args){
		String text= "wawa et wawa et wawa2";
		String t= " wawa ou wawa et wawa2";
		ArrayList<ArrayList<String>> liste= new ArrayList<ArrayList<String>>();
		parse(t,liste);
		ArrayList<String> ListeEt= new ArrayList<String>();
		int a=0;
		String query="";
		ArrayList<Integer> listEt= new ArrayList<Integer>();
		for(int i=0;i<text.length()-1;i++){
			if(text.charAt(i)=='e' && text.charAt(i+1)=='t'){
				listEt.add(i);
			}
		}
		if(text.contains("et")){
			a=text.indexOf("et");
			String partieA="";
			String partieB="";
			a=listEt.get(0);
			System.out.println(a);
			char fin='a';
			int posEt=a;
			a=a-2;
			
			int b=a;
			while(fin!=' ' && a!=0){
				
				fin=text.charAt(a);			
				a=a-1;
			}
						
			if(a==0){
				//System.out.println(text.substring(a,b+1));
			partieA=text.substring(a,b+1);
			}
			if(a!=0){
				partieA=text.substring(a+2,b+1);
			}
			b=posEt+3;
			fin='a';
			while(fin!=' ' && b!=text.length()){
				fin=text.charAt(b);
				System.out.println(fin);
				b=b+1;
			}
			
			partieB=text.substring(posEt+2,b);
			
			//System.out.println(partieA + ""+ partieB);
			
		}
		if(a==0){
			
		}
		
	}

}
