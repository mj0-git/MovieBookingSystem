package org.booking.system.DTO.MovieDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Rating {
    @JsonProperty("Source")
    private String source;
    @JsonProperty("Value")
    private String value;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap();
}
