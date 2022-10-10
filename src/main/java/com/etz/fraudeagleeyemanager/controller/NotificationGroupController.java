package com.etz.fraudeagleeyemanager.controller;



import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import com.etz.fraudeagleeyemanager.dto.request.NotificationGroupRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateNotificationGroupRequest;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.dto.response.PageResponse;
import com.etz.fraudeagleeyemanager.entity.NotificationGroup;
import com.etz.fraudeagleeyemanager.service.NotificationGroupService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/utility/notifyGroup")
@Validated
public class NotificationGroupController {

	private final NotificationGroupService notificationGroupService;

	@PostMapping
	public ResponseEntity<ModelResponse<NotificationGroup>> addNotificationGroup(@Valid @RequestBody  NotificationGroupRequest request,
																					@ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		ModelResponse<NotificationGroup> response = new ModelResponse<>(notificationGroupService.addNotificationGroup(request), HttpStatus.CREATED);
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
		
	@PutMapping
	public ModelResponse<NotificationGroup> updateNotificationGroup(@RequestBody @Valid UpdateNotificationGroupRequest request,
																	@ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setUpdatedBy(username);
		return new ModelResponse<>(notificationGroupService.updateNotificationGroup(request));
	}
	
	@GetMapping
	public PageResponse<NotificationGroup> queryNotificationGroup(@RequestParam(required = false) String name,
																  @RequestParam(required = false) Long groupId){
		return new PageResponse<>(notificationGroupService.getNotificationGroup(groupId, name));
	}
	
}
