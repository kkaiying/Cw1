package uk.ac.ed.acp.cw2.dto;

import jakarta.validation.constraints.NotNull;

public class QueryDTO {
    @NotNull
    public String attribute;
    @NotNull
    public String operator;
    @NotNull
    public String value;
}
