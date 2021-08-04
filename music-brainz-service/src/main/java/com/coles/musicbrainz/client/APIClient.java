package com.coles.musicbrainz.client;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.coles.musicbrainz.exceptions.CustomBadRequestException;
import com.coles.musicbrainz.exceptions.CustomNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class APIClient {

    protected Response operation(HttpMethod httpMethod, Object entity, WebTarget webTarget,
                                 MultivaluedMap<String, Object> headers, boolean isThrowExceptionOnResponseStatus) {
        Response response = null;
        switch (httpMethod) {
            case GET:
                response = webTarget.request(MediaType.APPLICATION_JSON_VALUE).headers(headers).get();
                break;
            case PATCH:
                response = webTarget.request().headers(headers)
                        .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                        .build("PATCH", Entity.entity(entity, MediaType.APPLICATION_JSON_VALUE)).invoke();
                break;
            case PUT:
                response = webTarget.request(MediaType.APPLICATION_JSON_VALUE).headers(headers)
                        .put(Entity.entity(entity, MediaType.APPLICATION_JSON_VALUE));
                break;
            case POST:
                response = webTarget.request(MediaType.APPLICATION_JSON_VALUE).headers(headers)
                        .post(Entity.entity(entity, MediaType.APPLICATION_JSON_VALUE));
                break;
            case DELETE:
                response = webTarget.request(MediaType.APPLICATION_JSON_VALUE).headers(headers).delete();
                break;
            default:
                return null;
        }

        response.bufferEntity();

        if (isThrowExceptionOnResponseStatus) {
            checkAndThrowExceptionBaseOnResponseStatus(response);
        }

        return response;
    }

    protected void checkAndThrowExceptionBaseOnResponseStatus(Response response) {
        HttpStatus status = HttpStatus.valueOf(response.getStatus());
        if (status.isError()) {
            String errorMessage = "";
            ResponseExceptionPayload responseEx = null;
            String responseJsonString = response.readEntity(String.class);
            if (isJsonFormat(responseJsonString)) {
                responseEx = response.readEntity(ResponseExceptionPayload.class);
            } else {
                errorMessage = responseJsonString;
            }

            if (responseEx != null) {
                errorMessage = String.format("%s : %s", responseEx.getError(), responseEx.getMessage());
            }
            log.warn(errorMessage);
            if (status == HttpStatus.NOT_FOUND) {
                throw new CustomNotFoundException(errorMessage);
            }
            if (status.is5xxServerError() || status.is4xxClientError()) {
                throw new CustomBadRequestException(errorMessage);
            }
        }
    }
    public static boolean isJsonFormat(String value) {
        try {
            new ObjectMapper().readValue(value, Object.class);
            return true;
        } catch (JsonProcessingException ex) {
        	throw new CustomBadRequestException(ex.toString());
        }
    }

    protected Response operation(HttpMethod httpMethod, Object entity, WebTarget webTarget) {
        return operation(httpMethod, entity, webTarget, getDefaultHeaders(), false);
    }

    protected Response operation(HttpMethod httpMethod, Object entity, WebTarget webTarget, boolean isThrowExceptionOnResponseStatus) {
        return operation(httpMethod, entity, webTarget, getDefaultHeaders(), isThrowExceptionOnResponseStatus);
    }

    protected MultivaluedMap<String, Object> getDefaultHeaders() {
        MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.putSingle("Accept", "application/json");
        return headers;
    }

   
    @Data
    private static final class ResponseExceptionPayload {
        private String timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
    }
}
