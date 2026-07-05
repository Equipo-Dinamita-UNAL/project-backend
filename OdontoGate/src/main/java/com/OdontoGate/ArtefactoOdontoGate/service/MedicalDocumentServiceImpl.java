package com.OdontoGate.ArtefactoOdontoGate.service;

import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Responses.MedicalDocumentResponse;
import com.OdontoGate.ArtefactoOdontoGate.exception.MedicalDocumentExceptions;
import com.OdontoGate.ArtefactoOdontoGate.model.MedicalDocument;
import com.OdontoGate.ArtefactoOdontoGate.repository.MedicalDocumentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class MedicalDocumentServiceImpl implements MedicalDocumentService {

    private static final long MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024; // 10 MB
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/jpg",
            "image/png",
            "application/dicom"
    );

    private final MedicalDocumentRepository repository;
    private final Path storageLocation;

    public MedicalDocumentServiceImpl(
            MedicalDocumentRepository repository,
            @Value("${app.storage.upload-dir:uploads/medical-documents}") String uploadDir) {
        this.repository = repository;
        this.storageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @PostConstruct
    private void initStorage() {
        try {
            Files.createDirectories(storageLocation);
        } catch (IOException e) {
            throw new MedicalDocumentExceptions.FileStorageException(
                    "No se pudo crear el directorio de almacenamiento: " + e.getMessage());
        }
    }

    @Override
    public MedicalDocumentResponse upload(MultipartFile file,
                                          Integer patientId,
                                          Integer medicalRecordId,
                                          String description,
                                          Integer uploadedBy) {
        validateFile(file);

        String uniqueFileName = UUID.randomUUID() + "_" + sanitize(file.getOriginalFilename());
        Path targetPath = storageLocation.resolve(uniqueFileName);

        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new MedicalDocumentExceptions.FileStorageException(e.getMessage());
        }

        MedicalDocument doc = new MedicalDocument();
        doc.setPatientId(patientId);
        doc.setMedicalRecordId(medicalRecordId);
        doc.setFileName(uniqueFileName);
        doc.setOriginalName(file.getOriginalFilename());
        doc.setFileType(file.getContentType());
        doc.setFileSize(file.getSize());
        doc.setFilePath(targetPath.toString());
        doc.setUploadedBy(uploadedBy);
        doc.setDescription(description);

        return toResponse(repository.save(doc));
    }

    @Override
    public List<MedicalDocumentResponse> findByPatient(Integer patientId,
                                                       Integer requestingUserId,
                                                       String requestingUserRole) {
        if ("patient".equalsIgnoreCase(requestingUserRole)
                && !requestingUserId.equals(patientId)) {
            throw new MedicalDocumentExceptions.PatientNotOwnerException();
        }
        return repository.findByPatientId(patientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<MedicalDocumentResponse> findByMedicalRecord(Integer medicalRecordId) {
        return repository.findByMedicalRecordId(medicalRecordId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MedicalDocumentResponse findById(Integer id,
                                            Integer requestingUserId,
                                            String requestingUserRole) {
        MedicalDocument doc = getOrThrow(id);
        checkOwnership(doc, requestingUserId, requestingUserRole);
        return toResponse(doc);
    }

    @Override
    public void delete(Integer id) {
        MedicalDocument doc = getOrThrow(id);
        Path filePath = Paths.get(doc.getFilePath());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new MedicalDocumentExceptions.FileStorageException(
                    "No se pudo eliminar el archivo físico: " + e.getMessage());
        }
        repository.deleteById(id);
    }

    @Override
    public String getFilePath(Integer id,
                              Integer requestingUserId,
                              String requestingUserRole) {
        MedicalDocument doc = getOrThrow(id);
        checkOwnership(doc, requestingUserId, requestingUserRole);
        return doc.getFilePath();
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new MedicalDocumentExceptions.FileStorageException("El archivo está vacío");
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new MedicalDocumentExceptions.FileSizeExceededException();
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new MedicalDocumentExceptions.InvalidFileTypeException(file.getContentType());
        }
    }

    private MedicalDocument getOrThrow(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new MedicalDocumentExceptions.NotFoundException(id));
    }

    private void checkOwnership(MedicalDocument doc,
                                Integer requestingUserId,
                                String requestingUserRole) {
        if ("patient".equalsIgnoreCase(requestingUserRole)
                && !requestingUserId.equals(doc.getPatientId())) {
            throw new MedicalDocumentExceptions.PatientNotOwnerException();
        }
    }

    private String sanitize(String originalFilename) {
        if (originalFilename == null) {
            return "file";
        }
        return originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private MedicalDocumentResponse toResponse(MedicalDocument doc) {
        MedicalDocumentResponse response = new MedicalDocumentResponse();
        response.setId(doc.getId());
        response.setPatientId(doc.getPatientId());
        response.setMedicalRecordId(doc.getMedicalRecordId());
        response.setFileName(doc.getFileName());
        response.setOriginalName(doc.getOriginalName());
        response.setFileType(doc.getFileType());
        response.setFileSize(doc.getFileSize());
        response.setDescription(doc.getDescription());
        response.setCreatedAt(doc.getCreatedAt());
        return response;
    }
}
