package com.fullstack.starter.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class XssUtils {

    private static final Safelist CONTENT_SAFELIST;

    static {
        CONTENT_SAFELIST = Safelist.basic()
                .addTags("img", "h1", "h2", "h3", "h4", "h5", "h6", "pre", "code", "blockquote", "span", "div", "hr")
                .addAttributes("img", "src", "alt", "width", "height", "style")
                .addAttributes("a", "target", "rel")
                .addAttributes("code", "class")
                .addAttributes("pre", "class")
                .addAttributes("span", "class", "style")
                .addAttributes("div", "class", "style")
                .addEnforcedAttribute("a", "rel", "nofollow noopener")
                .addProtocols("img", "src", "data", "http", "https");
    }

    public static String sanitize(String html) {
        if (html == null || html.isBlank()) {
            return html;
        }
        return Jsoup.clean(html, CONTENT_SAFELIST);
    }
}
