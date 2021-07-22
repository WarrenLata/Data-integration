package src;


import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Orpha2 {
    static ArrayList<String> listeNomSigne= new ArrayList<>();
    static ArrayList<Long> listeOrphaNB= new ArrayList<>();
    static ArrayList<String> listeNomMaladie= new ArrayList<>();

    public Orpha2(){}

    public static void main(String[] args){
        try{

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(new FileReader(
                    "/home/depot/2A/gmd/projet_2015-16/orpha/clinical_sign.json"));

            JSONObject jsonObject = (JSONObject) obj;
            //jsonObject.keySet().
            JSONArray name = (JSONArray)jsonObject.get("rows");
            //String author = (String) jsonObject.get("Author");
            // JSONArray companyList = (JSONArray) jsonObject.get("Company List");

            //System.out.println("Name: " + name);
            //System.out.println("Author: " + author);
            //System.out.println("\nCompany List:");

    /*Iterator<JSONArray> iterator = name.iterator();
    while (iterator.hasNext()) {
    	iterator.
        System.out.println(iterator.next());
    }*/
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
                //Orphanumber
                long orphaNB = (long)c.get("OrphaNumber");
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
}
