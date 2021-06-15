package com.etz.fraudeagleeyemanager.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.dto.request.NotificationGroupRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateNotificationGroupRequest;
import com.etz.fraudeagleeyemanager.dto.response.CollectionResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.NotificationGroup;
import com.etz.fraudeagleeyemanager.service.NotificationGroupService;

@Validated
@RestController
@RequestMapping("/v1/utility/notifyGroup")
public class NotificationGroupController {

	@Autowired
	NotificationGroupService notificationGroupService;

	@PostMapping
	public ResponseEntity<ModelResponse<NotificationGroup>> createNotificationGroup(
			@Valid @RequestBody  NotificationGroupRequest request){
		ModelResponse<NotificationGroup> response = 
				new ModelResponse<>(notificationGroupService.createNotificationGroup(request));
		response.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
		
	@PutMapping(path = "/{groupId}")
	public ModelResponse<NotificationGroup> updateNotificationGroup(@PathVariable(name = "groupId", required = true) Long groupId,
			@RequestBody @Valid UpdateNotificationGroupRequest request){
		request.setGroupId(groupId);
		return new ModelResponse<>(notificationGroupService.updateNotificationGroup(request));
	}
	
	@GetMapping
	public PageResponse<NotificationGroup> queryNotificationGroup(
			@RequestParam(name = "name", required = false) String name, 
			@RequestParam(name = "groupId", required = false) Long groupId){
		return new PageResponse<>(notificationGroupService.getNotificationGroup(groupId, name));
	}
	
}
