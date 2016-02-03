package introsde.project.rest;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONArray;
import org.json.JSONObject;


public class BusinessLogicModel {
	
	// Configure client
    ClientConfig clientConfig = new ClientConfig();
    Client client = ClientBuilder.newClient(clientConfig);
    WebTarget storageService = client.target(getStorageURI());
    WebTarget processService = client.target(getProcessURI());
    String acceptType = "application/json";
	
    public Response getAllPersons() {
    	Response result = storageService.path("/persons").request().accept(acceptType).get();
    	return result;
    }
    
    public Response getPersonById(int personId) {
    	Response result = storageService.path("/persons/"+personId).request().accept(acceptType).get();
    	return result;
    }
    
    public Response deletePerson(int personId) {
    	Response result = storageService.path("/persons/"+personId).request().accept(acceptType).delete();
    	return result;
    }
    
    public Response getPersonMeasureHistory(int personId, String measureType) {
    	Response result = storageService.path("/persons/"+personId+"/"+measureType).request().accept(acceptType).get();
    	return result;
    }
    
    public Response getPersonGoals(int personId) {
    	Response result = storageService.path("/persons/"+personId+"/goals").request().accept(acceptType).get();
    	return result;
    }
    
    public Response getPersonGoalsByMeasure(int personId, String measureType) {
    	Response result = storageService.path("/persons/"+personId+"/"+measureType+"/goals").request().accept(acceptType).get();
    	return result;
    }

    public Response getMeasureTypes() {
    	Response result = storageService.path("/measuretypes").request().accept(acceptType).get();
    	return result;
    }
    
    public Response getGoalTypes() {
    	Response result = storageService.path("/goaltypes").request().accept(acceptType).get();
    	return result;
    }
    
    public boolean checkGoal(int personId, String measureType, int measureId) {
    	boolean goalReached = false;
    	
    	Response measure = storageService.path("/persons/"+personId+"/"+measureType+"/"+measureId).request().accept(acceptType).get();
    	Response goals = this.getPersonGoalsByMeasure(personId, measureType);
    	
    	String goalsString = goals.readEntity(String.class);
    	JSONArray goalsArray = new JSONArray(goalsString);
    	
    	String measureString = measure.readEntity(String.class);
    	JSONObject measureJson = new JSONObject(measureString);
    	for (int i=0; i<goalsArray.length(); i++) {
    		int goalValue = goalsArray.getJSONObject(i).getInt("value");
    		int measureValue = measureJson.getInt("value");
    		if (measureType.equals("calories")) {
    			if (measureValue < goalValue) {
    				goalReached = true;
    			}
    		} else {
    			if (measureValue > goalValue) {
    				goalReached = true;
    			}
    		}
    	}
    	return goalReached;
    }
    
    public Response getSleepMusic() {
    	Response result = storageService.path("/music/sleep").request().accept(acceptType).get();
    	return result;
    }
    
    public Response getRunningMusic() {
    	Response result = storageService.path("/music/run").request().accept(acceptType).get();
    	return result;
    }
    
    public Response getJoke(String firstname, String lastname) {
    	Response result = storageService.path("/joke")
    			.queryParam("firstname", firstname)
    			.queryParam("lastname", lastname)
    			.request().accept(acceptType).get();
    	return result;
    }
    
    public Response getRecipe() {
    	Response result = storageService.path("/recipe").request().accept(acceptType).get();
    	return result;
    }

    private static URI getStorageURI() {
        return UriBuilder.fromUri(
                "http://localhost:5700/").build();
    }

    private static URI getProcessURI() {
        return UriBuilder.fromUri(
                "http://localhost:7000/").build();
    }

}
