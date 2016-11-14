package com.protostar.billingnstock.taskmangement;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billingnstock.user.services.UserService;
import com.protostar.billnstock.service.filters.TaskEntityFilterData;

@Api(name = "taskService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.taskmangement", ownerName = "com.protostar.billingnstock.taskmangement", packagePath = ""))
public class TaskManagementService {

	private final Logger logger = Logger.getLogger(TaskManagementService.class
			.getName());
	
	@ApiMethod(name = "saveTask", path = "saveTask")
	public void saveTask(TaskEntity taskEntity) {
		if (taskEntity.getId() == null) {
			taskEntity.setCreatedDate(new Date());
		} else {
			taskEntity.setModifiedDate(new Date());
		}
		ofy().save().entity(taskEntity).now();
	}

	@ApiMethod(name = "getTaskById", path = "getTaskById")
	public TaskEntity getTaskById(@Named("id") Long id) {
		TaskEntity task = ofy().load().type(TaskEntity.class).id(id).now();
		return task;
	}

	@ApiMethod(name = "getAllTask", path = "getAllTask")
	public List<TaskEntity> getAllTask(@Named("id") Long busId) {
		// logger.info("getAllTask#busId:" + busId);
		List<TaskEntity> list = ofy().load().type(TaskEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();

		return list;
	}

	@ApiMethod(name = "getMyAllTask", path = "getMyAllTask")
	public List<TaskEntity> getMyAllTask(@Named("userId") Long userId) {
		UserService userService = new UserService();
		// logger.info("getMyAllTask#email_id:" + email_id);
		UserEntity user = userService.getUserByID(userId);
		// logger.info("user:" + user);
		List<TaskEntity> list = ofy().load().type(TaskEntity.class)
				.filter("assignedTo", user).list();
		// logger.info("list:size:" + list.size());
		return list;
	}

	@ApiMethod(name = "getTasksPendingOlderThanDate", path = "getTasksPendingOlderThanDate")
	public List<TaskEntity> getTasksPendingOlderThanDate(
			@Named("id") Long busId, @Named("date") Date date) {

		List<TaskEntity> tasks = ofy().load().type(TaskEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		List<TaskEntity> filteredTasks = new ArrayList<TaskEntity>();

		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getAssignedDate().before(date)) {
				filteredTasks.add(tasks.get(i));
			}
		}
		return filteredTasks;
	}

	@ApiMethod(name = "filterTasksByFitlerData", path = "filterTasksByFitlerData")
	public List<TaskEntity> filterTasksByFitlerData(
			TaskEntityFilterData fitlerData) {

		Query<TaskEntity> filterQuery = ofy()
				.load()
				.type(TaskEntity.class)
				.ancestor(
						Key.create(BusinessEntity.class,
								fitlerData.getBusinessId()))
				.filter("assignedDate >", fitlerData.getSinceAssignedDate());

		if (fitlerData.getAssignedBy() != null)
			filterQuery = filterQuery.filter("assignedBy",
					fitlerData.getAssignedBy());
		if (fitlerData.getAssignedTo() != null)
			filterQuery = filterQuery.filter("assignedTo",
					fitlerData.getAssignedTo());
		if (fitlerData.getTaskStatus() != null)
			filterQuery = filterQuery.filter("taskStatus",
					fitlerData.getTaskStatus());

		List<TaskEntity> filteredTasks = filterQuery.list();

		logger.info("filteredTasks#size:" + filteredTasks.size());

		return filteredTasks;
	}
}// end of TaskServices
