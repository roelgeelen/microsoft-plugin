package com.differentdoors.microsoft.models.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Event {
    private String id;
    @JsonProperty("isAllDay")
    private boolean isAllDay;
    @JsonProperty("isCancelled")
    private boolean isCancelled;
    private String subject;
    private EventBody body;
    private EventTime start;
    private EventTime end;
    private EventLocation location;
    private EventOrganizer organizer;
    private List<Attendee> attendees;
    private String createdDateTime;
    private String lastModifiedDateTime;
    private List<String> categories = new ArrayList<>();
    private String originalStartTimeZone;
    private String originalEndTimeZone;
    private String importance;
    private String showAs;
    private String transactionId;
    private List<EventLocation> locations;
    private double distance;

    public void addCategory(String category) {
        this.categories.add(category);
    }
}
