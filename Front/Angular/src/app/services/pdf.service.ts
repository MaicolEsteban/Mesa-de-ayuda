import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PdfService {

  constructor() { }

  // ========== GENERAR PDF DE SOLICITUD ==========

  generarPdfSolicitud(solicitud: any, eventos: any[] = []) {
    let html = `
      <!DOCTYPE html>
      <html>
      <head>
        <meta charset="UTF-8">
        <title>Solicitud ${solicitud.codigo}</title>
        <style>
          body {
            font-family: Arial, sans-serif;
            margin: 20px;
            color: #333;
          }
          .header {
            text-align: center;
            margin-bottom: 30px;
            border-bottom: 3px solid #667eea;
            padding-bottom: 15px;
          }
          .header h1 {
            margin: 0;
            color: #667eea;
          }
          .header p {
            margin: 5px 0;
            color: #666;
          }
          .section {
            margin-bottom: 20px;
            page-break-inside: avoid;
          }
          .section-title {
            background-color: #f0f0f0;
            padding: 8px 12px;
            font-weight: bold;
            color: #333;
            margin-bottom: 10px;
            border-left: 4px solid #667eea;
          }
          .info-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
          }
          .info-item {
            margin-bottom: 10px;
          }
          .info-label {
            font-weight: bold;
            color: #667eea;
          }
          .info-value {
            margin: 3px 0 0 10px;
            color: #555;
          }
          .description-box {
            background-color: #f9f9f9;
            border: 1px solid #ddd;
            padding: 12px;
            border-radius: 4px;
            line-height: 1.6;
          }
          table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
          }
          th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
          }
          th {
            background-color: #667eea;
            color: white;
            font-weight: bold;
          }
          tr:nth-child(even) {
            background-color: #f9f9f9;
          }
          .footer {
            margin-top: 30px;
            border-top: 1px solid #ddd;
            padding-top: 10px;
            text-align: center;
            color: #999;
            font-size: 11px;
          }
          .estado-badge {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 4px;
            color: white;
            font-weight: bold;
          }
          .estado-radicada {
            background-color: #fbbf24;
            color: #333;
          }
          .estado-revision {
            background-color: #60a5fa;
          }
          .estado-esperando {
            background-color: #f87171;
          }
          .estado-resuelta {
            background-color: #34d399;
            color: #333;
          }
          @media print {
            body {
              margin: 0;
              padding: 0;
            }
            .section {
              page-break-inside: avoid;
            }
          }
        </style>
      </head>
      <body>
        <div class="header">
          <h1>MESA DIGITAL</h1>
          <p>Reporte de Solicitud</p>
          <p style="font-size: 12px;">Generado: ${new Date().toLocaleDateString('es-CO')} ${new Date().toLocaleTimeString('es-CO')}</p>
        </div>

        <div class="section">
          <div class="section-title">INFORMACIÓN DE LA SOLICITUD</div>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">Código:</div>
              <div class="info-value">${solicitud.codigo}</div>
            </div>
            <div class="info-item">
              <div class="info-label">Estado:</div>
              <div class="info-value">
                <span class="estado-badge ${this.getEstadoClass(solicitud.estado)}">
                  ${solicitud.estado}
                </span>
              </div>
            </div>
            <div class="info-item">
              <div class="info-label">Tipo de Solicitud:</div>
              <div class="info-value">${solicitud.tipoSolicitud}</div>
            </div>
            <div class="info-item">
              <div class="info-label">Prioridad:</div>
              <div class="info-value">${this.getPrioridadText(solicitud.prioridad)}</div>
            </div>
          </div>
        </div>

        <div class="section">
          <div class="section-title">INFORMACIÓN DEL SOLICITANTE</div>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">Nombre:</div>
              <div class="info-value">${solicitud.nombreEstudiante}</div>
            </div>
            <div class="info-item">
              <div class="info-label">Email:</div>
              <div class="info-value">${solicitud.emailEstudiante}</div>
            </div>
            <div class="info-item">
              <div class="info-label">Cédula:</div>
              <div class="info-value">${solicitud.cedulaEstudiante}</div>
            </div>
          </div>
        </div>

        <div class="section">
          <div class="section-title">INFORMACIÓN DE GESTIÓN</div>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">Responsable:</div>
              <div class="info-value">${solicitud.emailResponsable || 'Sin asignar'}</div>
            </div>
            <div class="info-item">
              <div class="info-label">Fecha de Radicación:</div>
              <div class="info-value">${this.formatearFecha(solicitud.fechaRadicacion)}</div>
            </div>
            <div class="info-item">
              <div class="info-label">Fecha de Asignación:</div>
              <div class="info-value">${solicitud.fechaAsignacion ? this.formatearFecha(solicitud.fechaAsignacion) : 'Pendiente'}</div>
            </div>
            <div class="info-item">
              <div class="info-label">Fecha de Resolución:</div>
              <div class="info-value">${solicitud.fechaResolucion ? this.formatearFecha(solicitud.fechaResolucion) : 'Pendiente'}</div>
            </div>
            <div class="info-item">
              <div class="info-label">Días Transcurridos:</div>
              <div class="info-value">${solicitud.diasTranscurridos || 0} días</div>
            </div>
          </div>
        </div>

        <div class="section">
          <div class="section-title">DESCRIPCIÓN</div>
          <div class="description-box">
            ${solicitud.descripcion}
          </div>
        </div>

        ${eventos.length > 0 ? `
        <div class="section">
          <div class="section-title">HISTORIAL DE CAMBIOS</div>
          <table>
            <thead>
              <tr>
                <th>Fecha</th>
                <th>Tipo</th>
                <th>Descripción</th>
                <th>Usuario</th>
              </tr>
            </thead>
            <tbody>
              ${eventos.map(e => `
              <tr>
                <td>${this.formatearFecha(e.fechaEvento)}</td>
                <td>${e.tipoEvento}</td>
                <td>${e.descripcion.substring(0, 50)}</td>
                <td>${e.usuario.substring(0, 20)}</td>
              </tr>
              `).join('')}
            </tbody>
          </table>
        </div>
        ` : ''}

        <div class="footer">
          <p>Este documento fue generado automáticamente por el sistema Mesa Digital</p>
        </div>
      </body>
      </html>
    `;

    this.abrirPDF(html, `Solicitud_${solicitud.codigo}.pdf`);
  }

  // ========== GENERAR PDF DE REPORTE ADMIN ==========

  generarPdfReporte(solicitudes: any[], filtros: any = {}) {
    let html = `
      <!DOCTYPE html>
      <html>
      <head>
        <meta charset="UTF-8">
        <title>Reporte de Solicitudes</title>
        <style>
          body {
            font-family: Arial, sans-serif;
            margin: 15px;
            color: #333;
          }
          .header {
            text-align: center;
            margin-bottom: 25px;
            border-bottom: 3px solid #667eea;
            padding-bottom: 15px;
          }
          .header h1 {
            margin: 0;
            color: #667eea;
          }
          .header p {
            margin: 5px 0;
            color: #666;
          }
          .resumen {
            background-color: #f0f0f0;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
            border-left: 4px solid #667eea;
          }
          .resumen p {
            margin: 5px 0;
            color: #555;
          }
          .section-title {
            background-color: #667eea;
            color: white;
            padding: 8px 12px;
            margin-bottom: 10px;
            border-radius: 4px;
            font-weight: bold;
          }
          table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
          }
          th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
            font-size: 11px;
          }
          th {
            background-color: #667eea;
            color: white;
            font-weight: bold;
          }
          tr:nth-child(even) {
            background-color: #f9f9f9;
          }
          .footer {
            margin-top: 20px;
            border-top: 1px solid #ddd;
            padding-top: 10px;
            text-align: center;
            color: #999;
            font-size: 10px;
          }
          .estado-badge {
            display: inline-block;
            padding: 3px 8px;
            border-radius: 3px;
            color: white;
            font-weight: bold;
            font-size: 10px;
          }
          .estado-radicada {
            background-color: #fbbf24;
            color: #333;
          }
          .estado-revision {
            background-color: #60a5fa;
          }
          .estado-esperando {
            background-color: #f87171;
          }
          .estado-resuelta {
            background-color: #34d399;
            color: #333;
          }
          @media print {
            body {
              margin: 0;
              padding: 0;
            }
          }
        </style>
      </head>
      <body>
        <div class="header">
          <h1>REPORTE DE SOLICITUDES</h1>
          <p>Panel Administrativo - Mesa Digital</p>
          <p style="font-size: 11px;">Generado: ${new Date().toLocaleDateString('es-CO')} ${new Date().toLocaleTimeString('es-CO')}</p>
        </div>

        <div class="resumen">
          <div class="section-title">RESUMEN DEL REPORTE</div>
          <p><strong>Total:</strong> ${solicitudes.length}</p>
          <p><strong>Radicadas:</strong> ${solicitudes.filter(s => s.estado === 'RADICADA').length}</p>
          <p><strong>En Revisión:</strong> ${solicitudes.filter(s => s.estado === 'EN_REVISIÓN').length}</p>
          <p><strong>Resueltas:</strong> ${solicitudes.filter(s => s.estado === 'RESUELTA').length}</p>
          <p><strong>Sin Asignar:</strong> ${solicitudes.filter(s => !s.emailResponsable).length}</p>
        </div>

        <div class="section-title">DETALLE DE SOLICITUDES</div>
        <table>
          <thead>
            <tr>
              <th>Código</th>
              <th>Solicitante</th>
              <th>Email</th>
              <th>Tipo</th>
              <th>Estado</th>
              <th>Responsable</th>
              <th>Radicación</th>
            </tr>
          </thead>
          <tbody>
            ${solicitudes.map(s => `
            <tr>
              <td>${s.codigo}</td>
              <td>${s.nombreEstudiante.substring(0, 15)}</td>
              <td>${s.emailEstudiante.substring(0, 18)}</td>
              <td>${s.tipoSolicitud.substring(0, 12)}</td>
              <td><span class="estado-badge ${this.getEstadoClass(s.estado)}">${s.estado}</span></td>
              <td>${s.emailResponsable ? s.emailResponsable.substring(0, 15) : 'Sin asignar'}</td>
              <td>${this.formatearFecha(s.fechaRadicacion)}</td>
            </tr>
            `).join('')}
          </tbody>
        </table>

        <div class="footer">
          <p>Este documento fue generado automáticamente por el sistema Mesa Digital</p>
        </div>
      </body>
      </html>
    `;

    this.abrirPDF(html, `Reporte_Solicitudes_${new Date().getTime()}.pdf`);
  }

  // ========== MÉTODO PARA ABRIR/DESCARGAR PDF ==========

  private abrirPDF(html: string, nombreArchivo: string) {
    const ventana = window.open('', '_blank');
    if (ventana) {
      ventana.document.write(html);
      ventana.document.close();
      setTimeout(() => {
        ventana.print();
      }, 250);
    }
  }

  // ========== MÉTODOS AUXILIARES ==========

  private formatearFecha(fecha: any): string {
    if (!fecha) return '';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-CO');
  }

  private getPrioridadText(prioridad: number): string {
    const prioridades: any = {
      1: 'Muy Alta',
      2: 'Alta',
      3: 'Normal',
      4: 'Baja'
    };
    return prioridades[prioridad] || 'Normal';
  }

  private getEstadoClass(estado: string): string {
    const clases: any = {
      'RADICADA': 'estado-radicada',
      'EN_REVISIÓN': 'estado-revision',
      'ESPERANDO_INFO': 'estado-esperando',
      'RESUELTA': 'estado-resuelta'
    };
    return clases[estado] || '';
  }
}
