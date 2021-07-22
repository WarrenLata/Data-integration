import java.sql.*;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

//10063691
public class main {
	
	

	public static void main(String[] args) throws Exception, SQLException{
		

		hpoboSearch myHpObo = new hpoboSearch();
		myHpObo.fonctionSearchHpobo("Name","Megacystis");                  // Nom symptome ----> Id symptome
						
		hboAnnotation myHpoAnnot = new hboAnnotation(myHpObo);
		myHpoAnnot.fonctionHboAnnotation();                         		// Id symptome -----> Id OMIM              // Id symptome -----> Id ORPHA  
		
		omintxtSearch myOmimTxt = new omintxtSearch();
		for (int i=0; i<myHpoAnnot.disease_id_OMIM.size(); i++){	
			myOmimTxt.fonctionSearchOmimTxt("pharmacology",myHpoAnnot.disease_id_OMIM.get(i));		// Nom symptome -----> Id OMIM     // myOmimTxt.disease_name
		}
		
		OmimOntoSearch myOmimOnto = new OmimOntoSearch();
		for (int i=0; i<myHpoAnnot.disease_id_OMIM.size(); i++){
			myOmimOnto.fonctionSearchOmimTxt("id",myHpoAnnot.disease_id_OMIM.get(i));		// Id OMIM -----> Nom maladie    //myOmimOnto.Name_omim
		}
		
		
		Orpha myOrpha = new Orpha();
		myOrpha.functionOrpha();
		myOrpha.searchNomSymp("Skull/cranial anomalies");
		myOrpha.searchNomId("11635");

		
		
		/*DrugBankSearch myDrugBank = new DrugBankSearch();
		for (int i=0; i<myOmimOnto.Name_omim.size(); i++){
			myDrugBank.fonctionSearchATC("indication",myOmimOnto.Name_omim.get(i));			// Nom maladie -----> Nom médicament
		}*/
		
/*
		System.out.println(".....................................................");

		SQLConnectionmeddra_all_indications myMedra_all_ind = new SQLConnectionmeddra_all_indications();    // Nom symptome ----> Cui meddra
		myMedra_all_ind.fonctionMeddra_all_indications("Sinusitis");
		
		SQLConnectionMeddra_all_se myMedra_all_se = new SQLConnectionMeddra_all_se();					// Cui meddra ----> Stitch1
		for (int i=0; i<myMedra_all_ind.cui_meddra.size(); i++){										// Cui meddra ----> Stitch2
			myMedra_all_se.fonctionMeddra_all_se(myMedra_all_ind.cui_meddra.get(i));
		}
		
		StitchSearch myStitch1 = new StitchSearch();
		StitchSearch myStitch2 = new StitchSearch();

		for (int i=0; i<myMedra_all_se.stitch1.size(); i++){
			myStitch1.fonctionSearchStitch("stitch1","CIDm"+myMedra_all_se.stitch1.get(i).substring(4,+myMedra_all_se.stitch1.get(i).length()));	// Stitch1 --> codeATC
			myStitch2.fonctionSearchStitch("stitch2","CIDs"+myMedra_all_se.stitch2.get(i).substring(4,myMedra_all_se.stitch2.get(i).length())); 	// Stitch2 --> codeATC
		}
		
		ArrayList<String> myCodeATC = new ArrayList<String>();
		for (int i=0; i<myStitch1.codeATC.size(); i++){
			if (myStitch2.codeATC.contains(myStitch1.codeATC.get(i))){
				myCodeATC.add(myStitch1.codeATC.get(i));								// codeATC de stitch1 et stitch2
			}
		}
		
		ATCSearch myATC = new ATCSearch();
		for (int i=0; i<myCodeATC.size(); i++){
				myATC.fonctionSearchATC("codeATC",myCodeATC.get(i));					// codeATC ----> Nom medicament
		}
	
*/
	
	}
	
	
	
}
