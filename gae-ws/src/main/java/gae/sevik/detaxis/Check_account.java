package gae.sevik.detaxis;

import static com.googlecode.objectify.ObjectifyService.ofy;


import entity.Account;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.googlecode.objectify.ObjectifyService;

@Path("/check")
public class Check_account {

	static {
		ObjectifyService.register(Account.class);
	}

	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getId(@PathParam("id") Long id) {

		Account a = ofy().load().type(Account.class).id(id).now();
		boolean risk = a.isRisk();
		
		if (a != null) {
			// get
			return Response.status(200).entity(risk).build();
		} else {
			return Response.status(200).entity("Error account doesn't exist").build();
		}
	}

}
