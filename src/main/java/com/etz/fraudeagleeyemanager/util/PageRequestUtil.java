package com.etz.fraudeagleeyemanager.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageRequestUtil {

    private static String DEFAULT_PAGE_LIMIT = "50";
    private static String PAGE_LIMIT = "limit";
    private static String PAGE = "page";
    private static String CREATEDAT = "createdAt";

    public static PageRequest getPageRequest(int startPosition) {
        return PageRequest.of(startPosition, getPageLimit(), Sort.Direction.DESC, CREATEDAT);
    }

    public static PageRequest getPageRequest() {
        return PageRequest.of(getPage(), getPageLimit(), Sort.Direction.DESC, CREATEDAT);
    }

    public static PageRequest getPageRequest(Sort.Direction direction) {
        return PageRequest.of(getPage(), getPageLimit(), direction, CREATEDAT);
    }

    public static PageRequest getPageRequest(Sort.Direction direction, String columnName) {
        return PageRequest.of(getPage(), getPageLimit(), direction, columnName);
    }

    private static int getPage(){
        /* Subtract one getFrom the value because PageRequest.of() first parameter starts getFrom 0 index */
        return Integer.valueOf(RequestUtil.getRequest().getParameter(PAGE) != null ? RequestUtil.getRequest().getParameter(PAGE) : "1") - 1;
    }
    
    private static int getPageLimit(){
        return Integer.valueOf(RequestUtil.getRequest().getParameter(PAGE_LIMIT) != null ? RequestUtil.getRequest().getParameter(PAGE_LIMIT) : DEFAULT_PAGE_LIMIT);
    }
}

