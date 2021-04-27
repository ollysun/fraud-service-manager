package com.etz.fraudeagleeyemanager.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etz.fraudeagleeyemanager.enums.CrudOperation;
import com.etz.fraudeagleeyemanager.service.AuditTrailService;
import com.etz.fraudeagleeyemanager.util.IAudit;
import com.etz.fraudeagleeyemanager.util.IAuthorization;
import com.etz.fraudeagleeyemanager.util.RequestUtil;

@Service
@Aspect
public class AuditTrailAdvice {

	@Autowired
	AuditTrailService auditTrailService;

	@Around("execution(* javax.persistence.EntityManager.persist(..))"
			+ " && !execution(* javax.persistence.EntityManager.persist(com.etz.fraudengine.entity.AuditEntity))"
			+ " && !execution(* javax.persistence.EntityManager.persist(com.etz.fraudengine.entity.ApprovalEntity))"
			+ " && args(entity,..)")
	public Object interceptCreate(ProceedingJoinPoint jp, Object entity) {
		String eventType = CrudOperation.CREATE.getValue();
		String eventDescription = "Created " + entity.getClass().getSimpleName();

		return processintercept(jp, entity, eventType, eventDescription);
	}

	@Around("execution(* javax.persistence.EntityManager.merge(..))"
			+ " && !execution(* javax.persistence.EntityManager.merge(com.etz.fraudengine.entity.AuditEntity))"
			+ " && !execution(* javax.persistence.EntityManager.merge(com.etz.fraudengine.entity.ApprovalEntity))"
			+ " && args(entity,..)")
	public Object interceptUpdate(ProceedingJoinPoint jp, Object entity) {
		String eventType = CrudOperation.UPDATE.getValue();
		String eventDescription = "Updated " + entity.getClass().getSimpleName();

		return processintercept(jp, entity, eventType, eventDescription);
	}

	@Around("execution(* javax.persistence.EntityManager.remove(..))"
			+ " && !execution(* javax.persistence.EntityManager.remove(com.etz.fraudengine.entity.AuditEntity))"
			+ " && !execution(* javax.persistence.EntityManager.remove(com.etz.fraudengine.entity.ApprovalEntity))"
			+ " && args(entity,..)")
	public Object interceptDelete(ProceedingJoinPoint jp, Object entity) {
		String eventType = CrudOperation.DELETE.getValue();
		String eventDescription = "Deleted " + entity.getClass().getSimpleName();
		return processintercept(jp, entity, eventType, eventDescription);
	}

	private Object processintercept(ProceedingJoinPoint jp, Object entity, String eventType, 
			String eventDescription) {
		
		String username = RequestUtil.getAccessTokenClaim("user_name");
		// only entities that implement the audit interface will be audit-trailed
		if (entity instanceof IAudit) {
			auditTrailService.saveAuditTrail(eventType, eventDescription, username);
		}

		// only entities that implement the audit interface will be audit-trailed
		if (entity instanceof IAuthorization) {
			// save to temp approval table
		}

		Object response = null;
		try {
			response = jp.proceed();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}

		return response;
	}
	
}
