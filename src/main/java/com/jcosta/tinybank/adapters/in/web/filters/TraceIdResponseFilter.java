package com.jcosta.tinybank.adapters.in.web.filters;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
//import io.opentelemetry.api.trace.Tracer;

import java.io.IOException;


// TODO: remove
@Provider
public class TraceIdResponseFilter implements ContainerResponseFilter {
//    @Inject
//    Tracer tracer;
//
//    private static final String TRACE_ID = "traceId";
//
    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) throws IOException {
//        // Get the current span
//        Span currentSpan = Span.current();
//
//        // Retrieve the traceId from the span context
//        SpanContext spanContext = currentSpan.getSpanContext();
//
//        if (spanContext.isValid()) {
//            String traceId = spanContext.getTraceId();
//
//            // Add the traceId to the response headers
//            responseContext.getHeaders().add("X-Trace-Id", traceId);
//        }
    }

}