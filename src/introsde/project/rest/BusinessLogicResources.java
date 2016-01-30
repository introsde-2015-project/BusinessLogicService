package introsde.project.rest;

import javax.ejb.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless // will work only inside a Java EE application
@LocalBean // will work only inside a Java EE application
@Path("/")
public class BusinessLogicResources {
	
	BusinessLogicModel blModel = new BusinessLogicModel();
	
	
	//Getting the list of person in the LocalDatabase Service
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/persons")
    public Response getPersonList() {
        Response persons = blModel.getAllPersons();
        return persons;
    }
    
    //Getting the list of person in the LocalDatabase Service
    
    //Getting the person information in the LocalDatabase Service
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/persons/{personId}")
    public Response getPerson(@PathParam("personId") int idPerson) {
    	Response person = blModel.getPersonById(idPerson);
        return person;
    }
	
}