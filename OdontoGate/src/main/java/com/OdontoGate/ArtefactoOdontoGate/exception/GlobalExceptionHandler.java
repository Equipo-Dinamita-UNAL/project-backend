package com.OdontoGate.ArtefactoOdontoGate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_KEY = "error";

        @ExceptionHandler(MedicalRecordExceptions.NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(
            MedicalRecordExceptions.NotFoundException ex) {

        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

        @ExceptionHandler(MedicalRecordExceptions.ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MedicalRecordExceptions.ValidationException ex) {

        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

        @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
            ex.printStackTrace();  // ← agrega esta línea

        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, "Error interno del servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

        @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 400);
        error.put("descripcion", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

        @ExceptionHandler(PaymentExceptions.PaymentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePaymentNotFound(
            PaymentExceptions.PaymentNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

        @ExceptionHandler(PaymentExceptions.AppointmentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAppointmentNotFound(
            PaymentExceptions.AppointmentNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

        @ExceptionHandler(PaymentExceptions.PaymentAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handlePaymentAlreadyExists(
            PaymentExceptions.PaymentAlreadyExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

        @ExceptionHandler(PaymentExceptions.InvalidAmountException.class)
    public ResponseEntity<Map<String, String>> handleInvalidAmount(
            PaymentExceptions.InvalidAmountException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

        @ExceptionHandler(ReceiptExceptions.PaymentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleReceiptPaymentNotFound(
            ReceiptExceptions.PaymentNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

        @ExceptionHandler(ReceiptExceptions.PaymentNotPaidException.class)
    public ResponseEntity<Map<String, String>> handlePaymentNotPaid(
            ReceiptExceptions.PaymentNotPaidException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

        @ExceptionHandler(ReceiptExceptions.ReceiptAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleReceiptAlreadyExists(
            ReceiptExceptions.ReceiptAlreadyExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

        @ExceptionHandler(AppointmentExceptions.InvalidDateException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDate(
            AppointmentExceptions.InvalidDateException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

        @ExceptionHandler(AppointmentExceptions.ScheduleNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleScheduleNotFound(
            AppointmentExceptions.ScheduleNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

        @ExceptionHandler(AppointmentExceptions.ScheduleNotAvailableException.class)
    public ResponseEntity<Map<String, String>> handleScheduleNotAvailable(
            AppointmentExceptions.ScheduleNotAvailableException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

        @ExceptionHandler(AppointmentExceptions.DoctorConflictException.class)
    public ResponseEntity<Map<String, String>> handleDoctorConflict(
            AppointmentExceptions.DoctorConflictException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

        @ExceptionHandler(ScheduleExceptions.InvalidScheduleTimeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidScheduleTime(
            ScheduleExceptions.InvalidScheduleTimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
<<<<<<< HEAD
    @ExceptionHandler(MaxUploadSizeExceededException.class)
public ResponseEntity<Map<String, String>> handleMaxUploadSize(
        MaxUploadSizeExceededException ex) {
    Map<String, String> error = new HashMap<>();
    error.put(ERROR_KEY, "El archivo excede el tamaño máximo permitido de 10 MB");
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(error);
}

@ExceptionHandler(MedicalDocumentExceptions.NotFoundException.class)
public ResponseEntity<Map<String, String>> handleDocumentNotFound(
        MedicalDocumentExceptions.NotFoundException ex) {
    Map<String, String> error = new HashMap<>();
    error.put(ERROR_KEY, ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
}

@ExceptionHandler(MedicalDocumentExceptions.InvalidFileTypeException.class)
public ResponseEntity<Map<String, String>> handleInvalidFileType(
        MedicalDocumentExceptions.InvalidFileTypeException ex) {
    Map<String, String> error = new HashMap<>();
    error.put(ERROR_KEY, ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error);
}

@ExceptionHandler(MedicalDocumentExceptions.FileSizeExceededException.class)
public ResponseEntity<Map<String, String>> handleFileSizeExceeded(
        MedicalDocumentExceptions.FileSizeExceededException ex) {
    Map<String, String> error = new HashMap<>();
    error.put(ERROR_KEY, ex.getMessage());
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(error);
}

@ExceptionHandler(MedicalDocumentExceptions.FileStorageException.class)
public ResponseEntity<Map<String, String>> handleFileStorage(
        MedicalDocumentExceptions.FileStorageException ex) {
    Map<String, String> error = new HashMap<>();
    error.put(ERROR_KEY, ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
}

@ExceptionHandler(MedicalDocumentExceptions.PatientNotOwnerException.class)
public ResponseEntity<Map<String, String>> handleDocumentNotOwner(
        MedicalDocumentExceptions.PatientNotOwnerException ex) {
    Map<String, String> error = new HashMap<>();
    error.put(ERROR_KEY, ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
}
    
=======

    @ExceptionHandler(MercadoPagoIntegrationException.class)
    public ResponseEntity<Map<String, String>> handleMercadoPagoException(MercadoPagoIntegrationException ex) {
        Map<String, String> response = new HashMap<>();

        response.put(ERROR_KEY, "Error en la pasarela de pagos");
        response.put("message", ex.getMessage());

        // Retorna un HTTP 503 (Service Unavailable)
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
>>>>>>> 035b10f29ab68d8085dca60acb95ec26e1643a45
}