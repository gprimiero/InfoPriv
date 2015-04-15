/*
 * @author Giuseppe Primiero
 * A Java implementation of Floridi's Theory of Informational Privacy
 * for Social Networks 
 * using the Netica platform for Bayesian Reasoning 
 */



import norsys.netica.*;
import norsys.neticaEx.aliases.Node;
     
public class BuildNet {
//main method with constructors
  public static void main (String[] args){
    try {
	Node.setConstructorClass ("norsys.neticaEx.aliases.Node");
	Environ env = new Environ (null);

	Net net = new Net();
	net.setName("Informational_Privacy");

	
//Nodes of the Informational Privacy Network
	Node InfoAccess       = new Node ("InfoAccess", "lowAccess,highAccess",net);	
	Node InfoGap      = new Node ("InfoGap", "Gap_Present,Gap_Absent",  net);
	Node NetworkFriction     = new Node ("NetworkFric", "Friction,Lubricated",  net);
	Node InfoFlow = new Node ("InformationFlow", "absent,present",  net);
	Node InfoPriv	  = new Node ("InfoPriv", "absent,present", net);



	InfoAccess.setTitle ("Information_Access");
	InfoGap.setTitle ("Information_Gap");
	NetworkFriction.setTitle ("Network_Friction");
	InfoFlow.setTitle ("Information_Flow");
	InfoPriv.setTitle ("Informational_Privacy");


	//Dependencies between the nodes
	InfoGap.addLink (InfoAccess); // link from Information_Access to Information_Gap
	InfoFlow.addLink (NetworkFriction); // link from Network_Friction to Information_Flow
	InfoPriv.addLink (InfoGap); // link from Information_Gap to Informational_Privacy
	InfoPriv.addLink (InfoFlow); // link from Information_Flow to Informational_Privacy
	


	// Define the first prior probability: Access
	//Access is calculated in terms of 
	// availability of the technology 
	InfoAccess.setCPTable(0.30,0.70); 
	
	//Define the first dependent probability: InfoGap 
	//InfoGap is dependent from Access
		          //InfoAccess    high   low
 	InfoGap.setCPTable ("lowAccess", 0.60, 0.40);
	InfoGap.setCPTable ("highAccess", 0.40, 0.60);
	

	
	// Define the second prior probability: Network Friction
	//Network Friction is calculated in terms of
	//proportion of common nodes (over a given span of time) 
	//or lenght of relative distance
	NetworkFriction.setCPTable (0.30, 0.70);

    
	//Define the second dependent probability: Information Flow
	//Information Flow is dependent from Network Friction
	                 //Friction  Frictioned    Lubricated
	InfoFlow.setCPTable ("Friction", 0.70, 0.30);
	InfoFlow.setCPTable ("Lubricated", 0.30, 0.70);

        
	
	InfoPriv.setEquation ("InfoPriv (InfoGap, InformationFlow) = absent  || present");
	InfoPriv.equationToTable (1, false, false);
	
	net.compile();
	


	

	//set the value of the first prior positive
	float open = InfoGap.getBelief("Gap_Present");
	System.out.println ("\nThe positive value of the information gap is " + open );


	//set the value of the first prior negative
	float close = InfoGap.getBelief ("Gap_Absent");          
	System.out.println ("\nThe negative value of the information gap is " + close);

	//set the value of the second prior positive
	float easy = InfoFlow.getBelief ("present");          
	System.out.println ("\nThe positive value of the information flow is " + easy);
	
	//set the value of the second prior negative
	float difficult = InfoFlow.getBelief ("absent");          
	System.out.println ("\nThe negative value of the information flow is " + difficult);
	
	//get the value of the result with no gap and easy flow
	//InfoPriv.finding().enterState ("present");
	//close = InfoGap.getBelief ("Gap_Present");          
	//easy = InfoFlow.getBelief ("absent");          
	System.out.println ("\nGiven the max of infogap and the min of flow,\n"+
			    "the probability of informational privacy is " + (open + difficult));


	//get the value of the result with gap and no flow
	//InfoPriv.finding().enterState ("absent");
	//open = InfoGap.getBelief ("Gap_Absent");          
	//difficult = InfoFlow.getBelief ("absent");          
	System.out.println ("\nGiven the min of infogap and the max of flow,\n"+
			    "the probability of informational openess is " + (close + easy));


	net.finalize();  // free resources immediately and safely; not strictly necessary, but a good habit
    }
    catch (Exception e) {
	e.printStackTrace();
    }
  }
}
