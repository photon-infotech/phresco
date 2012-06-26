package com.photon.phresco.service.rest.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.stereotype.Component;

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.ApplicationType;
import com.photon.phresco.model.Database;
import com.photon.phresco.model.ModuleGroup;
import com.photon.phresco.model.ProjectInfo;
import com.photon.phresco.model.Server;
import com.photon.phresco.model.SettingsTemplate;
import com.photon.phresco.model.Technology;
import com.photon.phresco.model.WebService;
import com.photon.phresco.service.model.ServerConstants;

@Component
@Path(ServerConstants.REST_API_COMPONENT)
public class ComponentService extends DbService implements ServerConstants{
	
	private static final Logger S_LOGGER= Logger.getLogger(ComponentService.class);
	
	public ComponentService() throws PhrescoException {
		super();
    }
	
	/**
	 * Returns the list of apptypes
	 * @return
	 */
	@GET
	@Path(REST_API_APPTYPES)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAppTypes() {
		S_LOGGER.debug("Entered into ComponentService.findAppTypes()");
    	List<ApplicationType> appTypeList = mongoOperation.getCollection(APPTYPES_COLLECTION_NAME , ApplicationType.class);
    	return Response.status(Response.Status.OK).entity(appTypeList).build();
	}
	
	/**
	 * Creates the list of apptypes
	 * @param appTypes
	 * @return 
	 */
	@POST
	@Consumes (MediaType.APPLICATION_JSON)
	@Path(REST_API_APPTYPES)
	public Response createAppTypes(List<ApplicationType> appTypes) {
		S_LOGGER.debug("Entered into ComponentService.createAppTypes(List<ApplicationType> appTypes)");
		mongoOperation.insertList(APPTYPES_COLLECTION_NAME , appTypes);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Updates the list of apptypes
	 * @param appTypes
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_APPTYPES)
	public Response updateAppTypes(List<ApplicationType> appTypes) {
		S_LOGGER.debug("Entered into ComponentService.updateAppTypes(List<ApplicationType> appTypes)");
		for (ApplicationType appType : appTypes) {
			ApplicationType applnType = mongoOperation.findOne(APPTYPES_COLLECTION_NAME , new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(appType.getId())), ApplicationType.class);
			if (applnType != null) {
				mongoOperation.save(APPTYPES_COLLECTION_NAME, appType);
			}
		}
		return Response.status(Response.Status.OK).entity(appTypes).build();
	}
	
	/**
	 * Updates the list of apptypes
	 * @param appTypes
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_APPTYPES + REST_API_PATH_ID)
	public Response updateAppType(@PathParam(REST_API_PATH_PARAM_ID) String id , ApplicationType appType) {
		S_LOGGER.debug("Entered into ComponentService.updateAppType(String id , ApplicationType appType)");
		if(id.equals(appType.getId())) {
			mongoOperation.save(APPTYPES_COLLECTION_NAME, appType);
		}
		return Response.status(Response.Status.OK).entity(appType).build();
	}
	
	/**
	 * Deletes the list of apptypes
	 * @param appTypes
	 * @throws PhrescoException 
	 */
	@DELETE
	@Path(REST_API_APPTYPES)
	@Produces (MediaType.TEXT_PLAIN)
	public void deleteAppTypes(List<ApplicationType> appTypes) throws PhrescoException {
		S_LOGGER.debug("Entered into ComponentService.deleteAppTypes(List<ApplicationType> appTypes)");
		PhrescoException phrescoException = new PhrescoException(EX_PHEX00001);
		S_LOGGER.error("PhrescoException Is" + phrescoException.getErrorMessage());
		throw phrescoException;
	}
	
	/**
	 * Get the apptype by id for the given parameter
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(REST_API_APPTYPES + REST_API_PATH_ID)
	public Response getApptype(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.getApptype(String id)");
		ApplicationType appType = mongoOperation.findOne(APPTYPES_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), ApplicationType.class);
		return Response.status(Response.Status.OK).entity(appType).build();
	}
	
	/**
	 * Deletes the apptype by id for the given parameter
	 * @param id
	 * @return 
	 */
	@DELETE
	@Path(REST_API_APPTYPES + REST_API_PATH_ID)
	public Response deleteAppType(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.deleteAppType(String id)");
		mongoOperation.remove(APPTYPES_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), ApplicationType.class);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Returns the list of settings
	 * @return
	 */
	@GET
	@Path(REST_API_SETTINGS)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findSettings() {
		S_LOGGER.debug("Entered into ComponentService.findSettings()");
    	List<SettingsTemplate> settingsList = mongoOperation.getCollection(SETTINGS_COLLECTION_NAME , SettingsTemplate.class);
    	return Response.status(Response.Status.OK).entity(settingsList).build();
	}
	
	/**
	 * Creates the list of settings
	 * @param settings
	 * @return 
	 */
	@POST
	@Consumes (MediaType.APPLICATION_JSON)
	@Path(REST_API_SETTINGS)
	public Response createSettings(List<SettingsTemplate> settings) {
		S_LOGGER.debug("Entered into ComponentService.createSettings(List<SettingsTemplate> settings)");
		mongoOperation.insertList(SETTINGS_COLLECTION_NAME , settings);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Updates the list of settings
	 * @param settings
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_SETTINGS)
	public Response updateSettings(List<SettingsTemplate> settings) {
		S_LOGGER.debug("Entered into ComponentService.updateSettings(List<SettingsTemplate> settings)");
		for (SettingsTemplate settingTemplate : settings) {
			SettingsTemplate settingTemplateInfo = mongoOperation.findOne(SETTINGS_COLLECTION_NAME , new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(settingTemplate.getId())), SettingsTemplate.class);
			if (settingTemplateInfo != null) {
				mongoOperation.save(SETTINGS_COLLECTION_NAME, settingTemplate);
			}
		}
		return Response.status(Response.Status.OK).entity(settings).build();
	}
	
	/**
	 * Deletes the list of settings
	 * @param settings
	 * @throws PhrescoException 
	 */
	@DELETE
	@Path(REST_API_SETTINGS)
	public void deleteSettings(List<SettingsTemplate> settings) throws PhrescoException {
		S_LOGGER.debug("Entered into ComponentService.updateSettings(List<SettingsTemplate> settings)");
		PhrescoException phrescoException = new PhrescoException(EX_PHEX00001);
		S_LOGGER.error("PhrescoException Is" + phrescoException.getErrorMessage());
		throw phrescoException;
	}
	
	/**
	 * Get the settingTemplate by id 
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(REST_API_SETTINGS + REST_API_PATH_ID)
	public Response getSettingsTemplate(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.getSettingsTemplate(String id)");
		SettingsTemplate settingTemplate = mongoOperation.findOne(SETTINGS_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), SettingsTemplate.class);
		return Response.status(Response.Status.OK).entity(settingTemplate).build();
	}
	
	/**
	 * Updates the list of setting
	 * @param settings
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_SETTINGS + REST_API_PATH_ID)
	public Response updateSetting(@PathParam(REST_API_PATH_PARAM_ID) String id , SettingsTemplate settingsTemplate) {
		S_LOGGER.debug("Entered into ComponentService.updateAppType(String id , SettingsTemplate settingsTemplate)");
		if(id.equals(settingsTemplate.getId())) {
			mongoOperation.save(SETTINGS_COLLECTION_NAME, settingsTemplate);
		}
		return Response.status(Response.Status.OK).entity(settingsTemplate).build();
	}
	
	/**
	 * Deletes the settingsTemplate by id for the given parameter
	 * @param id
	 * @return 
	 */
	@DELETE
	@Path(REST_API_SETTINGS + REST_API_PATH_ID)
	public Response deleteSettingsTemplate(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.deleteSettingsTemplate(String id)");
		mongoOperation.remove(SETTINGS_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), SettingsTemplate.class);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Returns the list of modules
	 * @return
	 */
	@GET
	@Path(REST_API_MODULES)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findModules() {
		S_LOGGER.debug("Entered into ComponentService.findModules()");
    	List<ModuleGroup> modulesList = mongoOperation.getCollection(MODULES_COLLECTION_NAME , ModuleGroup.class);
    	return Response.status(Response.Status.OK).entity(modulesList).build();
	}
	
	/**
	 * Creates the list of modules
	 * @param modules
	 * @return 
	 */
	@POST
	@Consumes (MediaType.APPLICATION_JSON)
	@Path(REST_API_MODULES)
	public Response createModules(List<ModuleGroup> modules) {
		S_LOGGER.debug("Entered into ComponentService.createModules(List<ModuleGroup> modules)");
		mongoOperation.insertList(MODULES_COLLECTION_NAME , modules);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Updates the list of modules
	 * @param modules
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_MODULES)
	public Response updateModules(List<ModuleGroup> modules) {
		S_LOGGER.debug("Entered into ComponentService.updateModules(List<ModuleGroup> modules)");
		for (ModuleGroup moduleGroup : modules) {
			ModuleGroup module = mongoOperation.findOne(MODULES_COLLECTION_NAME , new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(moduleGroup.getId())), ModuleGroup.class);
			if (module != null) {
				mongoOperation.save(MODULES_COLLECTION_NAME, moduleGroup);
			}
		}
		return Response.status(Response.Status.OK).entity(modules).build();
	}
	
	/**
	 * Deletes the list of modules
	 * @param modules
	 * @throws PhrescoException 
	 */
	@DELETE
	@Path(REST_API_MODULES)
	public void deleteModules(List<ModuleGroup> modules) throws PhrescoException {
		S_LOGGER.debug("Entered into ComponentService.deleteModules(List<ModuleGroup> modules)");
		PhrescoException phrescoException = new PhrescoException(EX_PHEX00001);
		S_LOGGER.error("PhrescoException Is" + phrescoException.getErrorMessage());
		throw phrescoException;
	}
	
	/**
	 * Get the module by id for the given parameter
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(REST_API_MODULES + REST_API_PATH_ID)
	public Response getModule(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.getModule(String id)");
		ModuleGroup module = mongoOperation.findOne(MODULES_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), ModuleGroup.class);
		return Response.status(Response.Status.OK).entity(module).build();
	}
	
	/**
	 * Updates the module given by the parameter
	 * @param id
	 * @param module
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_MODULES + REST_API_PATH_ID)
	public Response updatemodule(@PathParam(REST_API_PATH_PARAM_ID) String id , ModuleGroup module) {
		S_LOGGER.debug("Entered into ComponentService.updatemodule(String id , ModuleGroup module)");
		if(id.equals(module.getId())) {
			mongoOperation.save(PILOTS_COLLECTION_NAME, module);
		}
		return Response.status(Response.Status.OK).entity(module).build();
	}
	
	/**
	 * Deletes the module by id for the given parameter
	 * @param id
	 * @return 
	 */
	@DELETE
	@Path(REST_API_MODULES + REST_API_PATH_ID)
	public Response deleteModules(String id) {
		S_LOGGER.debug("Entered into ComponentService.deleteModules(String id)");
		mongoOperation.remove(MODULES_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), ModuleGroup.class);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Returns the list of pilots
	 * @return
	 */
	@GET
	@Path(REST_API_PILOTS)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPilots() {
		S_LOGGER.debug("Entered into ComponentService.findPilots()");
    	List<ProjectInfo> pilotList = mongoOperation.getCollection(PILOTS_COLLECTION_NAME , ProjectInfo.class);
    	return Response.status(Response.Status.OK).entity(pilotList).build();
	}
	
	/**
	 * Creates the list of pilots
	 * @param projectInfos
	 * @return 
	 */
	@POST
	@Consumes (MediaType.APPLICATION_JSON)
	@Path(REST_API_PILOTS)
	public Response createPilots(List<ProjectInfo> projectInfos) {
		S_LOGGER.debug("Entered into ComponentService.createPilots(List<ProjectInfo> projectInfos)");
		mongoOperation.insertList(PILOTS_COLLECTION_NAME , projectInfos);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Updates the list of pilots
	 * @param projectInfos
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_PILOTS)
	public Response updatePilots(List<ProjectInfo> pilots) {
		S_LOGGER.debug("Entered into ComponentService.updatePilots(List<ProjectInfo> pilots)");
		for (ProjectInfo pilot : pilots) {
			ProjectInfo projectInfo = mongoOperation.findOne(PILOTS_COLLECTION_NAME , new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(pilot.getId())), ProjectInfo.class);
			if (projectInfo != null) {
				mongoOperation.save(PILOTS_COLLECTION_NAME, pilot);
			}
		}
		return Response.status(Response.Status.OK).entity(pilots).build();
	}
	
	/**
	 * Deletes the list of pilots
	 * @param pilots
	 * @throws PhrescoException 
	 */
	@DELETE
	@Path(REST_API_PILOTS)
	public void deletePilots(List<ProjectInfo> pilots) throws PhrescoException {
		S_LOGGER.debug("Entered into ComponentService.deletePilots(List<ProjectInfo> pilots)");
		PhrescoException phrescoException = new PhrescoException(EX_PHEX00001);
		S_LOGGER.error("PhrescoException Is" + phrescoException.getErrorMessage());
		throw phrescoException;
	}
	
	/**
	 * Get the pilot by id for the given parameter
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(REST_API_PILOTS + REST_API_PATH_ID)
	public Response getPilot(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.getPilot(String id)");
		ProjectInfo projectInfo = mongoOperation.findOne(PILOTS_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), ProjectInfo.class);
		return Response.status(Response.Status.OK).entity(projectInfo).build();
	}
	
	/**
	 * Updates the pilot given by the parameter
	 * @param id
	 * @param pilot
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_PILOTS + REST_API_PATH_ID)
	public Response updatePilot(@PathParam(REST_API_PATH_PARAM_ID) String id , ProjectInfo pilot) {
		S_LOGGER.debug("Entered into ComponentService.updatePilot(String id, ProjectInfo pilot)");
		if(id.equals(pilot.getId())) {
			mongoOperation.save(PILOTS_COLLECTION_NAME, pilot);
		}
		return Response.status(Response.Status.OK).entity(pilot).build();
	}
	
	/**
	 * Deletes the pilot by id for the given parameter
	 * @param id
	 * @return 
	 */
	@DELETE
	@Path(REST_API_PILOTS + REST_API_PATH_ID)
	public Response deletePilot(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.deletePilot(String id)");
		mongoOperation.remove(PILOTS_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), ProjectInfo.class);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Returns the list of servers
	 * @return
	 */
	@GET
	@Path(REST_API_SERVERS)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findServers() {
		S_LOGGER.debug("Entered into ComponentService.findServers()");
    	List<Server> serverList = mongoOperation.getCollection(SERVERS_COLLECTION_NAME , Server.class);
    	return Response.status(Response.Status.OK).entity(serverList).build();
	}
	
	/**
	 * Creates the list of servers
	 * @param servers
	 * @return 
	 */
	@POST
	@Consumes (MediaType.APPLICATION_JSON)
	@Path(REST_API_SERVERS)
	public Response createServers(List<Server> servers) {
		S_LOGGER.debug("Entered into ComponentService.createServers(List<Server> servers)");
		mongoOperation.insertList(SERVERS_COLLECTION_NAME , servers);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Updates the list of servers
	 * @param servers
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_SERVERS)
	public Response updateServers(List<Server> servers) {
		S_LOGGER.debug("Entered into ComponentService.updateServers(List<Server> servers)");
		for (Server server : servers) {
			Server serverInfo = mongoOperation.findOne(SERVERS_COLLECTION_NAME , new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(server.getId())), Server.class);
			if (serverInfo != null) {
				mongoOperation.save(SERVERS_COLLECTION_NAME , server);
			}
		}
		return Response.status(Response.Status.OK).entity(servers).build();
	}
	
	/**
	 * Deletes the list of servers
	 * @param servers
	 * @throws PhrescoException 
	 */
	@DELETE
	@Path(REST_API_SERVERS)
	public void deleteServers(List<Server> servers) throws PhrescoException {
		S_LOGGER.debug("Entered into ComponentService.deleteServers(List<Server> servers)");
		PhrescoException phrescoException = new PhrescoException(EX_PHEX00001);
		S_LOGGER.error("PhrescoException Is" + phrescoException.getErrorMessage());
		throw phrescoException;
	}
	
	/**
	 * Get the server by id for the given parameter
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(REST_API_SERVERS + REST_API_PATH_ID)
	public Response getServer(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.getServer(String id)");
		Server server = mongoOperation.findOne(SERVERS_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), Server.class);
		return Response.status(Response.Status.OK).entity(server).build();
	}
	
	/**
	 * Updates the server given by the parameter
	 * @param id
	 * @param server
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_SERVERS + REST_API_PATH_ID)
	public Response updateServer(@PathParam(REST_API_PATH_PARAM_ID) String id , Server server) {
		S_LOGGER.debug("Entered into ComponentService.updateServer(String id, Server server)");
		if(id.equals(server.getId())) {
			mongoOperation.save(SERVERS_COLLECTION_NAME, server);
		}
		return Response.status(Response.Status.OK).entity(server).build();
	}
	
	/**
	 * Deletes the server by id for the given parameter
	 * @param id
	 * @return 
	 */
	@DELETE
	@Path(REST_API_SERVERS + REST_API_PATH_ID)
	public Response deleteServer(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.deleteServer(String id)");
		mongoOperation.remove(SERVERS_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), Server.class);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Returns the list of databases
	 * @return
	 */
	@GET
	@Path(REST_API_DATABASES)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findDatabases() {
		S_LOGGER.debug("Entered into ComponentService.findDatabases()");
    	List<Database> dataBaseList = mongoOperation.getCollection(DATABASES_COLLECTION_NAME , Database.class);
    	return Response.status(Response.Status.OK).entity(dataBaseList).build();
	}
	
	/**
	 * Creates the list of databases
	 * @param databases
	 * @return 
	 */
	@POST
	@Consumes (MediaType.APPLICATION_JSON)
	@Path(REST_API_DATABASES)
	public Response createDatabases(List<Database> databases) {
		S_LOGGER.debug("Entered into ComponentService.createDatabases(List<Database> databases)");
		mongoOperation.insertList(DATABASES_COLLECTION_NAME , databases);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Updates the list of databases
	 * @param databases
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_DATABASES)
	public Response updateDatabases(List<Database> databases) {
		S_LOGGER.debug("Entered into ComponentService.updateDatabases(List<Database> databases)");
		for (Database dataBase : databases) {
			Database dataBaseInfo = mongoOperation.findOne(DATABASES_COLLECTION_NAME , new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(dataBase.getId())), Database.class);
			if (dataBaseInfo != null) {
				mongoOperation.save(DATABASES_COLLECTION_NAME , dataBase);
			}
		}
		return Response.status(Response.Status.OK).entity(databases).build();
	}
	
	/**
	 * Deletes the list of databases
	 * @param databases
	 * @throws PhrescoException 
	 */
	@DELETE
	@Path(REST_API_DATABASES)
	public void deleteDatabases(List<Database> databases) throws PhrescoException {
		S_LOGGER.debug("Entered into ComponentService.deleteDatabases(List<Database> databases)");
		PhrescoException phrescoException = new PhrescoException(EX_PHEX00001);
		S_LOGGER.error("PhrescoException Is" + phrescoException.getErrorMessage());
		throw phrescoException;
	}
	
	/**
	 * Get the database by id for the given parameter
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(REST_API_DATABASES + REST_API_PATH_ID)
	public Response getDatabase(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.getDatabase(String id)");
		Database database = mongoOperation.findOne(DATABASES_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), Database.class);
		return Response.status(Response.Status.OK).entity(database).build();
	}
	
	/**
	 * Updates the database given by the parameter
	 * @param id
	 * @param database
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_DATABASES + REST_API_PATH_ID)
	public Response updateDatabase(@PathParam(REST_API_PATH_PARAM_ID) String id , Database database) {
		S_LOGGER.debug("Entered into ComponentService.updateDatabase(String id, Database database)");
		if(id.equals(database.getId())) {
			mongoOperation.save(DATABASES_COLLECTION_NAME, database);
		}
		return Response.status(Response.Status.OK).entity(database).build();
	}
	
	/**
	 * Deletes the server by id for the given parameter
	 * @param id
	 * @return 
	 */
	@DELETE
	@Path(REST_API_DATABASES + REST_API_PATH_ID)
	public Response deleteDatabase(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.deleteDatabase(String id)");
		mongoOperation.remove(DATABASES_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), Database.class);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Returns the list of webservices
	 * @return
	 */
	@GET
	@Path(REST_API_WEBSERVICES)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findWebServices() {
		S_LOGGER.debug("Entered into ComponentService.findWebServices()");
    	List<WebService> webServiceList = mongoOperation.getCollection(WEBSERVICES_COLLECTION_NAME , WebService.class);
    	return Response.status(Response.Status.OK).entity(webServiceList).build();
	}
	
	/**
	 * Creates the list of webservices
	 * @param webServices
	 * @return 
	 */
	@POST
	@Consumes (MediaType.APPLICATION_JSON)
	@Path(REST_API_WEBSERVICES)
	public Response createWebServices(List<WebService> webServices) {
		S_LOGGER.debug("Entered into ComponentService.createWebServices(List<WebService> webServices)");
		mongoOperation.insertList(WEBSERVICES_COLLECTION_NAME , webServices);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Updates the list of webservices
	 * @param webServices
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_WEBSERVICES)
	public Response updateWebServices(List<WebService> webServices) {
		S_LOGGER.debug("Entered into ComponentService.updateWebServices(List<WebService> webServices)");
		for (WebService webService : webServices) {
			WebService webServiceInfo = mongoOperation.findOne(WEBSERVICES_COLLECTION_NAME , new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(webService.getId())), WebService.class);
			if (webServiceInfo != null) {
				System.out.println("Entered");
				mongoOperation.save(WEBSERVICES_COLLECTION_NAME , webService);
			}
		}
		return Response.status(Response.Status.OK).entity(webServices).build();
	}
	
	/**
	 * Deletes the list of webservices
	 * @param webServices
	 * @throws PhrescoException 
	 */
	@DELETE
	@Path(REST_API_WEBSERVICES)
	public void deleteWebServices(List<WebService> webServices) throws PhrescoException {
		S_LOGGER.debug("Entered into ComponentService.deleteWebServices(List<WebService> webServices)");
		PhrescoException phrescoException = new PhrescoException(EX_PHEX00001);
		S_LOGGER.error("PhrescoException Is" + phrescoException.getErrorMessage());
		throw phrescoException;
	}
	
	/**
	 * Get the database by id for the given parameter
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(REST_API_WEBSERVICES + REST_API_PATH_ID)
	public Response getWebService(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.getWebService(String id)");
		WebService webService = mongoOperation.findOne(WEBSERVICES_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), WebService.class);
		return Response.status(Response.Status.OK).entity(webService).build();
	}
	
	/**
	 * Updates the database given by the parameter
	 * @param id
	 * @param webService
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_WEBSERVICES + REST_API_PATH_ID)
	public Response updateWebService(@PathParam(REST_API_PATH_PARAM_ID) String id , WebService webService) {
		S_LOGGER.debug("Entered into ComponentService.updateWebService(String id, WebService webService)");
		if(id.equals(webService.getId())) {
			mongoOperation.save(WEBSERVICES_COLLECTION_NAME, webService);
		}
		return Response.status(Response.Status.OK).entity(webService).build();
	}
	
	/**
	 * Deletes the server by id for the given parameter
	 * @param id
	 * @return 
	 */
	@DELETE
	@Path(REST_API_WEBSERVICES + REST_API_PATH_ID)
	public Response deleteWebService(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.deleteWebService(String id)");
		mongoOperation.remove(WEBSERVICES_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), WebService.class);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Returns the list of technologies
	 * @return
	 */
	@GET
	@Path(REST_API_TECHNOLOGIES)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findTechnologies() {
		S_LOGGER.debug("Entered into ComponentService.findTechnologies()");
    	List<Technology> techList = mongoOperation.getCollection(TECHNOLOGIES_COLLECTION_NAME , Technology.class);
    	return Response.status(Response.Status.OK).entity(techList).build();
	}
	
	/**
	 * Creates the list of technologies
	 * @param technologies
	 */
	@POST
	@Consumes (MediaType.APPLICATION_JSON)
	@Path (REST_API_TECHNOLOGIES)
	public Response createTechnologies(List<Technology> technologies) {
		S_LOGGER.debug("Entered into ComponentService.createTechnologies(List<Technology> technologies)");
		mongoOperation.insertList(TECHNOLOGIES_COLLECTION_NAME , technologies);
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Updates the list of technologies
	 * @param technologies
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path (REST_API_TECHNOLOGIES)
	public Response updateTechnologies(List<Technology> technologies) {
		S_LOGGER.debug("Entered into ComponentService.updateTechnologies(List<Technology> technologies)");
		for (Technology tech : technologies) {
			Technology techInfo = mongoOperation.findOne(TECHNOLOGIES_COLLECTION_NAME , new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(tech.getId())), Technology.class);
			if (techInfo != null) {
				mongoOperation.save(TECHNOLOGIES_COLLECTION_NAME , tech);
			}
		}
		return Response.status(Response.Status.OK).entity(technologies).build();
	}
	
	/**
	 * Deletes the list of technologies
	 * @param technologies
	 * @throws PhrescoException 
	 */
	@DELETE
	@Path(REST_API_TECHNOLOGIES)
	public void deleteTechnologies(List<WebService> technologies) throws PhrescoException {
		S_LOGGER.debug("Entered into ComponentService.deleteTechnologies(List<WebService> technologies)");
		PhrescoException phrescoException = new PhrescoException(EX_PHEX00001);
		S_LOGGER.error("PhrescoException Is" + phrescoException.getErrorMessage());
		throw phrescoException;
	}
	
	/**
	 * Get the technology by id for the given parameter
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path(REST_API_TECHNOLOGIES + REST_API_PATH_ID)
	public Response getTechnology(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.getTechnology(String id)");
		Technology technology = mongoOperation.findOne(TECHNOLOGIES_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), Technology.class);
		return Response.status(Response.Status.OK).entity(technology).build();
	}
	
	/**
	 * Updates the technology given by the parameter
	 * @param id
	 * @param technology
	 * @return
	 */
	@PUT
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	@Path(REST_API_TECHNOLOGIES + REST_API_PATH_ID)
	public Response updateTechnology(@PathParam(REST_API_PATH_PARAM_ID) String id , Technology technology) {
		S_LOGGER.debug("Entered into ComponentService.getTechnology(String id, Technology technology)");
		if(id.equals(technology.getId())) {
			mongoOperation.save(TECHNOLOGIES_COLLECTION_NAME, technology);
		}
		return Response.status(Response.Status.OK).entity(technology).build();
	}
	
	/**
	 * Deletes the server by id for the given parameter
	 * @param id
	 * @return 
	 */
	@DELETE
	@Path(REST_API_TECHNOLOGIES + REST_API_PATH_ID)
	public Response deleteTechnology(@PathParam(REST_API_PATH_PARAM_ID) String id) {
		S_LOGGER.debug("Entered into ComponentService.deleteTechnology(String id)");
		mongoOperation.remove(TECHNOLOGIES_COLLECTION_NAME, new Query(Criteria.where(REST_API_PATH_PARAM_ID).is(id)), Technology.class);
		return Response.status(Response.Status.OK).build();
	}
}