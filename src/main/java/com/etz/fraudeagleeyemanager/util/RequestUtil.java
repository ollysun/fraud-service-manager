package com.etz.fraudeagleeyemanager.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.etz.fraudengine.constant.AppConstant.PAGE;

public class RequestUtil {

    public static HttpServletRequest getRequest(){
        return  ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
    }

    public static void setMessage(String message) {
        getRequest().setAttribute("message", message);
    }

    public static String getMessage() {
        String message = AppUtil.isBlank(getRequest().getAttribute("message")) ?
                "Processed successfully" : (String) getRequest().getAttribute("message");
        setMessage("");
        return message;
    }

    public static String perPage() {
        String pageSize = "50";
        return AppUtil.isBlank(getRequest().getParameter("page_size")) ? pageSize :getRequest().getParameter("page_size");
    }

    public static int getPage(){
        return Integer.parseInt(getRequest().getParameter(PAGE) != null ? getRequest().getParameter(PAGE) : "1");
    }

    public static String getIpAddress() {
        return getClientIpAddress(getRequest());
    }

    public static String getUserAgent() {
        return getRequest().getHeader("user-agent");
    }

    private static String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_VIA");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }

        String comma = ",";
        if(ip.contains(comma)){
            ip = ip.split(comma)[0];
        }
        return ip;
    }

    public static void setStartTime(long startTime) {
        getRequest().setAttribute("start_time", startTime);
    }

    public static long getStartTime() {
        return (Long) getRequest().getAttribute("start_time");
    }

}