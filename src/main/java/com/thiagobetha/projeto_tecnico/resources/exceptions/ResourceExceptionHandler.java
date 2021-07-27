package com.thiagobetha.projeto_tecnico.resources.exceptions;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.thiagobetha.projeto_tecnico.services.exceptions.DataIntegrityException;
import com.thiagobetha.projeto_tecnico.services.exceptions.InvalidSituationException;
import com.thiagobetha.projeto_tecnico.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException resError, HttpServletRequest request){
		StandardError err = new StandardError(
				HttpStatus.NOT_FOUND.value(), 
				resError.getMessage(), 
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrityViolation(DataIntegrityException resError, HttpServletRequest request){
		StandardError err = new StandardError(
				HttpStatus.CONFLICT.value(), 
				resError.getMessage(), 
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException resError, HttpServletRequest request){
		ValidationError err = new ValidationError(
				HttpStatus.BAD_REQUEST.value(), 
				"Erro de Validação", 
				LocalDateTime.now());
		
		for(FieldError fieldErr : resError.getBindingResult().getFieldErrors()) {
			err.addError(fieldErr.getField(), fieldErr.getDefaultMessage());
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<StandardError> validation(ConstraintViolationException resError, HttpServletRequest request){
		ValidationError err = new ValidationError(
				HttpStatus.BAD_REQUEST.value(), 
				"Erro de Validação", 
				LocalDateTime.now());
		
		for(ConstraintViolation<?> fieldErr : resError.getConstraintViolations()) {
			err.addError(fieldErr.getPropertyPath().toString(), fieldErr.getMessage());
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<StandardError> illegalArgument(IllegalArgumentException resError, HttpServletRequest request){
		StandardError err = new StandardError(
				HttpStatus.BAD_REQUEST.value(), 
				resError.getMessage(), 
				LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<StandardError> missingReqParameter(
			MissingServletRequestParameterException resError, 
			HttpServletRequest request){
		
		StandardError err = new StandardError(
				HttpStatus.BAD_REQUEST.value(), 
				resError.getMessage(), 
				LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(InvalidSituationException.class)
	public ResponseEntity<StandardError> invalidSituation(InvalidSituationException resError, HttpServletRequest request){
		StandardError err = new StandardError(
				HttpStatus.BAD_REQUEST.value(), 
				resError.getMessage(), 
				LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<StandardError> accessDenied(AccessDeniedException resError, HttpServletRequest request){
		StandardError err = new StandardError(
				HttpStatus.FORBIDDEN.value(), 
				resError.getMessage(), 
				LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
	}
}