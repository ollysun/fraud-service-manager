package com.etz.fraudeagleeyemanager.dto.request;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.etz.fraudeagleeyemanager.entity.eagleeyedb.NotificationGroup;

public class NotificationGroupRequestSpec implements Specification<NotificationGroup> {
	private static final long serialVersionUID = 1L;

	@Override
	public Predicate toPredicate(Root<NotificationGroup> root, CriteriaQuery<?> query,
			CriteriaBuilder criteriaBuilder) {
		// TODO Auto-generated method stub
		return null;
	}

//	  public static Specification<NotificationGroup> isLongTermCustomer() {
//	    return (root, query, builder) -> {
//	      return builder.lessThan(root.get(NotificationGroup_.createdAt), date);
//	    };
//	  }
//
//	  public static Specification<NotificationGroup> hasSalesOfMoreThan(MonetaryAmount value) {
//	    return (root, query, builder) -> {
//	    };
//	  }
	  
}
