package ru.enterix.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class RegistryExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ModelAndView handeException(SubscriberNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Subscriber not found");
        modelAndView.addObject("baseUrl", ex.getBaseUrl());
        modelAndView.addObject("phone", ex.getPhone());
        return modelAndView;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ModelAndView handeException(BadSubscribersException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Subscriber validation failed");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

}
