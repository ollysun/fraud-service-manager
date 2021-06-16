package com.etz.fraudeagleeyemanager.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.entity.BaseAuditEntity;
import com.etz.fraudeagleeyemanager.enums.CrudOperation;
import com.etz.fraudeagleeyemanager.service.AuditTrailService;
import com.etz.fraudeagleeyemanager.util.JsonConverter;
import com.etz.fraudeagleeyemanager.util.RequestUtil;

@Service
@Aspect
public class AuditTrailAdvice {

	@Autowired
	AuditTrailService auditTrailService;

	@Around("execution(* javax.persistence.EntityManager.persist(..))"
			+ " && args(entity,..) && !args(com.etz.fraudeagleeyemanager.entity.EventLogEntity)")
	public Object interceptCreate(ProceedingJoinPoint jp, Object entity) {
		String eventType = CrudOperation.CREATE.getValue();
		String eventDescription = "Created " + entity.getClass().getSimpleName();
		
		return processintercept(jp, entity, eventType, eventDescription);
	}
	
	@Around("execution(* javax.persistence.EntityManager.merge(..))"
			+ " && !execution(* javax.persistence.EntityManager.merge(com.etz.fraudeagleeyemanager.entity.EventLogEntity))"
			+ " && args(entity,..)")
	public Object interceptUpdate(ProceedingJoinPoint jp, Object entity) {
		String eventType = CrudOperation.UPDATE.getValue();
		String eventDescription = "Updated " + entity.getClass().getSimpleName();

		return processintercept(jp, entity, eventType, eventDescription);
	}

	@Around("execution(* javax.persistence.EntityManager.remove(..))"
			+ " && !execution(* javax.persistence.EntityManager.remove(com.etz.fraudeagleeyemanager.entity.EventLogEntity))"
			+ " && args(entity,..)")
	public Object interceptDelete(ProceedingJoinPoint jp, Object entity) {
		String eventType = CrudOperation.DELETE.getValue();
		String eventDescription = "Deleted " + entity.getClass().getSimpleName();

		return processintercept(jp, entity, eventType, eventDescription);
	}

	private Object processintercept(ProceedingJoinPoint jp, Object entity, String eventType, String eventDescription) {
		Object response = null;
		
		try {
			response = jp.proceed();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		System.out.println("entity: " + entity);
		System.out.println("response: " + response);
		
		// only entities that extend the BaseEntityModel class will be audited
		if (entity instanceof BaseAuditEntity) {
			BaseAuditEntity baseAuditEntity = (BaseAuditEntity) entity;
			baseAuditEntity.setEntity(entity.getClass().getSimpleName());
			baseAuditEntity.setEventDescription(eventDescription);
			baseAuditEntity.setEndpoint(RequestUtil.getSourceURL());
			baseAuditEntity.setUserId(RequestUtil.getAccessTokenClaim("user_name"));
			baseAuditEntity.setEventType(eventType);

			if (baseAuditEntity.getRecordAfter() != null) {
				String recordAfter = JsonConverter.objectToJson(entity);
				baseAuditEntity.setRecordAfter(
						JsonConverter.objectToJson(response).equalsIgnoreCase("null") ? recordAfter : JsonConverter.objectToJson(response));
			}
			
			auditTrailService.save(baseAuditEntity);
		}

		return response;
	}

}
