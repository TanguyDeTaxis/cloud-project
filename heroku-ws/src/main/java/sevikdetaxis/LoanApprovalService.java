package sevikdetaxis;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.http.HttpStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Root resource (exposed at "loanapproval" path)
 */
@Path("loanapproval")
public class LoanApprovalService {

	/**
	 * Make a transfer approval
	 * 
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as "text/plain" media type.
	 * 
	 * @param nom	lastname of the Account's owner
	 * @param id	unique identifier of Account
	 * @param somme	amount of the loan
	 * @return Response.
	 */
	@Path("credit/{nom}/{id}/{somme}")
	@GET
	public Response getRisk(@PathParam("nom") String nom, @PathParam("id") Long id, @PathParam("somme") int somme)
			throws JsonParseException, JsonMappingException, IOException {

		//Creating the client to talk with GAE Services
		Client client = Client.create();
		Approval readedApproval = new Approval();
		ObjectMapper mapper = new ObjectMapper();

		// get account
		WebResource webResourceAccManager = client.resource("https://inf63app2.appspot.com/rest/account/" + id);
		ClientResponse responseAccManager;
		responseAccManager = webResourceAccManager.accept("application/json").get(ClientResponse.class);

		if (responseAccManager.getStatus() != HttpStatus.ACCEPTED_202) {
			return Response.status(HttpStatus.NOT_FOUND_404).entity("Account not found").build();
		}

		String jsonAcc = responseAccManager.getEntity(String.class);
		Account acc = mapper.readValue(jsonAcc, Account.class);

		// check if name and id parameters corresponding to data in database
		if (!acc.getLastName().equals(nom) || !acc.getId().equals(id)) {
			return Response.status(HttpStatus.NOT_FOUND_404).entity("check parameters : " + nom + " and " + id).build();
		}

		// get risk of the account
		WebResource webResourceCheck_account = client.resource("https://inf63app2.appspot.com/rest/check/" + id);
		ClientResponse responseCheck_account = webResourceCheck_account.accept("application/json")
				.get(ClientResponse.class);

		if (responseCheck_account.getStatus() != HttpStatus.ACCEPTED_202) {
			return Response.status(HttpStatus.NOT_FOUND_404).entity("Can't check account which not exist").build();
		}

		String jsonCheck = responseCheck_account.getEntity(String.class);
		boolean isRisk = mapper.readValue(jsonCheck, boolean.class);

		// initialize approvals
		Approval initialApproval = new Approval();
		initialApproval.setLastName(nom);

		if (isRisk) {
			initialApproval.setAccepted(false);
		} else {
			initialApproval.setAccepted(true);
		}

		// post approval
		WebResource resourceAddAppManager = client.resource("https://inf63app2.appspot.com/rest/approval/add");
		ClientResponse responseAddAppManager = resourceAddAppManager.accept("application/json")
				.post(ClientResponse.class, initialApproval);

		String jsonAppAdded = responseAddAppManager.getEntity(String.class);

		// read approval which containing 'approval id' : useful later
		initialApproval = mapper.readValue(jsonAppAdded, Approval.class);

		if (somme < 10000) {

			// check approval only if there is risk
			if (isRisk) { // high

				readedApproval = checkApproval(client, initialApproval.getId());

				if (!readedApproval.isAccepted()) {

					return Response.status(HttpStatus.ACCEPTED_202).entity("Approval refused for : " + acc.getLastName()).build();

				} else {

					transferMoney(client, id, somme);

					return Response.status(HttpStatus.ACCEPTED_202)
							.entity("Approval approved and money transfered to : " + acc.getLastName()).build();
				}

			} else { // low

				transferMoney(client, id, somme);

				return Response.status(HttpStatus.ACCEPTED_202).entity("Approval approved and money transfered to : " + acc.getLastName())
						.build();
			}

		} else { // somme >= 10 000

			readedApproval = checkApproval(client, initialApproval.getId());

			if (!readedApproval.isAccepted()) {

				return Response.status(HttpStatus.ACCEPTED_202).entity("Approval refused for : " + acc.getLastName()).build();

			} else {

				transferMoney(client, id, somme);

				return Response.status(HttpStatus.ACCEPTED_202).entity("Approval approved and money transfered to : " + acc.getLastName())
						.build();
			}

		}

	}

	public Approval checkApproval(Client client, Long approvalId)
			throws JsonParseException, JsonMappingException, IOException {
		WebResource resourceGetAppManager = client
				.resource("https://inf63app2.appspot.com/rest/approval/" + approvalId);

		ClientResponse getApproval;

		try {
			getApproval = resourceGetAppManager.accept("application/json").get(ClientResponse.class);
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(HttpStatus.NOT_FOUND_404).entity("Can't find approval").type("text/html").build());
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonApproval = getApproval.getEntity(String.class);
		Approval app = mapper.readValue(jsonApproval, Approval.class);
		return app;
	}

	public void transferMoney(Client client, Long id, int somme) {

		WebResource transferMoneyAccManager = client
				.resource("https://inf63app2.appspot.com/rest/account/transfer/" + id + '/' + somme);

		ClientResponse responseMoneyTransferAccManager;

		try {
			responseMoneyTransferAccManager = transferMoneyAccManager.accept("application/json")
					.get(ClientResponse.class);
		} catch (Exception e) {
			throw new WebApplicationException(
					Response.status(HttpStatus.NOT_FOUND_404).entity("Can't transfer money").type("text/html").build());
		}

	}

}
