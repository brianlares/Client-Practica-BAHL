package com.bahl.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Pokemon {

    private Integer id;

    private String is_default;

    private String location_area_encounters;

    @JsonProperty(value="moves")
    private List<Object> moves;

    private String name;

    private Integer order;

    @JsonProperty(value="past_abilities")
    private List<Object> past_abilities;

    @JsonProperty(value="past_types")
    private List<Object> past_types;

    @JsonProperty(value="species")
    private Object species;

    @JsonProperty(value="sprites")
    private Object sprites;

    @JsonProperty(value="stats")
    private List<Object> stats;

    @JsonProperty(value="types")
    private List<Object> types;

    private Integer weight;
}
