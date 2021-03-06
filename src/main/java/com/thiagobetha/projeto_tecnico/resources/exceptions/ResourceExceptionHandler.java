package com.thiagobetha.projeto_tecnico.resources.exceptions;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.thiagobetha.projeto_tecnico.services.exceptions.DataIntegrityException;
import com.thiagobetha.projeto_tecnico.services.exceptions.FileException;
import com.thiagobetha.projeto_tecnico.services.exceptions.InvalidSituationException;
import com.thiagobetha.projeto_tecnico.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException resError, HttpServletRequest request){
		StandardError err = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.NOT_FOUND.value(),
				"Não encontrado",
				resError.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrityViolation(DataIntegrityException resError, HttpServletRequest request){
		StandardError err = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.CONFLICT.value(),
				"Conflito de dados",
				resError.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException resError, HttpServletRequest request){
		ValidationError err = new ValidationError(
				LocalDateTime.now().toString(),
				HttpStatus.UNPROCESSABLE_ENTITY.value(),
				"Erro de validação",
				resError.getMessage(),
				request.getRequestURI());
		
		for(FieldError fieldErr : resError.getBindingResult().getFieldErrors()) {
			err.addError(fieldErr.getField(), fieldErr.getDefaultMessage());
		}
		
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<StandardError> validation(ConstraintViolationException resError, HttpServletRequest request){
		ValidationError err = new ValidationError(
				LocalDateTime.now().toString(),
				HttpStatus.UNPROCESSABLE_ENTITY.value(),
				"Erro de validação",
				resError.getMessage(),
				request.getRequestURI());
		
		for(ConstraintViolation<?> fieldErr : resError.getConstraintViolations()) {
			err.addError(fieldErr.getPropertyPath().toString(), fieldErr.getMessage());
		}
		
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<StandardError> illegalArgument(IllegalArgumentException resError, HttpServletRequest request){
		StandardError err = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.BAD_REQUEST.value(),
				"Bad request",
				resError.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<StandardError> missingReqParameter(
			MissingServletRequestParameterException resError, 
			HttpServletRequest request){
		
		StandardError err = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.BAD_REQUEST.value(),
				"Parametros da requisição faltando",
				resError.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(InvalidSituationException.class)
	public ResponseEntity<StandardError> invalidSituation(InvalidSituationException resError, HttpServletRequest request){
		StandardError err = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.BAD_REQUEST.value(),
				"Situacao invalida",
				resError.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<StandardError> accessDenied(AccessDeniedException resError, HttpServletRequest request){
		StandardError err = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.FORBIDDEN.value(),
				"Acesso negado",
				resError.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
	}
	
	@ExceptionHandler(FileException.class)
	public ResponseEntity<StandardError> file(FileException resError, HttpServletRequest request){
		StandardError err = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.BAD_REQUEST.value(),
				"Erro de arquivo",
				resError.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<StandardError> amazonService(AmazonServiceException resError, HttpServletRequest request){
		HttpStatus code = HttpStatus.valueOf(resError.getErrorCode());
		StandardError err = new StandardError(
				LocalDateTime.now().toString(),
				code.value(),
				"Erro Amazon Service",
				resError.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(code).body(err);
	}
	
	@ExceptionHandler(AmazonClientException.class)
	public ResponseEntity<StandardError> amazonClient(AmazonClientException resError, HttpServletRequest request){
		StandardError err = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.BAD_REQUEST.value(),
				"Erro Amazon Client",
				resError.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(AmazonS3Exception.class)
	public ResponseEntity<StandardError> amazonS3(AmazonS3Exception resError, HttpServletRequest request){
		StandardError err = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.BAD_REQUEST.value(),
				"Erro Amazon S3",
				resError.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<StandardError> reqMethodNotSupported(
			HttpRequestMethodNotSupportedException resError, 
			HttpServletRequest request){
		StandardError err = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.METHOD_NOT_ALLOWED.value(),
				"Método http não permitido",
				resError.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(err);
	}
}