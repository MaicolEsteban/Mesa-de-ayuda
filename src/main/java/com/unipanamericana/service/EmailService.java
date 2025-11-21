package com.unipanamericana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final String FROM_EMAIL = "ventas.klikajaco@gmail.com";
    private static final String APP_NAME = "Mesa Digital - Universidad Panamericana";

    @Autowired
    private JavaMailSender mailSender;

    // ========== ENVIAR EMAIL BÁSICO ==========

    public void enviarEmail(String destinatario, String asunto, String cuerpo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_EMAIL);
            message.setTo(destinatario);
            message.setSubject(asunto);
            message.setText(cuerpo);

            mailSender.send(message);
            logger.info("✅ Email enviado a: " + destinatario);
        } catch (Exception e) {
            logger.error(" Error al enviar email a " + destinatario + ": " + e.getMessage());
        }
    }

    // ========== ENVIAR EMAIL HTML ==========

    public void enviarEmailHTML(String destinatario, String asunto, String cuerpoHTML) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(FROM_EMAIL);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpoHTML, true); // true = HTML

            mailSender.send(message);
            logger.info("✅ Email HTML enviado a: " + destinatario);
        } catch (MessagingException e) {
            logger.error("❌ Error al enviar email HTML a " + destinatario + ": " + e.getMessage());
        }
    }

    // ========== EMAIL AL CREAR SOLICITUD ==========

    public void enviarEmailCreacionSolicitud(String emailEstudiante, String nombreEstudiante, String codigoSolicitud) {
        String asunto = "✅ Tu solicitud ha sido creada - " + codigoSolicitud;
        String html = generarHTMLCreacionSolicitud(nombreEstudiante, codigoSolicitud);
        enviarEmailHTML(emailEstudiante, asunto, html);
    }

    private String generarHTMLCreacionSolicitud(String nombre, String codigo) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset='UTF-8'>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; background-color: #f5f5f5; }\n" +
                "        .container { max-width: 600px; margin: 20px auto; background: white; padding: 30px; border-radius: 8px; }\n" +
                "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 5px; text-align: center; }\n" +
                "        .content { padding: 20px 0; }\n" +
                "        .info-box { background: #f0f4ff; padding: 15px; border-left: 4px solid #667eea; margin: 15px 0; }\n" +
                "        .button { display: inline-block; background: #667eea; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin-top: 10px; }\n" +
                "        .footer { color: #888; font-size: 12px; text-align: center; margin-top: 20px; border-top: 1px solid #eee; padding-top: 15px; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class='container'>\n" +
                "        <div class='header'>\n" +
                "            <h1>✅ Solicitud Creada</h1>\n" +
                "        </div>\n" +
                "        <div class='content'>\n" +
                "            <p>Hola <strong>" + nombre + "</strong>,</p>\n" +
                "            <p>¡Excelente! Tu solicitud ha sido creada exitosamente en nuestro sistema.</p>\n" +
                "            <div class='info-box'>\n" +
                "                <p><strong>📌 Código de Solicitud:</strong> " + codigo + "</p>\n" +
                "                <p><strong>📊 Estado:</strong> RADICADA</p>\n" +
                "                <p><strong>📅 Fecha:</strong> " + obtenerFechaActual() + "</p>\n" +
                "            </div>\n" +
                "            <p>Puedes hacer seguimiento de tu solicitud en cualquier momento accediendo a tu cuenta en <strong>Mesa Digital</strong>.</p>\n" +
                "            <p>Si tienes alguna pregunta, no dudes en contactarnos.</p>\n" +
                "        </div>\n" +
                "        <div class='footer'>\n" +
                "            <p>" + APP_NAME + "</p>\n" +
                "            <p>Este es un email automático, por favor no responder a este mensaje.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    // ========== EMAIL AL CAMBIAR ESTADO ==========

    public void enviarEmailCambioEstado(String emailEstudiante, String nombreEstudiante, String codigoSolicitud, String nuevoEstado) {
        String asunto = "📢 Estado actualizado - " + codigoSolicitud;
        String html = generarHTMLCambioEstado(nombreEstudiante, codigoSolicitud, nuevoEstado);
        enviarEmailHTML(emailEstudiante, asunto, html);
    }

    private String generarHTMLCambioEstado(String nombre, String codigo, String estado) {
        String colorEstado = obtenerColorEstado(estado);
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset='UTF-8'>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; background-color: #f5f5f5; }\n" +
                "        .container { max-width: 600px; margin: 20px auto; background: white; padding: 30px; border-radius: 8px; }\n" +
                "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 5px; text-align: center; }\n" +
                "        .content { padding: 20px 0; }\n" +
                "        .estado-badge { display: inline-block; background: " + colorEstado + "; color: white; padding: 10px 20px; border-radius: 20px; font-weight: bold; }\n" +
                "        .info-box { background: #f0f4ff; padding: 15px; border-left: 4px solid #667eea; margin: 15px 0; }\n" +
                "        .footer { color: #888; font-size: 12px; text-align: center; margin-top: 20px; border-top: 1px solid #eee; padding-top: 15px; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class='container'>\n" +
                "        <div class='header'>\n" +
                "            <h1>📢 Tu solicitud ha sido actualizada</h1>\n" +
                "        </div>\n" +
                "        <div class='content'>\n" +
                "            <p>Hola <strong>" + nombre + "</strong>,</p>\n" +
                "            <p>El estado de tu solicitud ha cambiado:</p>\n" +
                "            <div class='info-box'>\n" +
                "                <p><strong>📌 Código:</strong> " + codigo + "</p>\n" +
                "                <p><strong>🔄 Nuevo Estado:</strong> <span class='estado-badge'>" + estado + "</span></p>\n" +
                "                <p><strong>📅 Fecha de cambio:</strong> " + obtenerFechaActual() + "</p>\n" +
                "            </div>\n" +
                "            <p>Por favor revisa los detalles en tu cuenta de Mesa Digital.</p>\n" +
                "        </div>\n" +
                "        <div class='footer'>\n" +
                "            <p>" + APP_NAME + "</p>\n" +
                "            <p>Este es un email automático, por favor no responder a este mensaje.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    // ========== EMAIL AL ASIGNAR RESPONSABLE ==========

    public void enviarEmailAsignacionResponsable(String emailResponsable, String codigoSolicitud, String nombreEstudiante) {
        String asunto = "👤 Nueva solicitud asignada - " + codigoSolicitud;
        String html = generarHTMLAsignacionResponsable(codigoSolicitud, nombreEstudiante);
        enviarEmailHTML(emailResponsable, asunto, html);
    }

    private String generarHTMLAsignacionResponsable(String codigo, String estudiante) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset='UTF-8'>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; background-color: #f5f5f5; }\n" +
                "        .container { max-width: 600px; margin: 20px auto; background: white; padding: 30px; border-radius: 8px; }\n" +
                "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 5px; text-align: center; }\n" +
                "        .content { padding: 20px 0; }\n" +
                "        .alert { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 15px 0; border-radius: 3px; }\n" +
                "        .action-box { background: #e8f4f8; padding: 15px; border-left: 4px solid #17a2b8; margin: 15px 0; }\n" +
                "        .button { display: inline-block; background: #28a745; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin-top: 10px; }\n" +
                "        .footer { color: #888; font-size: 12px; text-align: center; margin-top: 20px; border-top: 1px solid #eee; padding-top: 15px; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class='container'>\n" +
                "        <div class='header'>\n" +
                "            <h1>👤 Nueva Solicitud Asignada</h1>\n" +
                "        </div>\n" +
                "        <div class='content'>\n" +
                "            <p>Hola,</p>\n" +
                "            <p>Se te ha asignado una nueva solicitud para gestionar.</p>\n" +
                "            <div class='alert'>\n" +
                "                <p><strong>⚠️ Acción requerida:</strong> Revisa y procesa esta solicitud lo antes posible.</p>\n" +
                "            </div>\n" +
                "            <div class='action-box'>\n" +
                "                <p><strong>📌 Código de Solicitud:</strong> " + codigo + "</p>\n" +
                "                <p><strong>👤 Solicitante:</strong> " + estudiante + "</p>\n" +
                "                <p><strong>📅 Fecha de asignación:</strong> " + obtenerFechaActual() + "</p>\n" +
                "            </div>\n" +
                "            <p>Por favor accede al Panel Administrativo para revisar los detalles completos.</p>\n" +
                "        </div>\n" +
                "        <div class='footer'>\n" +
                "            <p>" + APP_NAME + "</p>\n" +
                "            <p>Este es un email automático, por favor no responder a este mensaje.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    // ========== EMAIL AL RESOLVER SOLICITUD ==========

    public void enviarEmailResolucionSolicitud(String emailEstudiante, String nombreEstudiante, String codigoSolicitud) {
        String asunto = "✅ Tu solicitud ha sido resuelta - " + codigoSolicitud;
        String html = generarHTMLResolucionSolicitud(nombreEstudiante, codigoSolicitud);
        enviarEmailHTML(emailEstudiante, asunto, html);
    }

    private String generarHTMLResolucionSolicitud(String nombre, String codigo) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset='UTF-8'>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; background-color: #f5f5f5; }\n" +
                "        .container { max-width: 600px; margin: 20px auto; background: white; padding: 30px; border-radius: 8px; }\n" +
                "        .header { background: linear-gradient(135deg, #28a745 0%, #20c997 100%); color: white; padding: 20px; border-radius: 5px; text-align: center; }\n" +
                "        .content { padding: 20px 0; }\n" +
                "        .success-box { background: #d4edda; padding: 15px; border-left: 4px solid #28a745; margin: 15px 0; border-radius: 3px; }\n" +
                "        .info-box { background: #f0f4ff; padding: 15px; border-left: 4px solid #667eea; margin: 15px 0; }\n" +
                "        .button { display: inline-block; background: #667eea; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin-top: 10px; }\n" +
                "        .footer { color: #888; font-size: 12px; text-align: center; margin-top: 20px; border-top: 1px solid #eee; padding-top: 15px; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class='container'>\n" +
                "        <div class='header'>\n" +
                "            <h1>✅ ¡Solicitud Resuelta!</h1>\n" +
                "        </div>\n" +
                "        <div class='content'>\n" +
                "            <p>Hola <strong>" + nombre + "</strong>,</p>\n" +
                "            <p>¡Excelente noticia! Tu solicitud ha sido resuelta exitosamente.</p>\n" +
                "            <div class='success-box'>\n" +
                "                <p>Tu solicitud ha pasado todas las etapas de revisión y está lista.</p>\n" +
                "            </div>\n" +
                "            <div class='info-box'>\n" +
                "                <p><strong>📌 Código:</strong> " + codigo + "</p>\n" +
                "                <p><strong>✅ Estado:</strong> RESUELTA</p>\n" +
                "                <p><strong>📅 Fecha de resolución:</strong> " + obtenerFechaActual() + "</p>\n" +
                "            </div>\n" +
                "            <p>Por favor, accede a tu cuenta para ver los detalles y proporcionar tu calificación sobre el servicio.</p>\n" +
                "        </div>\n" +
                "        <div class='footer'>\n" +
                "            <p>" + APP_NAME + "</p>\n" +
                "            <p>Este es un email automático, por favor no responder a este mensaje.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    // ========== MÉTODOS HELPER ==========

    private String obtenerFechaActual() {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return ahora.format(formato);
    }

    private String obtenerColorEstado(String estado) {
        switch (estado.toUpperCase()) {
            case "RADICADA":
                return "#FFA500";
            case "EN_REVISION":
                return "#4169E1";
            case "RESUELTA":
                return "#28a745";
            case "ARCHIVADA":
                return "#808080";
            default:
                return "#6c757d";
        }
    }
}