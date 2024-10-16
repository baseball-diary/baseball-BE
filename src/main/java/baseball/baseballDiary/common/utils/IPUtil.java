package baseball.baseballDiary.common.utils;

import jakarta.servlet.http.HttpServletRequest;

public class IPUtil {
    public static final String[] HEADERS_TO_TRY = {
        "x-forwarded-for",
        "X-Forwarded-For",
        "X-REAL-IP",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR",
        "REMOTE-HOST"
    };

    public static String getClientIp(HttpServletRequest request) {
        String clientIp = _getClientIp(request);

        if (null != clientIp && !clientIp.trim().isEmpty()){
            return clientIp;
        }

        return request.getRemoteAddr();
    }


    private static String _getClientIp(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
