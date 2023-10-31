package ru.otus.andrk.i18n;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.springframework.web.server.i18n.LocaleContextResolver;
import ru.otus.andrk.config.LibraryConfig;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Component(WebHttpHandlerBuilder.LOCALE_CONTEXT_RESOLVER_BEAN_NAME)
@Log4j2
public class CustomLocaleContextResolver implements LocaleContextResolver {

    private final LibraryConfig config;

    @Override
    public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
        String lang = config.getDefaultLang();
        String langMsg = "use default lang=";
        List<String> referLang = exchange.getRequest().getQueryParams().get("lang");
        if (referLang != null && !referLang.isEmpty()) {
            lang = referLang.get(0);
            langMsg = "save lang to cookie, lang=";
            exchange.getResponse().addCookie(ResponseCookie.from("lang").value(lang)
                    .build());
        } else {
            var langCookies = exchange.getRequest().getCookies().get("lang");
            if (langCookies != null && langCookies.size() > 0) {
                lang = langCookies.get(0).getValue();
                langMsg = "receive lang from cookie, lang=";
            }
        }
        log.debug("request path={}, {}{}", exchange.getRequest().getPath().value(), langMsg, lang);
        Locale targetLocale = Locale.forLanguageTag(lang);
        return new SimpleLocaleContext(targetLocale);
    }

    @Override
    public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {

    }
}
