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
import com.etz.fraudeagleeyemanager.dto.request.ApprovalRequest;
import com.etz.fraudeagleeyemanager.dto.request.UpdateUserNotificationRequest;
import com.etz.fraudeagleeyemanager.dto.request.UserNotificationRequest;
import com.etz.fraudeagleeyemanager.dto.response.BooleanResponse;
import com.etz.fraudeagleeyemanager.dto.response.CollectionResponse;
import com.etz.fraudeagleeyemanager.dto.response.ModelResponse;
import com.etz.fraudeagleeyemanager.entity.UserNotification;
import com.etz.fraudeagleeyemanager.service.UtilityService;

import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/utility")
@Validated
public class UtilityController {

	private final UtilityService utilityService;

	@PostMapping("/approve")
	public ResponseEntity<BooleanResponse> approve(@Valid @RequestBody  ApprovalRequest request,
																	   @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		BooleanResponse response = new BooleanResponse(utilityService.approval(request));
		return ResponseEntity.status(HttpStatus.valueOf(response.getStatus())).body(response);
	}
	
	@PostMapping("/notification")
	public ModelResponse<UserNotification> createUserNotifcation(
			@RequestBody @Valid UserNotificationRequest request, @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setCreatedBy(username);
		return new ModelResponse<>(utilityService.createUserNotification(request));
	}
	
	@PutMapping("/notification")
	public ModelResponse<UserNotification> updateUserNotifcationReadStatus(
			@RequestBody @Valid UpdateUserNotificationRequest request, @ApiIgnore @RequestAttribute(AppConstant.USERNAME) String username){
		request.setUpdatedBy(username);
		return new ModelResponse<>(utilityService.updateUserNotification(request));
	}
	
	@GetMapping("/notification")
	public CollectionResponse<UserNotification> queryUserNotification(@RequestParam Long userRoleId, @RequestParam(required = false) Long userId){
		return new CollectionResponse<>(utilityService.getUserNotification(userRoleId, userId));
	}
}
