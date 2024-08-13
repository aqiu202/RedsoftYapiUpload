package com.github.aqiu202.ideayapi.constant;

/**
 * java 注解
 *
 * @author aqiu 2019/5/18 4:49 PM
 */
public interface ServletConstants {

    String HttpServletRequest = "javax.servlet.http.HttpServletRequest";

    String HttpServletResponse = "javax.servlet.http.HttpServletResponse";

    String HttpSession = "javax.servlet.http.HttpSession";

    String JakartaHttpServletRequest = "jakarta.servlet.http.HttpServletRequest";

    String JakartaHttpServletResponse = "jakarta.servlet.http.HttpServletResponse";

    String JakartaHttpSession = "jakarta.servlet.http.HttpSession";

    String[] HttpServletTypes = new String[]{HttpServletRequest, HttpServletResponse, HttpSession,
            JakartaHttpServletRequest, JakartaHttpServletResponse, JakartaHttpSession};

}
