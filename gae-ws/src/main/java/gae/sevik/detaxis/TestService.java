package gae.sevik.detaxis;

import static com.googlecode.objectify.ObjectifyService.ofy;
import entity.Account;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;

@Path("/helloworld")
public class TestService {

  @GET
  @Produces("text/html")
  public String getHello() {
      
    return "<html><body>Hello MILLE BALANCOIRE Engine World!</body></html>";

  }
  
  @Path("appelEntity/{texte}")
	 @GET
	 @Produces("text/html")	
  public String getEntity(@PathParam("texte")String t) {		
		
		// Instantiates a client
	    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
		//Datastore datastore =DatastoreOptions.newBuilder().setProjectId("inf63app2").build().getService();


	    // The kind for the new entity
	    String kind = "Employee";
	    // The name/ID for the new entity
	    String name = "first";
	    // The Cloud Datastore key for the new entity
	    Key taskKey = datastore.newKeyFactory().setKind(kind).newKey(name);

	    // Prepares the new entity
	    Entity employee = Entity.newBuilder(taskKey)
	        .set("name", t)
	        .build();

	    // Saves the entity
	    datastore.put(employee);

	    System.out.printf("Saved %s: %s%n", employee.getKey().getName(), employee.getString("name"));

	    //Retrieve entity
	    Entity retrieved = datastore.get(taskKey);

	    System.out.printf("Retrieved %s: %s%n", taskKey.getName(), retrieved.getString("name"));
		


	return "<html><body><h1>Entity</h1>done</body></html>";
	}
  
	@Path("savegetEntity/{texte}")
	 @GET
	 @Produces("text/html")	
public String savegetEntity(@PathParam("texte") String t) {				
		
		Account e = new Account("sasa", "cc");
		ofy().save().entity(e).now();    // async without the now()
		
		//get
		Account fetched = ofy().load().type(Account.class).first().now();
	return "<html><body><h1>Entity</h1>done<br>"+fetched.getFirstName()+"</body></html>";
	}
}