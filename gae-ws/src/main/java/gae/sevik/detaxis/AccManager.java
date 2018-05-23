package gae.sevik.detaxis;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import entity.Account;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.googlecode.objectify.cmd.QueryExecute;

@Path("/account")
public class AccManager {

  @Path("list")
  @GET
  @Produces("text/html")
  public String getList() {
      
	List<Account> fetched = ofy().load().type(Account.class).list();
    String openHtml = "<html><body>";
    String add = "";
    for(Account a : fetched) {
    	add+=a.getFirstName() + " " + a.getLastName() + "<br>";
    }
    String closeHtml = "</body></html>";

    return openHtml + add + closeHtml;
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
  
	@Path("addAccount/{firstname}-{lastname}")
	 @GET
	 @Produces("text/html")	
public String savegetEntity(@PathParam("firstname") String firstName, @PathParam("lastname") String lastName) {				
		
		Account e = new Account(firstName,lastName);
		ofy().save().entity(e).now();    // async without the now()
		
		//get
		Account fetched = ofy().load().type(Account.class).filter("firstName","tanguy").first().now();
	return "<html><body><h1>Entity</h1>done<br>"+fetched.getFirstName()+"</body></html>";
	}
}