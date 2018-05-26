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

import org.apache.http.HttpStatus;

import com.googlecode.objectify.ObjectifyService;

import entity.Account;
import entity.Approval;

@Path("/approval")
public class AppManager {

	static {
		ObjectifyService.register(Account.class);
		ObjectifyService.register(Approval.class);
	}

	@Path("/{id: [0-9]*}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getId(@PathParam("id") Long id) {

		Approval approval = null;
		
		try {
			//Sync loading ID
			approval = ofy().load().type(Approval.class).id(id).now();
		} catch(Exception e) {
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_ACCEPTED).entity(approval).build();
	}
	
	@Path("list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getList() {
		
		List<Approval> listApprovals = null;
		
		try {
			listApprovals = ofy().load().type(Approval.class).list();
		} catch(Exception e) {
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
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
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

	@Path("add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEntity(Approval app) {				
		
		try {
			
			com.googlecode.objectify.Key<Approval> keyApproval = ofy().save().entity(app).now();
			app = ofy().load().key(keyApproval).now();
			
		} catch(Exception e) {
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_CREATED).entity(app).build();
	}

	@Path("delete/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteEntity(@PathParam("id") Long id) {
		
		try {
			
			ofy().delete().type(Approval.class).id(id).now();
			
		} catch(Exception e) {
			return Response.status(HttpStatus.SC_NOT_FOUND).entity("Error : Resource not found").build();
		}
		
		return Response.status(HttpStatus.SC_ACCEPTED).entity("Successfully deleted").build();
	}
	
}
