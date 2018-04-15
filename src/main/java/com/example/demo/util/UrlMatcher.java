package com.example.demo.util;

import org.assertj.core.util.Sets;
import org.springframework.util.AntPathMatcher;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by liulanhua on 2018/3/19.
 */
public class UrlMatcher {

    public static final Set<String> STATIC_URL_SUFFIX_LIST = (Set) Sets.newLinkedHashSet(
            new String[]{"js", "gif", "jpg", "jpeg", "ico", "css"}).stream().map((x) -> {
        return "." + x;
    }).collect(Collectors.toSet());
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    private Set<String> antUrls = new LinkedHashSet();
    private Set<String> suffixUrls = new LinkedHashSet();

    public UrlMatcher(Set<String> antUrls) {
        this.antUrls = antUrls;
    }

    public UrlMatcher(Set<String> antUrls, Set<String> suffixUrls) {
        this.antUrls = antUrls;
        this.suffixUrls = suffixUrls;
    }

    public boolean ignore(String url) {
        return this.suffixUrls.stream().anyMatch(url::endsWith) || this.antUrls.stream().anyMatch((ignoreUrl) -> {
            return this.pathMatcher.match(ignoreUrl, url);
        });

    }

}
