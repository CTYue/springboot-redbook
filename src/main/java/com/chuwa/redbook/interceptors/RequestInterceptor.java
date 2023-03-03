package com.chuwa.redbook.interceptors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.EnumerationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author b1go
 * @date 3/3/23 9:55 AM
 */
@Component
@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            final Object handler
            ) throws Exception {
        corsHeaders(request, response);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private void corsHeaders(final HttpServletRequest request, final HttpServletResponse response) {
        response.setHeader("Strict-Transport-Security", "max-age=60; includeSubDomains ");
        if (Objects.nonNull(request.getHeader("Origin"))) {
            final List<String> list = EnumerationUtils.toList(request.getHeaders("Origin"));
            final String[] array = list.toArray(new String[CollectionUtils.size(list)]);
            response.setHeader("Access-Control-Allow-Origin", String.join(",", array));
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader(
                "Access-Control-Allow-Methods",
                String.join(
                        ",",
                        Stream.of(HttpMethod.values())
                                .map(HttpMethod::name)
                                .collect(Collectors.toList())
                                .toArray(new String[HttpMethod.values().length])
                )
        );
        response.addHeader(
                "Access-Control-Allow-Headers",
                "X-Requested-With, Origin, Content0Type, Accept, responseType, Authorization, x-xsrf-token, jsessionid"
        );
    }
}
