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
    String list = "";
    for(Account a : fetched) {
    	list = String.format("%s %s ------ %s %s <br>", list, a.getId(), a.getFirstName(), a.getLastName());
    }
    String closeHtml = "</body></html>";

    return String.format("%s %s %s", openHtml, list, closeHtml);
  }
  
	@Path("add/{firstname}-{lastname}")
	 @GET
	 @Produces("text/html")	
	public String createEntity(@PathParam("firstname") String firstName, @PathParam("lastname") String lastName) {				
		
		Account e = new Account(firstName,lastName);
		ofy().save().entity(e).now();    // async without the now()
		
		//get
		Account fetched = ofy().load().type(Account.class).filter("firstName","tanguy").first().now();
	return "<html><body><h1>Entity</h1>done<br>"+fetched.getFirstName()+"</body></html>";
	}
	
	@Path("delete/{id}")
	 @GET
	 @Produces("text/html")	
	public String deleteEntity(@PathParam("id") Integer id) {				
		
		ofy().delete().type(Account.class).id(id).now();
		//get
	return "<html><body><h1>Entity</h1> successfully deleted ! <a href=\"/rest/account/list\">Return to list</a></body></html>";
	}
}