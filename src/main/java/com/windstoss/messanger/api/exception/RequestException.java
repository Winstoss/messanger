package com.windstoss.messanger.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class RequestException {
    String message;
    ZonedDateTime timestamp;
}
