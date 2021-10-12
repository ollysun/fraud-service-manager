package com.etz.fraudeagleeyemanager.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.etz.fraudeagleeyemanager.constant.AppConstant;
import static com.etz.fraudeagleeyemanager.constant.AppConstant.ACCESS_TOKEN_CLAIM;
import static com.etz.fraudeagleeyemanager.constant.AppConstant.ACCESS_TOKEN;

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
    	return AppUtil.isBlank(getRequest().getParameter(AppConstant.PAGE_LIMIT)) ? AppConstant.DEFAULT_PAGE_LIMIT :getRequest().getParameter(AppConstant.PAGE_LIMIT);
    }

    public static int getPage(){
        return Integer.parseInt(getRequest().getParameter(AppConstant.PAGE) != null ? getRequest().getParameter(AppConstant.PAGE) : "1");
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

    public static String getSourceURL() {
    	return getRequest().getRequestURL().toString();
    }

    @SuppressWarnings("unchecked")
	public static void setAccessTokenClaim(OAuth2Authentication authentication) {
    	Map<String, Object> claims = (Map<String, Object>) authentication.getDetails();
    	getRequest().setAttribute(ACCESS_TOKEN_CLAIM, claims);
    }
    
    @SuppressWarnings("unchecked")
	public static String getAccessTokenClaim(String claim) {
    	Map<String, Object> claims = (Map<String, Object>)getRequest().getAttribute(ACCESS_TOKEN_CLAIM);
    	String claimValue = "";
    	if (claims.containsKey(claim)) {
    		claimValue = String.valueOf(claims.get(claim));
    	}
    	return claimValue;
    }
    
    public static void setToken(String token) {
    	getRequest().setAttribute(ACCESS_TOKEN, token);
    }
    
    public static String getToken() {
    	return (String) getRequest().getAttribute(ACCESS_TOKEN);
    }
    
}