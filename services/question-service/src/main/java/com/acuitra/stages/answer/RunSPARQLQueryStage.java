package com.acuitra.stages.answer;

import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.acuitra.pipeline.ContextWithJerseyClient;
import com.acuitra.stages.StageException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class RunSPARQLQueryStage extends AbstractAnswerStage {
	
	public static final String EXPECTED_INPUT_NAME = "SPARQL-QUERY";
	

	String sparqlEndpointURL;
	
	public RunSPARQLQueryStage(String sparqlEndpointURL) {
		super();
		
		this.sparqlEndpointURL = sparqlEndpointURL;
		
	}	
	
	@Override
	public void execute() {
		Map<String,String> input = getContext().getInput();
		
		String query = input.get(EXPECTED_INPUT_NAME);
		if (query == null || query.length() == 0) {
			throw new StageException("Could not find query to run. Looked for key " + EXPECTED_INPUT_NAME);
		}
		Client jerseyClient = ((ContextWithJerseyClient) getContext()).getJerseyClient();
		WebResource webResource = jerseyClient.resource(sparqlEndpointURL);
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("output", "json");
		params.add("query", query);
		
		// Run the query and get the response
		ClientResponse response = webResource.queryParams(params).get(ClientResponse.class);
		
		String output = response.getEntity(String.class);
		this.setOutput(output);
		
		
//		String json = input.get(QuepyStage.class.getName());
//		
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			JsonNode rootNode = mapper.readTree(json);
//			String query = rootNode.path("query").asText();
//			System.out.println(query);
//			
//			Client jerseyClient = ((ContextWithJerseyClient) getContext()).getJerseyClient();		
//			
//			WebResource webResource = jerseyClient.resource(sparqlEndpointURL);		
//			MultivaluedMap<String, String> params = new MultivaluedMapImpl();
//			params.add("output", "json");
//			
//			params.add("query", query);
//			
//			ClientResponse response = webResource.queryParams(params).get(ClientResponse.class);
//			
//			String text = response.getEntity(String.class);
//			
//			System.out.println("P1");
//			
//			System.out.println(text);
//			
//			this.setOutput(text);
//			
//			
//			
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//		
		
		
		
	}

}