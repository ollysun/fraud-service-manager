package com.etz.fraudeagleeyemanager.controller;



import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.dto.request.NotificationGroupRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateNotificationGroupRequest;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.NotificationGroup;
import com.etz.fraudeagleeyemanager.service.NotificationGroupService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/utility/notifyGroup")
public class NotificationGroupController {

	private final NotificationGroupService notificationGroupService;

	@PostMapping
	public ResponseEntity<ModelResponse<NotificationGroup>> createNotificationGroup(@Valid @RequestBody  NotificationGroupRequest request){
		ModelResponse<NotificationGroup> response = new ModelResponse<>(notificationGroupService.createNotificationGroup(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
		
	@PutMapping("/{groupId}")
	public ModelResponse<NotificationGroup> updateNotificationGroup(@PathVariable Long groupId, @RequestBody @Valid UpdateNotificationGroupRequest request){
		return new ModelResponse<>(notificationGroupService.updateNotificationGroup(request, groupId));
	}
	
	@GetMapping
	public PageResponse<NotificationGroup> queryNotificationGroup(String name, Long groupId){
		return new PageResponse<>(notificationGroupService.getNotificationGroup(groupId, name));
	}
	
}
