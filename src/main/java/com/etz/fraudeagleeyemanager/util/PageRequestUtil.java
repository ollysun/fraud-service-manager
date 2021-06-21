package com.etz.fraudeagleeyemanager.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.etz.fraudeagleeyemanager.constant.AppConstant;

public class PageRequestUtil {
	
	private PageRequestUtil() {}

    public static PageRequest getPageRequest(int startPosition) {
        return PageRequest.of(startPosition, getPageLimit(), Sort.Direction.DESC, AppConstant.UPDATEDAT);
    }

    public static PageRequest getPageRequest() {
        return PageRequest.of(getPage(), getPageLimit(), Sort.Direction.DESC, AppConstant.UPDATEDAT);
    }

    public static PageRequest getPageRequest(Sort.Direction direction) {
        return PageRequest.of(getPage(), getPageLimit(), direction, AppConstant.UPDATEDAT);
    }

    public static PageRequest getPageRequest(Sort.Direction direction, String columnName) {
        return PageRequest.of(getPage(), getPageLimit(), direction, columnName);
    }

    private static int getPage(){
        /* Subtract one getFrom the value because PageRequest.of() first parameter starts getFrom 0 index */
        return Integer.valueOf(RequestUtil.getRequest().getParameter(AppConstant.PAGE) != null ? RequestUtil.getRequest().getParameter(AppConstant.PAGE) : "1") - 1;
    }
    
    private static int getPageLimit(){
        return Integer.valueOf(RequestUtil.getRequest().getParameter(AppConstant.PAGE_LIMIT) != null ? RequestUtil.getRequest().getParameter(AppConstant.PAGE_LIMIT) : AppConstant.DEFAULT_PAGE_LIMIT);
    }
}

