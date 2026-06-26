package com.OdontoGate.ArtefactoOdontoGate.controller;

import com.OdontoGate.ArtefactoOdontoGate.dto.MedicalRecord.Responses.MedicalDocumentResponse;
import com.OdontoGate.ArtefactoOdontoGate.service.MedicalDocumentService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/medical-documents")
public class MedicalDocumentController {

    private final MedicalDocumentService service;

    public MedicalDocumentController(MedicalDocumentService service) {
        this.service = service;
    }

    /**
     * POST /api/medical-documents
     * Sube un archivo médico. Solo DOCTOR o ADMINISTRATOR.
     * Parámetros multipart:
     *   - file        : archivo a subir (obligatorio)
     *   - patientId   : ID del paciente (obligatorio)
     *   - medicalRecordId : ID de la historia clínica (opcional)
     *   - description : descripción del documento (opcional)
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasAuthority('DOCTOR_CREAR_HISTORIA_CLINICA')")
    public MedicalDocumentResponse upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("patientId") Integer patientId,
            @RequestParam(value = "medicalRecordId", required = false) Integer medicalRecordId,
            @RequestParam(value = "description", required = false) String description) {

        Integer uploadedBy = getCurrentUserId();
        return service.upload(file, patientId, medicalRecordId, description, uploadedBy);
    }

    /**
     * GET /api/medical-documents/patient/{patientId}
     * Consulta todos los documentos de un paciente.
     * - PATIENT: solo puede ver los suyos propios.
     * - DOCTOR / ADMINISTRATOR: puede ver los de cualquier paciente.
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') "
            + "or hasAuthority('DOCTOR_LEER_HISTORIA_CLINICA') "
            + "or hasAuthority('PATIENT_LEER_CITA')")
    public List<MedicalDocumentResponse> findByPatient(@PathVariable Integer patientId) {
        Integer requestingUserId = getCurrentUserId();
        String requestingUserRole = getCurrentUserRole();
        return service.findByPatient(patientId, requestingUserId, requestingUserRole);
    }

    /**
     * GET /api/medical-documents/medical-record/{medicalRecordId}
     * Retorna todos los documentos asociados a una historia clínica.
     * Solo DOCTOR o ADMINISTRATOR.
     */
    @GetMapping("/medical-record/{medicalRecordId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasAuthority('DOCTOR_LEER_HISTORIA_CLINICA')")
    public List<MedicalDocumentResponse> findByMedicalRecord(
            @PathVariable Integer medicalRecordId) {
        return service.findByMedicalRecord(medicalRecordId);
    }

    /**
     * GET /api/medical-documents/{id}
     * Retorna los metadatos de un documento.
     * El paciente solo puede ver documentos que le pertenecen.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR') "
            + "or hasAuthority('DOCTOR_LEER_HISTORIA_CLINICA') "
            + "or hasAuthority('PATIENT_LEER_CITA')")
    public MedicalDocumentResponse findById(@PathVariable Integer id) {
        Integer requestingUserId = getCurrentUserId();
        String requestingUserRole = getCurrentUserRole();
        return service.findById(id, requestingUserId, requestingUserRole);
    }

    /**
     * GET /api/medical-documents/{id}/download
     * Descarga el archivo físico.
     * El paciente solo puede descargar documentos que le pertenecen.
     */
    @GetMapping("/{id}/download")
    @PreAuthorize("hasRole('ADMINISTRATOR') "
            + "or hasAuthority('DOCTOR_LEER_HISTORIA_CLINICA') "
            + "or hasAuthority('PATIENT_LEER_CITA')")
    public ResponseEntity<Resource> download(@PathVariable Integer id) {
        Integer requestingUserId = getCurrentUserId();
        String requestingUserRole = getCurrentUserRole();

        String filePath = service.getFilePath(id, requestingUserId, requestingUserRole);
        MedicalDocumentResponse meta = service.findById(id, requestingUserId, requestingUserRole);

        try {
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(meta.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + meta.getOriginalName() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * DELETE /api/medical-documents/{id}
     * Elimina un documento. Solo ADMINISTRATOR o DOCTOR.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasAuthority('DOCTOR_MODIFICAR_HISTORIA_CLINICA')")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    // ── helpers de seguridad ─────────────────────────────────────────────────

    /**
     * Extrae el userId que está en los claims del JWT.
     * El JwtAuthenticationFilter guarda el userId como detalle de la autenticación.
     */
    private Integer getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object details = auth.getDetails();
        if (details instanceof Integer) {
            return (Integer) details;
        }
        // Fallback: intentar castear desde el principal (nombre = email → no sirve para ID)
        throw new IllegalStateException("No se pudo obtener el userId del token JWT");
    }

    private String getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.replace("ROLE_", "").toLowerCase())
                .findFirst()
                .orElse("");
    }
}