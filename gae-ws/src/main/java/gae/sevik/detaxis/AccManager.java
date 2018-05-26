package gae.sevik.detaxis;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import entity.Account;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.QueryExecute;

@Path("/account")
public class AccManager {

	static {
		ObjectifyService.register(Account.class);
	}

	@Path("list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getList() {
		List<Account> fetched = ofy().load().type(Account.class).list();

		return Response.status(200).entity(fetched).build();
	}

	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getId(@PathParam("id") Long id) {

		Account a = ofy().load().type(Account.class).id(id).now();
		if (a != null) {
			// get
			return Response.status(200).entity(a).build();
		}
		return Response.status(200).entity(a).build();
	}

	@Path("add/{firstname}-{lastname}")
	@GET
	@Produces("text/html")
	public String createEntity(@PathParam("firstname") String firstName, @PathParam("lastname") String lastName) {

		Account e = new Account(firstName, lastName);
		ofy().save().entity(e).now(); // async without the now()

		// get
		Account fetched = ofy().load().type(Account.class).filter("firstName", firstName).first().now();
		return "<html><body><h1>Entity</h1>done<br>" + fetched.getFirstName() + "</body></html>";
	}

	@Path("delete/{id}")
	@GET
	@Produces("text/html")
	public String deleteEntity(@PathParam("id") Integer id) {
		Account a = ofy().load().type(Account.class).id(id).now();
		if (a != null) {
			ofy().delete().type(Account.class).id(id).now();
			// get
			return "<html><body><h1>Entity</h1> successfully deleted ! <a href=\"/rest/account/list\">Return to list</a></body></html>";
		} else {
			// TODO: Handle exception error instead
			return "<html><body>NULL</body></html>";
		}
	}

	@Path("transfer/{id}/{somme}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response transfer(@PathParam("id") Long id, @PathParam("somme") int somme) {
		
		Account a = ofy().load().type(Account.class).id(id).now();
		
		int updatedAmount = a.getAmount() + somme;
		
		a.setAmount(updatedAmount);
		
		ofy().save().entity(a).now();
		
		String output = "Money transfered";
		return Response.status(200).entity(output).build();
	}
	
	
	
}