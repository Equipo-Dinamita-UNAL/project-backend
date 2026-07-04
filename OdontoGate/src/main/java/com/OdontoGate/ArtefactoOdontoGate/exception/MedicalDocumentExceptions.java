package com.OdontoGate.ArtefactoOdontoGate.exception;

public class MedicalDocumentExceptions {

    private MedicalDocumentExceptions() {}

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(Integer id) {
            super("Documento médico no encontrado con id: " + id);
        }
    }

    public static class InvalidFileTypeException extends RuntimeException {
        public InvalidFileTypeException(String fileType) {
            super("Tipo de archivo no permitido: " + fileType
                    + ". Solo se permiten: PDF, JPG, JPEG, PNG, DICOM");
        }
    }

    public static class FileSizeExceededException extends RuntimeException {
        public FileSizeExceededException() {
            super("El archivo excede el tamaño máximo permitido de 10 MB");
        }
    }

    public static class FileStorageException extends RuntimeException {
        public FileStorageException(String message) {
            super("Error al almacenar el archivo: " + message);
        }
    }

    public static class PatientNotOwnerException extends RuntimeException {
        public PatientNotOwnerException() {
            super("No tienes permiso para acceder a este documento");
        }
    }
}