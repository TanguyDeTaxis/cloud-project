package gae.sevik.detaxis;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpStatus;

import com.google.appengine.repackaged.com.google.protobuf.Empty;
import com.googlecode.objectify.ObjectifyService;

import entity.Account;

/**
 * Root resource (exposed at "account" path)
 */
@Path("/account")
public class AccManager {

	static {
		ObjectifyService.register(Account.class);
	}

	/**
	 * Get an Account
	 * 
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as a Response with a MediaType.APPLICATION_JSON object.
	 *
	 * @param id 	unique identifier for the Account
	 * @return 		Response.
	 */
	@Path("/{id: [0-9]*}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getId(@PathParam("id") Long id) {

		Account account = null;
		
		try {
			
			// Sync loading of account filtering by Id
			account = ofy().load().type(Account.class).id(id).now();
			
		} catch(Exception e) {
			//If errors occurs during loading, return 404
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		//If account is null, return 404
		if(account == null) {
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_ACCEPTED).entity(account).build();
	}
	
	/**
	 * Get all the Accounts
	 * 
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as a Response with a MediaType.APPLICATION_JSON object.
	 *
	 * @return 	Response.
	 */
	@Path("list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getList() {
		
		List<Account> listAccount = null;
		
		try {
			
			// Sync loading of all the Accounts
			listAccount = ofy().load().type(Account.class).list();
			
		} catch(Exception e) {
			//If errors occurs during loading, return 404
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		//If list is null or empty, return 404
		if(listAccount == null || listAccount.isEmpty()) {
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_ACCEPTED).entity(listAccount).build();
	}
	
	/**
	 * Create an Account
	 * 
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as a Response with a MediaType.APPLICATION_JSON object.
	 *
	 * @param firstName	Firstname of the new account owner
	 * @param lastName 	Lastname of the new account owner
	 * @return 			Response.
	 */
	@Path("add/{firstname}-{lastname}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEntity(@PathParam("firstname") String firstName, @PathParam("lastname") String lastName) {

		Account account = new Account(firstName, lastName);
		
		try {
			
			//Sync saving of Account
			com.googlecode.objectify.Key<Account> keyAccount = ofy().save().entity(account).now();
			// Sync loading of Account filter by key
			account = ofy().load().key(keyAccount).now();
			
		} catch(Exception e) {
			//If errors occurs during loading, return 404
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		//If account is null, return 404
		if(account == null) {
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_ACCEPTED).entity(account).build();
	}

	/**
	 * Delete an Account
	 * 
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as a Response with a MediaType.APPLICATION_JSON object.
	 * 
	 * @param id	unique identifier for the Account
	 * @return 		Response.
	 */
	@Path("delete/{id: [0-9]*}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteEntity(@PathParam("id") Integer id) {
		
		try {
			
			//Try to delete sync the Account
			ofy().delete().type(Account.class).id(id).now();
			
		} catch(Exception e) {
			
			//If errors occurs during deleting, return 404
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
			return Response.status(HttpStatus.SC_ACCEPTED).entity("Successfully deleted").build();
	}

	/**
	 * Transfer some money to someone
	 * 
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as a Response with a MediaType.APPLICATION_JSON object.
	 *
	 * @param id 	an unique identifier for the Account
	 * @param somme The value of the transfer as a Integer 
	 * @return 		Response.
	 */
	@Path("transfer/{id: [0-9]*}/{somme: [0-9]*}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response transfer(@PathParam("id") Long id, @PathParam("somme") int somme) {
		
		try {
			
			// Sync loading of Account filtering by Id
			Account account = ofy().load().type(Account.class).id(id).now();
			
			int updatedAmount = account.getAmount() + somme;
			account.setAmount(updatedAmount);
			
			// Sync saving of Account
			ofy().save().entity(account).now();
			
		} catch(Exception e) {
			//If errors occurs during load or save, return 404
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_ACCEPTED).entity("Money transfered").build();
	}
	
	
	
}