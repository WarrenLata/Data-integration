

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Orpha {
    static ArrayList<String> listeNomSigne= new ArrayList<>();
    static ArrayList<String> listeOrphaNB= new ArrayList<>();
    static ArrayList<String> listeNomMaladie= new ArrayList<>();
    
    static ArrayList<String> resNomMaladie= new ArrayList<>();

    public Orpha(){
    	listeNomSigne= new ArrayList<>();
    	listeOrphaNB= new ArrayList<>();
    	listeNomMaladie= new ArrayList<>();
    	resNomMaladie= new ArrayList<>();
    }

    public void functionOrpha(){
        try{

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(new FileReader(
                    "/home/depot/2A/gmd/projet_2015-16/orpha/clinical_sign.json"));

            JSONObject jsonObject = (JSONObject) obj;
            //jsonObject.keySet().
            JSONArray name = (JSONArray)jsonObject.get("rows");
            for(int i=0; i< name.size();i++){
                //System.out.println(name.get(1));
                name.get(1);
                JSONObject b= (JSONObject)name.get(i);
                JSONObject a= (JSONObject)b.get("value");

                //nom du symptome
                JSONObject c= (JSONObject)a.get("clinicalSign");
                JSONObject Nom =(JSONObject)c.get("Name");
                String nom = (String)Nom.get("text");
                listeNomSigne.add(nom);
                System.out.println(nom);

                c = (JSONObject)a.get("disease");
                //Disease ID
                String orphaNB = (String)c.get("id");
                listeOrphaNB.add(orphaNB);
                System.out.println(orphaNB);
                //Nom de la maladie
                Nom = (JSONObject)c.get("Name");
                nom = (String)Nom.get("text");
                listeNomMaladie.add(nom);
                System.out.println(nom);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void searchNomSymp(String symptome){
    	for (int i=0; i<listeNomSigne.size(); i++){
    		if(listeNomSigne.get(i).equals(symptome)){
    			resNomMaladie.add(listeNomMaladie.get(i));
    			System.out.println(listeNomMaladie.get(i));
    		}
    	}
    }
    
    public void searchNomId(String id){
    	for (int i=0; i<listeOrphaNB.size(); i++){
    		if(listeOrphaNB.get(i).equals(id)&!resNomMaladie.contains(listeNomMaladie.get(i))){
    			resNomMaladie.add(listeNomMaladie.get(i));
    			System.out.println(listeNomMaladie.get(i));
    		}
    	}
    }
    
}
