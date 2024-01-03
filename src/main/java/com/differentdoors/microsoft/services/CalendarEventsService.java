package com.differentdoors.microsoft.services;

import com.differentdoors.microsoft.exceptions.CustomResponseErrorHandler;
import com.differentdoors.microsoft.models.MResults;
import com.differentdoors.microsoft.models.event.Event;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
public class CalendarEventsService {
    @Autowired
    @Qualifier("Microsoft")
    private WebClient webClient;

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();

    public MResults<Object> getCalendars(String userId) throws Exception {
        return objectMapper.readValue(webClient.get()
                .uri("users/" + userId + "/calendars")
                .retrieve()
                .bodyToMono(String.class)
                .block(), new TypeReference<>() {
        });
    }
    public MResults<Event> getCalendarView(String userId, String calendarId, @Nullable String top, @Nullable String start, @Nullable String end) throws Exception {
        return objectMapper.readValue(webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("users/" + userId + "/calendars/" + calendarId + "/calendarView")
                        .queryParam("top", top)
                        .queryParam("startdatetime", start)
                        .queryParam("enddatetime", end)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block(), new TypeReference<>() {
        });
    }

    public MResults<Event> getEvents(String userId, String calendarId) throws Exception {
        return objectMapper.readValue(webClient.get()
                .uri("users/" + userId + "/calendars/" + calendarId + "/events")
                .retrieve()
                .bodyToMono(String.class)
                .block(), new TypeReference<>() {
        });
    }

    public Event getEvent(String userId, String calendarId, String eventId) throws Exception {
        return objectMapper.readValue(webClient.get()
                .uri("users/" + userId + "/calendars/" + calendarId + "/events/" + eventId)
                .retrieve()
                .bodyToMono(String.class)
                .block(), new TypeReference<>() {
        });
    }

    public String createEvent(String userId, String calendarId, Event event) throws Exception {
        return webClient.post()
                .uri("users/" + userId + "/calendars/" + calendarId + "/events")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(objectMapper.writeValueAsString(event)), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(Mono::error)
                .block();
    }

    public Event updateEvent(String userId, String calendarId, String eventId, Event event) throws Exception {
        return webClient.patch()
                .uri("users/" + userId + "/calendars/" + calendarId + "/events/" + eventId)
                .body(objectMapper.writeValueAsString(event), String.class)
                .retrieve()
                .bodyToMono(Event.class)
                .block();
    }
}
