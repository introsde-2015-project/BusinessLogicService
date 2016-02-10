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
    
	public Response getSingleGoal(int personId, int goalId) {
		Response result = storageService.path("/persons/"+personId+"/goals/"+goalId).request().accept(acceptType).get();
    	return result;
	}

	public Response deleteGoal(int personId, int goalId) {
		Response result = storageService.path("/persons/"+personId+"/goals/"+goalId).request().accept(acceptType).delete();
		return result;
	}
    
    public Response getPersonTimelines(int personId) {
    	Response result = storageService.path("/persons/"+personId+"/timelines").request().accept(acceptType).get();
    	return result;
    }
    

	public Response getSingleTimeline(int personId, int timelineId) {
		Response result = storageService.path("/persons/"+personId+"/timelines/"+timelineId).request().accept(acceptType).get();
		return result;
	}

	public Response deleteTimeline(int personId, int timelineId) {
		Response result = storageService.path("/persons/"+personId+"/timelines/"+timelineId).request().accept(acceptType).delete();
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
    
    // Return -1 if no goals exist, 0 if not reached, 1 if reached
    public int checkGoal(int personId, String measureType, int measureId) {
    	int GOAL_NOT_EXIST = -1;
    	int GOAL_NOT_REACHED = 0;
    	int GOAL_REACHED = 1;
    	int goalReached = GOAL_NOT_REACHED;
    	
    	Response measure = storageService.path("/persons/"+personId+"/"+measureType+"/"+measureId).request().accept(acceptType).get();
    	Response goals = this.getPersonGoalsByMeasure(personId, measureType);
    	
    	String goalsString = goals.readEntity(String.class);
    	JSONArray goalsArray = new JSONArray(goalsString);
    	if (goalsArray.length() == 0) {
    		return GOAL_NOT_EXIST;
    	}
    	
    	String measureString = measure.readEntity(String.class);
    	JSONObject measureJson = new JSONObject(measureString);
    	for (int i=0; i<goalsArray.length(); i++) {
    		int goalValue = goalsArray.getJSONObject(i).getInt("value");
    		int measureValue = measureJson.getInt("value");
    		if (measureType.equals("calories")) {
    			if (measureValue < goalValue) {
    				goalReached = GOAL_REACHED;
    			}
    		} else {
    			if (measureValue > goalValue) {
    				goalReached = GOAL_REACHED;
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
                "https://storage-introsde.herokuapp.com/").build();
    }

    private static URI getProcessURI() {
        return UriBuilder.fromUri(
                "https://processcentric-introsde.herokuapp.com/").build();
    }

}
