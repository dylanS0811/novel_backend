package com.novelgrain.common;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Messages implements MessageSourceAware {
    private static MessageSource source;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        source = messageSource;
    }

    public static String tr(String code) {
        if (code == null) {
            return null;
        }
        if (source == null) {
            return code;
        }
        return source.getMessage(code, null, code, LocaleContextHolder.getLocale());
    }
}
