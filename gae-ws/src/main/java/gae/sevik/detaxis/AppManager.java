package gae.sevik.detaxis;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpStatus;

import com.googlecode.objectify.ObjectifyService;

import entity.Account;
import entity.Approval;

/**
 * Root resource (exposed at "approval" path)
 */
@Path("/approval")
public class AppManager {

	static {
		ObjectifyService.register(Account.class);
		ObjectifyService.register(Approval.class);
	}

	/**
	 * Get an Approval
	 * 
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as a Response with a MediaType.APPLICATION_JSON object.
	 *
	 * @param id 	unique identifier for the Approval
	 * @return 		Response.
	 */
	@Path("/{id: [0-9]*}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getId(@PathParam("id") Long id) {

		Approval approval = null;
		
		try {
			
			// Sync loading of approval filtering by Id
			approval = ofy().load().type(Approval.class).id(id).now();
			
		} catch(Exception e) {
			//If errors occurs during loading, return 404
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		//If approval is null, return 404
		if(approval == null) {
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_ACCEPTED).entity(approval).build();
	}
	
	/**
	 * Get all the Approvals
	 * 
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as a Response with a MediaType.APPLICATION_JSON object.
	 *
	 * @return 		Response.
	 */
	@Path("list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getList() {
		
		List<Approval> listApprovals = null;
		
		try {
			
			// Sync loading of all Approvals
			listApprovals = ofy().load().type(Approval.class).list();
			
		} catch(Exception e) {
			//If errors occurs during loading, return 404
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		//If approval is null or empty, return 404
		if(listApprovals == null || listApprovals.isEmpty()) {
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_ACCEPTED).entity(listApprovals).build();
	}
	
/*
	@Path("list")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getList() {

		List<Approval> listApprovals = null;
		
		try {
			listApprovals = ofy().load().type(Approval.class).list();
		} catch(Exception e) {
			return String.format("Error exception : %s",e.getMessage());
		}
		
		String openHtml = "<html><body>";
		String list = "";
		for (Approval a : listApprovals) {
			list = String.format("%s %s ------ %s %s <br>", list, a.getId(), a.getLastName(), a.isAccepted());
		}
		String closeHtml = "</body></html>";

		return String.format("%s %s %s", openHtml, list, closeHtml);
	}
	*/

	/**
	 * Createan Approval
	 * 
	 * Method handling HTTP POST requests. The returned object will be sent to the
	 * client as a Response with a MediaType.APPLICATION_JSON object.
	 *
	 * @return 		Response.
	 */
	@Path("add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEntity(Approval app) {				
		
		try {
			
			// Sync saving of an approval
			com.googlecode.objectify.Key<Approval> keyApproval = ofy().save().entity(app).now();
			// Sync loading it by its key
			app = ofy().load().key(keyApproval).now();
			
		} catch(Exception e) {
			//If errors occurs during saving or loading, return 404
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_ACCEPTED).entity(app).build();
	}

	/**
	 * Delete an Approval
	 * 
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as a Response with a MediaType.APPLICATION_JSON object.
	 *
	 * @param id 	unique identifier for the Approval
	 * @return 		Response.
	 */
	@Path("delete/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteEntity(@PathParam("id") Long id) {
		
		try {
			
			ofy().delete().type(Approval.class).id(id).now();
			
		} catch(Exception e) {
			//If errors occurs during deleting, return 404
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_ACCEPTED).entity("Successfully deleted").build();
	}
	
}
