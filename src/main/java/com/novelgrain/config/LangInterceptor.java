package com.novelgrain.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;

public class LangInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String lang = request.getParameter("lang");
        if (lang == null || lang.isBlank()) {
            lang = request.getHeader("Lang");
        }
        if (lang == null || lang.isBlank()) {
            lang = request.getHeader("Accept-Language");
        }
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        if (lang != null && !lang.isBlank()) {
            lang = lang.split(",")[0];
            if (lang.toLowerCase().startsWith("en")) {
                locale = Locale.ENGLISH;
            } else if (lang.toLowerCase().startsWith("zh")) {
                locale = Locale.SIMPLIFIED_CHINESE;
            }
        }
        LocaleContextHolder.setLocale(locale);
        response.setHeader("Content-Language", locale.toLanguageTag());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        LocaleContextHolder.resetLocaleContext();
    }
}
