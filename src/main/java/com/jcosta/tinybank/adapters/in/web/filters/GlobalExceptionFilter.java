package com.jcosta.tinybank.adapters.in.web.filters;

import com.jcosta.tinybank.domain.exceptions.BusinessException;
import com.jcosta.tinybank.domain.exceptions.DomainException;
import com.jcosta.tinybank.domain.exceptions.ExceptionCode;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import static com.jcosta.tinybank.adapters.in.web.filters.WebErrorTypes.getWebErrorStatus;
import static com.jcosta.tinybank.adapters.in.web.filters.WebErrorTypes.getWebErrorType;
import static io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument.kv;

@Provider
public class GlobalExceptionFilter implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionFilter.class);

    @Override
    public Response toResponse(Exception exception) {
        return mapExceptionToResponse(exception);
    }

    private Response mapExceptionToResponse(Exception exception) {
        if (exception instanceof WebApplicationException) {
            // Overwrite error message
            Response originalErrorResponse = ((WebApplicationException) exception).getResponse();

            if(originalErrorResponse.getStatus() == 405) {
                return Response.status(originalErrorResponse.getStatus()).entity(
                        new ErrorResponse(
                                getWebErrorType(ExceptionCode.OPERATION_NOT_ALLOWED),
                                "the invoked method is not allowed",
                                null)).build();
            }

            return Response.fromResponse(originalErrorResponse)
                    .entity(exception.getMessage())
                    .build();
        } else if (exception instanceof DomainException domainException) {
            int statusCode = getWebErrorStatus(domainException.getCode());
            LOG.infof("Status Code: " + statusCode , kv("problem", domainException));
            return Response.status(statusCode).entity(
                    new ErrorResponse(
                            getWebErrorType(domainException.getCode()),
                            domainException.getMessage(),
                            domainException.getInvalidParams())).build();
        }else if (exception instanceof BusinessException businessException) {
            int statusCode = getWebErrorStatus(businessException.getCode());
            LOG.infof("Status Code: " + statusCode , kv("problem", businessException));
            return Response.status(statusCode).entity(
                    new ErrorResponse(
                            getWebErrorType(businessException.getCode()),
                            businessException.getMessage(),
                            null)).build();
        } else if (exception instanceof IllegalArgumentException) {
            return Response.status(400).entity(
                    new ErrorResponse(
                            getWebErrorType(ExceptionCode.VALIDATION_EXCEPTION),
                            "Illegal Operation",
                            null)).build();
        }
        else {
            LOG.fatalf(exception,
                    "Failed to process request");
            return Response.status(500).entity(
                    new ErrorResponse(
                            getWebErrorType(ExceptionCode.INTERNAL_SERVER_ERROR),
                            "Internal Server Error",
                            null)).build();
        }
    }
}