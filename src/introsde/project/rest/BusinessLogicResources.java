package introsde.project.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

@Path("/")
public class BusinessLogicResources {
	
	BusinessLogicModel blModel = new BusinessLogicModel();
	
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/persons")
    public Response getPersonList() {
        Response persons = blModel.getAllPersons();
        return persons;
    }
    

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/persons/{personId}")
    public Response getPerson(@PathParam("personId") int idPerson) {
    	Response person = blModel.getPersonById(idPerson);
        return person;
    }
    
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON )
    @Path("/persons/{personId}")
    public Response deletePerson(@PathParam("personId") int idPerson) {
        Response response = blModel.deletePerson(idPerson);
        return response;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON )
    @Path("/persons/{personId}/{measureType}")
    public Response getMeasureHistory(@PathParam("personId") int idPerson, @PathParam("measureType") String measureType) {
    	Response measureHistory = blModel.getPersonMeasureHistory(idPerson, measureType);
        if (measureHistory == null) {
        	throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return measureHistory;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON )
    @Path("/persons/{personId}/goals")
    public Response getGoals(@PathParam("personId") int idPerson) {
    	Response goals = blModel.getPersonGoals(idPerson);
        if (goals == null) {
        	throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return goals;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON )
    @Path("/persons/{personId}/goals/{goalId}")
    public Response getSingleGoal(@PathParam("personId") int idPerson, @PathParam("goalId") int goalId) {
    	Response goal = blModel.getSingleGoal(idPerson, goalId);
        return goal;
    }
    
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON )
    @Path("/persons/{personId}/goals/{goalId}")
    public Response deleteGoal(@PathParam("personId") int idPerson, @PathParam("goalId") int goalId) {
    	Response response = blModel.deleteGoal(idPerson, goalId);
        return response;
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON )
    @Path("/persons/{personId}/timelines")
    public Response getTimelines(@PathParam("personId") int idPerson) {
    	Response timelines = blModel.getPersonTimelines(idPerson);
        if (timelines == null) {
        	throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return timelines;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON )
    @Path("/persons/{personId}/timelines/{timelineId}")
    public Response getSingleTimeline(@PathParam("personId") int idPerson, @PathParam("timelineId") int timelineId) {
    	Response timeline = blModel.getSingleTimeline(idPerson, timelineId);
        return timeline;
    }
    
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON )
    @Path("/persons/{personId}/timelines/{timelineId}")
    public Response deleteTimeline(@PathParam("personId") int idPerson, @PathParam("timelineId") int timelineId) {
    	Response response = blModel.deleteTimeline(idPerson, timelineId);
        return response;
    }
    
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON )
    @Path("/measuretypes")
    public Response getMeasureTypes() {
    	Response measureTypes = blModel.getMeasureTypes();
        return measureTypes;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON )
    @Path("/goaltypes")
    public Response getGoalTypes() {
    	Response goalTypes = blModel.getGoalTypes();
        return goalTypes;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON )
    @Path("/persons/{personId}/{measureType}/{measureId}/checkgoal")
    public Response checkGoal(@PathParam("personId") int idPerson, @PathParam("measureType") String measureType, @PathParam("measureId") int mid) {
    	int goalReached = blModel.checkGoal(idPerson, measureType, mid);
    	if (goalReached == -1) {
    		return null;
    	}
    	Response tempResult = null;
    	JSONObject resultObj = null;
    	if (goalReached == 1) {
    		Response person = blModel.getPersonById(idPerson);
    		
        	String personString = person.readEntity(String.class);
        	JSONObject personObj = new JSONObject(personString);
        	String firstname = personObj.getString("firstname");
        	String lastname = personObj.getString("lastname");
    		
        	// If goal reached, get joke
    		tempResult = blModel.getJoke(firstname, lastname);
    		
        	String resultString = tempResult.readEntity(String.class);
        	resultObj = new JSONObject(resultString);
    	} else {
    		if (measureType.equals("calories")) {
    			tempResult = blModel.getRecipe();
    		} else if (measureType.equals("sleep")){
    			tempResult = blModel.getSleepMusic();
    		} else if (measureType.equals("steps")){
    			tempResult = blModel.getRunningMusic();
    		}
        	String resultString = tempResult.readEntity(String.class);
        	resultObj = new JSONObject(resultString);
    	}
    	resultObj.put("goalReached", goalReached);
    	resultObj.put("measureType", measureType);
    	return Response.ok(resultObj.toString()).build();
    }
}