package com.chuwa.redbook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerInternalException extends RuntimeException {
 public ServerInternalException(String message) {
     super("Server Internal Error: " + message);
 }
}
