package gae.sevik.detaxis;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import entity.Account;
import entity.Approval;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.gson.JsonObject;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.QueryExecute;

@Path("/approval")
public class AppManager {

	static {
		ObjectifyService.register(Account.class);
		ObjectifyService.register(Approval.class);
	}

	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getId(@PathParam("id") Long id) {

		Approval a = ofy().load().type(Approval.class).id(id).now();
		if (a != null) {
			// get
			return Response.status(200).entity(a).build();
		}
		return Response.status(200).entity(a).build();

	}

	@Path("list")
	@GET
	@Produces("text/html")
	public String getList() {

		List<Approval> fetched = ofy().load().type(Approval.class).list();
		String openHtml = "<html><body>";
		String list = "";
		for (Approval a : fetched) {
			list = String.format("%s %s ------ %s %s <br>", list, a.getId(), a.getLastName(), a.isAccepted());
		}
		String closeHtml = "</body></html>";

		return String.format("%s %s %s", openHtml, list, closeHtml);
	}

	@Path("add")
	@POST
	public Response createEntity(Approval app) {				
		
		ofy().save().entity(app).now();    // async without the now()
		
		// get
		Approval fetched = ofy().load().type(Approval.class).filter("lastName",app.getLastName()).first().now();
		
		String output = "Approval added";
		return Response.status(200).entity(output).build();
	}

	@Path("delete/{id}")
	@GET
	@Produces("text/html")
	public String deleteEntity(@PathParam("id") Integer id) {
		Approval a = ofy().load().type(Approval.class).id(id).now();
		if (a != null) {
			ofy().delete().type(Approval.class).id(id).now();
			// get
			return "<html><body><h1>Entity</h1> successfully deleted ! <a href=\"/rest/approval/list\">Return to list</a></body></html>";
		} else {
			// TODO: Handle exception error instead
			return "<html><body>NULL</body></html>";
		}
	}
}
