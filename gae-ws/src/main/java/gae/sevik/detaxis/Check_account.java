package gae.sevik.detaxis;

import static com.googlecode.objectify.ObjectifyService.ofy;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpStatus;

import com.googlecode.objectify.ObjectifyService;

import entity.Account;

@Path("/check")
public class Check_account {

	static {
		ObjectifyService.register(Account.class);
	}
	
	/**
	 * Check if an account is risky
	 * 
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as a Response with a MediaType.APPLICATION_JSON object.
	 *
	 * @param id 	unique identifier for the Account
	 * @return 		Response.
	 */
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getId(@PathParam("id") Long id) {

		Account account = null;	
		boolean risk = false;
		
		try {
			
			//Sync loading of a Account filtering by Id
			account = ofy().load().type(Account.class).id(id).now();
			
			// If account is null, return 404
			if (account == null) {
				return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : account doesn't exist").build();
			}
			
			risk = account.isRisk();
			
		} catch(Exception e) {
			//If errors occurs during loading, return 404
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_ACCEPTED).entity(risk).build();
	}

}
