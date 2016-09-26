package com.protostar.billingnstock.taskmangement;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.protostar.billingnstock.user.entities.BusinessEntity;

@Api(name = "taskService", version = "v0.1", namespace = @ApiNamespace(ownerDomain = "com.protostar.billingnstock.taskmangement", ownerName = "com.protostar.billingnstock.taskmangement", packagePath = ""))
public class TaskService {

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
		System.out.println("getAllTask#busId:" + busId);
		List<TaskEntity> filteredTasks = ofy().load().type(TaskEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		return filteredTasks;
	}

	@ApiMethod(name = "getTasksPendingOlderThanDate", path = "getTasksPendingOlderThanDate")
	public List<TaskEntity> getTasksPendingOlderThanDate(
			@Named("id") Long busId, @Named("date") Date date) {

		List<TaskEntity> tasks = ofy().load().type(TaskEntity.class)
				.ancestor(Key.create(BusinessEntity.class, busId)).list();
		List<TaskEntity> filteredTasks = new ArrayList<TaskEntity>();

		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getCreatedDate().before(date)) {
				filteredTasks.add(tasks.get(i));
			}
		}
		return filteredTasks;
	}

}// end of TaskServices
