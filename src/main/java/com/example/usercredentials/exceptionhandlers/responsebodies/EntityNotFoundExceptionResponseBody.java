package com.example.usercredentials.exceptionhandlers.responsebodies;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityNotFoundExceptionResponseBody {
    @JsonProperty("timestamp")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateTime;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("message")
    private String errorMessage;

    public EntityNotFoundExceptionResponseBody(HttpStatusCode status, RuntimeException exception) {
        this.dateTime = LocalDateTime.now();
        this.status = status.value();
        this.errorMessage = exception.getMessage();
    }
}
