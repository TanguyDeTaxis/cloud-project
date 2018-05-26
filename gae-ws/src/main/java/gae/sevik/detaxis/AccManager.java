package gae.sevik.detaxis;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

import com.googlecode.objectify.ObjectifyService;

import entity.Account;

@Path("/account")
public class AccManager {

	static {
		ObjectifyService.register(Account.class);
	}

	@Path("list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getList() {
		List<Account> fetched = null;
		
		try {
			
			fetched = ofy().load().type(Account.class).list();
			
		} catch(Exception e) {
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		return Response.status(HttpStatus.SC_ACCEPTED).entity(fetched).build();
	}

	@Path("/{id: [0-9]*}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getId(@PathParam("id") Long id) {

		Account a = null;
		
		try {
			
			a = ofy().load().type(Account.class).id(id).now();
			
		} catch(Exception e) {
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_ACCEPTED).entity(a).build();
	}

	@Path("add/{firstname}-{lastname}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEntity(@PathParam("firstname") String firstName, @PathParam("lastname") String lastName) {

		Account account = new Account(firstName, lastName);
		
		try {
			
			com.googlecode.objectify.Key<Account> keyAccount = ofy().save().entity(account).now();
			account = ofy().load().key(keyAccount).now();
			
		} catch(Exception e) {
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_CREATED).entity(account).build();
	}

	@Path("delete/{id: [0-9]*}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteEntity(@PathParam("id") Integer id) {
		
		try {
			
			//Try to delete sync the Account
			ofy().delete().type(Account.class).id(id).now();
			
		} catch(Exception e) {
			
			//If wrong Id or if delete don't work, return 404
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
			return Response.status(HttpStatus.SC_ACCEPTED).entity("Successfully deleted").build();
	}

	@Path("transfer/{id: [0-9]*}/{somme: [0-9]*}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response transfer(@PathParam("id") Long id, @PathParam("somme") int somme) {
		
		try {
			
			// Try to load sync the Account 
			Account a = ofy().load().type(Account.class).id(id).now();
			
			int updatedAmount = a.getAmount() + somme;
			a.setAmount(updatedAmount);
			
			ofy().save().entity(a).now();
			
		} catch(Exception e) {
			//If wrong Id or if load don't work, return 404
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_ACCEPTED).entity("Money transfered").build();
	}
	
	
	
}