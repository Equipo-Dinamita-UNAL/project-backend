package com.OdontoGate.ArtefactoOdontoGate.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PdfService {
    private final TemplateEngine templateEngine;

    // Un metodo único para to el proyecto
    public byte[] generatePdf(String templateName, Map<String, Object> data) throws Exception {

        // Thymeleaf acepta mapas genéricos directamente, ¡es una maravilla!
        Context context = new Context();
        context.setVariables(data);

        // Rellena la plantilla que le pidas ("recibo", "receta", etc.)
        String htmlContent = templateEngine.process(templateName, context);

        // OpenHTMLToPDF hace su magia
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(htmlContent, null);
        builder.toStream(outputStream);
        builder.run();

        return outputStream.toByteArray();
    }
}
