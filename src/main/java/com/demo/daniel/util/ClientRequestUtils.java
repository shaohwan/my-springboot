package com.demo.daniel.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@UtilityClass
@Slf4j
public class ClientRequestUtils {

    private static Searcher ip2regionSearcher;

    static {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource(AppConstants.IP2REGION_DB_PATH);
        try {
            ip2regionSearcher = Searcher.newWithBuffer(resource.getContentAsByteArray());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        throw new IllegalStateException("No current HttpServletRequest found");
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            ip = ip.split(",")[0].trim();
        } else {
            ip = request.getHeader("X-Real-IP");
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        }

        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    private static boolean isPrivateIp(String ip) {
        if (ip == null || ip.isEmpty() || "127.0.0.1".equals(ip)) {
            return true;
        }
        if (ip.contains(":")) {
            return false;
        }
        try {
            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }
            int first = Integer.parseInt(parts[0]);
            int second = Integer.parseInt(parts[1]);
            return (first == 10) ||
                    (first == 172 && second >= 16 && second <= 31) ||
                    (first == 192 && second == 168);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getLocationByIp(String ip) {
        if (isPrivateIp(ip)) {
            return "127.0.0.1".equals(ip) ? "localhost" : "intranet";
        }

        if (ip2regionSearcher == null) {
            return "Unknown location";
        }

        try {
            String location = ip2regionSearcher.search(ip);
            if (location == null || location.isEmpty()) {
                return "Unknown location";
            }
            return location;
        } catch (Exception e) {
            return "Unknown location";
        }
    }

    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? userAgent : "Unknown";
    }
}
