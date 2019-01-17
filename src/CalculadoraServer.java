			import CalculadoraApp.*;
			import org.omg.CosNaming.*;
			import org.omg.CosNaming.NamingContextPackage.*;
			import org.omg.CORBA.*;
			import org.omg.PortableServer.*;
			import org.omg.PortableServer.POA;
				
			import java.util.Properties;
			
			class CalculadoraImpl extends CalculadoraPOA {
			  private ORB orb;
			
			  public void setORB(ORB orb_val) {
			    orb = orb_val; 
			  }
	    
			  // implement  methods
			  public float suma(float a,float b) {
				    return a+b;
			  }
              public float resta(float a,float b) {
				    return a-b;
			  }
              public float multiplicacion(float a,float b) {
				    return a*b;
			  }
              public float division(float a,float b) {
				    return a/b;
			  }
			    
			  // implement shutdown() method
			  public void shutdown() {
			    orb.shutdown(false);
			  }
			}
			
			
			public class CalculadoraServer {
			
			  public static void main(String args[]) {
			    try{
			      // create and initialize the ORB
	      			ORB orb = ORB.init(args, null);
			
			      // get reference to rootpoa & activate the POAManager
			      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			      rootpoa.the_POAManager().activate();
			
			      // create servant and register it with the ORB
			      CalculadoraImpl calculadoraImpl = new CalculadoraImpl();
			      calculadoraImpl.setORB(orb); 
			
			      // get object reference from the servant
			      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(calculadoraImpl);
			      Calculadora href = CalculadoraHelper.narrow(ref);
				  
			      // get the root naming context
			      // NameService invokes the name service
			      org.omg.CORBA.Object objRef =
				          orb.resolve_initial_references("NameService");
			      // Use NamingContextExt which is part of the Interoperable
			      // Naming Service (INS) specification.
			      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
				
			      // bind the Object Reference in Naming
			      String name = "Calculadora";
			      NameComponent path[] = ncRef.to_name( name );
				      ncRef.rebind(path, href);
			
				      System.out.println("CalculadoraServidor is ready and waiting ...");
			
			      // wait for invocations from clients
			      orb.run();
			    } 
				
				      catch (Exception e) {
			        System.err.println("ERROR: " + e);
			        e.printStackTrace(System.out);
			      }
				  
			      System.out.println("CalculadoraServer Exiting ...");
				
			  }
			}