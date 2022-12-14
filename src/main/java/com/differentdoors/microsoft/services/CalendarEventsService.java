package com.differentdoors.microsoft.services;

import com.differentdoors.microsoft.models.MResults;
import com.differentdoors.microsoft.models.event.Event;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class CalendarEventsService {
    @Value("${different_doors.microsoft.url}")
    private String URL;

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();

    @Autowired
    @Qualifier("Microsoft")
    private RestTemplate restTemplate;

    @Retryable(value = ResourceAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public MResults<Event> getCalendarView(String userId, String calendarId, @Nullable String top, @Nullable String start, @Nullable String end) throws Exception {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("path", "users/" + userId + "/calendars/" + calendarId + "/calendarView");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL);

        if (top != null) {
            builder.queryParam("top", top);
        }
        if (start != null) {
            builder.queryParam("startdatetime", start);
        }
        if (end != null) {
            builder.queryParam("enddatetime", end);
        }
        return objectMapper.readValue(restTemplate.getForObject(builder.buildAndExpand(urlParams).toUri(), String.class), new TypeReference<MResults<Event>>() {
        });
    }

    @Retryable(value = ResourceAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public MResults<Event> getEvents(String userId, String calendarId) throws Exception {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("path", "users/" + userId + "/calendars/" + calendarId + "/events");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL);

        return objectMapper.readValue(restTemplate.getForObject(builder.buildAndExpand(urlParams).toUri(), String.class), new TypeReference<MResults<Event>>() {
        });
    }

    @Retryable(value = ResourceAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Event getEvent(String userId, String calendarId, String eventId) throws Exception {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("path", "users/" + userId + "/calendars/" + calendarId + "/events/" + eventId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL);

        return objectMapper.readValue(restTemplate.getForObject(builder.buildAndExpand(urlParams).toUri(), String.class), Event.class);
    }

    public Event createEvent(String userId, String calendarId, Event event) throws Exception {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("path", "users/" + userId + "/calendars/" + calendarId + "/events");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity <Object> entity = new HttpEntity<>(objectMapper.writeValueAsString(event), headers);

        return objectMapper.readValue(restTemplate.postForObject(builder.buildAndExpand(urlParams).toUri(), entity, String.class), Event.class);
    }

    @Retryable(value = ResourceAccessException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Event updateEvent(String userId, String calendarId, String eventId, Event event) throws Exception {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("path", "users/" + userId + "/calendars/" + calendarId + "/events/" + eventId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(event), headers);

        return objectMapper.readValue(restTemplate.patchForObject(builder.buildAndExpand(urlParams).toUri(), requestEntity, String.class), Event.class);
    }

    @Recover
    public RetryException recover(Exception t){
        return new RetryException("Maximum retries reached: " + t.getMessage());
    }
}
