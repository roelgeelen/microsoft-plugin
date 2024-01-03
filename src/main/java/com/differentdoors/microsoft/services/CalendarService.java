package com.differentdoors.microsoft.services;

import com.differentdoors.microsoft.models.MResults;
import com.differentdoors.microsoft.models.calendar.Calendar;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CalendarService {
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();

    @Autowired
    @Qualifier("Microsoft")
    private WebClient webClient;

//    @Scheduled(fixedRate = 300000)
//    public MResults<Calendar> getCalendars(String userId) throws Exception {
//        return objectMapper.readValue(webClient.get()
//                .uri("users/" + userId + "/calendars/")
//                .retrieve()
//                .bodyToMono(String.class)
//                .block(), new TypeReference<>() {
//        });
//    }

    public Calendar getCalendar(String userId, String calendarId) throws Exception {
        return objectMapper.readValue(webClient.get()
                .uri("users/" + userId + "/calendars/" + calendarId)
                .retrieve()
                .bodyToMono(String.class)
                .block(), new TypeReference<>() {
        });
    }
}
