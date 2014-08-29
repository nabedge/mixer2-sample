package org.mixer2.sample.config;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;

import org.springframework.boot.context.embedded.ServletContextInitializer;

/**
 * <p>
 * セッション情報をcookieに格納するときのcookie名の指定。<br />
 * urlrewritingによるセッション維持も無効にする。<br />
 * set-cookieヘッダにhttpOnly属性も付けるようにする
 * </p>
 * <p>
 * 通常のWebApplicationであればWEB-INF/web.xmlの
 * session-configタグやcookie-configタグを使って設定するのと等価。
 * </p>
 */
public class SessionTrackingConfigListener implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {
        SessionCookieConfig sessionCookieConfig = servletContext
                .getSessionCookieConfig();
        sessionCookieConfig.setHttpOnly(true);
        sessionCookieConfig.setName("SAMPLESESSIONID");
        Set<SessionTrackingMode> stmSet = new HashSet<SessionTrackingMode>();
        stmSet.add(SessionTrackingMode.COOKIE);
        servletContext.setSessionTrackingModes(stmSet);
    }
}
