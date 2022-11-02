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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
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

    public MResults<Event> getCalendarView(String userId, String calendarId, @Nullable String top, @Nullable String start, @Nullable String end) throws JsonProcessingException {
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
}
