import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { PdfService } from '../../services/pdf.service';
import { CommonModule, LowerCasePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-detalle-solicitud',
  standalone: true,
  imports: [CommonModule, FormsModule, LowerCasePipe],
  templateUrl: './detalle-solicitud.html',
  styleUrls: ['./detalle-solicitud.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DetalleSolicitudComponent implements OnInit {
  usuario: any = null;
  solicitud: any = null;
  eventos: any[] = [];
  loading = true;
  error = '';
  rutaAnterior: string = '/dashboard'; // ⭐ NUEVO: Guardar ruta anterior

  // Para comentarios
  comentario = '';
  enviadoComentario = false;

  // Para calificación
  calificacion = 0;
  comentarioCalificacion = '';
  enviadoCalificacion = false;

  // Para asignación y cambio de estado
  nuevoResponsable = '';
  nuevoEstado = '';
  enviandoAsignacion = false;
  enviandoEstado = false;

  estados = ['RADICADA', 'EN_REVISION', 'EN_ESPERA', 'RESUELTA', 'ARCHIVADA'];
  esAdmin = false;

  constructor(
    private apiService: ApiService,
    private pdfService: PdfService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    const usuarioData = localStorage.getItem('usuario');
    if (!usuarioData) {
      this.router.navigate(['/']);
      return;
    }

    this.usuario = JSON.parse(usuarioData);
    const tipo = this.usuario?.tipo || this.usuario?.tipoUsuario;
const rol = this.usuario?.rol || this.usuario?.tipo || this.usuario?.tipoUsuario;
this.esAdmin = rol && (rol.includes('ADMIN') || rol.includes('REVISOR') || rol === 'PERSONAL');
    // ⭐ NUEVO: Obtener ruta anterior del sessionStorage
    const rutaGuardada = sessionStorage.getItem('rutaAnterior');
    if (rutaGuardada) {
      this.rutaAnterior = rutaGuardada;
      sessionStorage.removeItem('rutaAnterior'); // Limpiar después de usar
      console.log('🔙 Ruta anterior guardada:', this.rutaAnterior);
    }

    // Obtener código de la URL
    this.route.params.subscribe((params: any) => {
      const codigo = params['codigo'];
      if (codigo) {
        this.cargarSolicitud(codigo);
      } else {
        this.error = 'No se especificó código de solicitud';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  cargarSolicitud(codigo: string) {
    this.loading = true;
    this.error = '';
    this.cdr.markForCheck();

    // Cargar solicitud
    this.apiService.obtenerSolicitudPorCodigo(codigo)
      .then((response: any) => {
        console.log('✅ Solicitud cargada:', response);
        
        // Manejo flexible de respuesta
        if (response.data) {
          this.solicitud = response.data;
        } else {
          this.solicitud = response;
        }
        
        console.log('📋 Solicitud:', this.solicitud);
        this.cdr.markForCheck();
      })
      .catch((error: any) => {
        console.error('❌ Error al cargar solicitud:', error);
        this.error = 'Error al cargar la solicitud';
        this.cdr.markForCheck();
      });

    // Cargar timeline de eventos
    this.apiService.obtenerTimeline(codigo)
      .then((response: any) => {
        console.log('✅ Timeline cargado:', response);
        
        // Manejo flexible de respuesta
        if (Array.isArray(response)) {
          this.eventos = response;
        } else if (response.data && Array.isArray(response.data)) {
          this.eventos = response.data;
        } else if (response.eventos && Array.isArray(response.eventos)) {
          this.eventos = response.eventos;
        } else {
          this.eventos = [];
        }

        // Ordenar por fecha descendente (más recientes primero)
        this.eventos.sort((a, b) => {
          const fechaA = new Date(a.fechaEvento).getTime();
          const fechaB = new Date(b.fechaEvento).getTime();
          return fechaB - fechaA;
        });

        console.log('📅 Eventos cargados:', this.eventos.length);
        this.cdr.markForCheck();
      })
      .catch((error: any) => {
        console.error('❌ Error al cargar timeline:', error);
        this.eventos = [];
        this.cdr.markForCheck();
      })
      .finally(() => {
        this.loading = false;
        this.cdr.markForCheck();
      });
  }

  // ========== MÉTODOS PARA ADMIN ==========

  asignarResponsable() {
    if (!this.esAdmin) {
      alert('Solo los administradores pueden asignar responsables');
      return;
    }

    const email = prompt('Ingresa el email del responsable:', this.solicitud?.emailResponsable || '');
    
    if (!email || email.trim() === '') {
      return;
    }

    this.enviandoAsignacion = true;
    this.cdr.markForCheck();

    this.apiService.asignarResponsable(this.solicitud.codigo, email)
      .then((response: any) => {
        console.log('✅ Responsable asignado:', response);
        
        // Actualizar solicitud
        this.solicitud.emailResponsable = email;
        
        // Agregar evento al timeline
        const evento = {
          codigoSolicitud: this.solicitud.codigo,
          tipoEvento: 'ASIGNACION',
          descripcion: `Solicitud asignada a: ${email}`,
          usuario: this.usuario.email,
          fechaEvento: new Date().toISOString()
        };
        
        this.eventos.unshift(evento);
        alert('✅ Responsable asignado correctamente');
        this.cdr.markForCheck();
      })
      .catch((error: any) => {
        console.error('❌ Error:', error);
        alert('❌ Error al asignar responsable');
        this.cdr.markForCheck();
      })
      .finally(() => {
        this.enviandoAsignacion = false;
        this.cdr.markForCheck();
      });
  }

  cambiarEstado() {
    if (!this.esAdmin) {
      alert('Solo los administradores pueden cambiar el estado');
      return;
    }

    if (!this.nuevoEstado) {
      alert('Por favor selecciona un estado');
      return;
    }

    this.enviandoEstado = true;
    this.cdr.markForCheck();

    this.apiService.cambiarEstado(this.solicitud.codigo, this.nuevoEstado)
      .then((response: any) => {
        console.log('✅ Estado cambiado:', response);
        
        // Actualizar solicitud
        this.solicitud.estado = this.nuevoEstado;
        
        // Agregar evento al timeline
        const evento = {
          codigoSolicitud: this.solicitud.codigo,
          tipoEvento: 'CAMBIO_ESTADO',
          descripcion: `Estado cambió a: ${this.nuevoEstado}`,
          usuario: this.usuario.email,
          fechaEvento: new Date().toISOString()
        };
        
        this.eventos.unshift(evento);
        this.nuevoEstado = '';
        alert('✅ Estado actualizado correctamente');
        this.cdr.markForCheck();
      })
      .catch((error: any) => {
        console.error('❌ Error:', error);
        alert('❌ Error al cambiar estado');
        this.cdr.markForCheck();
      })
      .finally(() => {
        this.enviandoEstado = false;
        this.cdr.markForCheck();
      });
  }

  // ========== MÉTODOS PDF ==========

  descargarPDF() {
    if (this.solicitud) {
      console.log('📥 Descargando PDF de solicitud:', this.solicitud.codigo);
      this.pdfService.generarPdfSolicitud(this.solicitud, this.eventos);
    }
  }

  // ========== MÉTODOS COMENTARIOS Y CALIFICACIÓN ==========

  agregarComentario() {
    if (!this.comentario.trim()) {
      return;
    }

    this.enviadoComentario = true;
    this.cdr.markForCheck();

    // Guardar comentario en BD
    this.apiService.agregarComentario(
      this.solicitud.codigo,
      this.comentario,
      this.usuario.email
    )
      .then((response: any) => {
        console.log('✅ Comentario guardado:', response);
        
        // Agregar a la lista local
        const nuevoEvento = {
          codigoSolicitud: this.solicitud.codigo,
          tipoEvento: 'COMENTARIO',
          descripcion: this.comentario,
          usuario: this.usuario.email,
          fechaEvento: new Date().toISOString()
        };

        this.eventos.unshift(nuevoEvento);
        this.comentario = '';
        alert('✅ Comentario agregado');
        this.cdr.markForCheck();
      })
      .catch((error: any) => {
        console.error('❌ Error al guardar comentario:', error);
        alert('❌ Error al guardar el comentario');
        this.cdr.markForCheck();
      })
      .finally(() => {
        this.enviadoComentario = false;
        this.cdr.markForCheck();
      });
  }

  calificarSolicitud() {
    if (this.calificacion === 0) {
      alert('Por favor selecciona una calificación');
      return;
    }

    this.enviadoCalificacion = true;
    this.cdr.markForCheck();

    // Guardar calificación
    this.apiService.crearEvento(
      this.solicitud.codigo,
      'CALIFICACION',
      `Calificación: ${this.calificacion}/5 - ${this.comentarioCalificacion}`,
      this.usuario.email
    )
      .then((response: any) => {
        console.log('✅ Calificación guardada:', response);
        
        // Agregar a la lista local
        const nuevoEvento = {
          codigoSolicitud: this.solicitud.codigo,
          tipoEvento: 'CALIFICACION',
          descripcion: `Calificación: ${this.calificacion}/5 - ${this.comentarioCalificacion}`,
          usuario: this.usuario.email,
          fechaEvento: new Date().toISOString()
        };

        this.eventos.unshift(nuevoEvento);
        alert('¡Gracias por tu calificación!');
        this.calificacion = 0;
        this.comentarioCalificacion = '';
        this.cdr.markForCheck();
      })
      .catch((error: any) => {
        console.error('❌ Error al guardar calificación:', error);
        alert('Error al guardar la calificación');
        this.cdr.markForCheck();
      })
      .finally(() => {
        this.enviadoCalificacion = false;
        this.cdr.markForCheck();
      });
  }

  getEstadoColor(estado: string): string {
    const colores: any = {
      'RADICADA': '#fbbf24',
      'EN_REVISION': '#60a5fa',
      'EN_ESPERA': '#f87171',
      'RESUELTA': '#34d399',
      'ARCHIVADA': '#808080'
    };
    return colores[estado] || '#999';
  }

  volver() {
    // ⭐ CORREGIDO: Volver a la ruta guardada
    console.log('🔙 Volviendo a:', this.rutaAnterior);
    this.router.navigate([this.rutaAnterior]);
  }

  calcularPorcentajeRestante(): number {
  if (!this.solicitud?.fechaLimiteSolucion || !this.solicitud?.fechaRadicacion) {
    return 0;
  }

  const ahora = new Date().getTime();
  const fechaRadicacion = new Date(this.solicitud.fechaRadicacion).getTime();
  const fechaLimite = new Date(this.solicitud.fechaLimiteSolucion).getTime();

  const tiempoTotal = fechaLimite - fechaRadicacion;
  const tiempoTranscurrido = ahora - fechaRadicacion;
  const tiempoRestante = tiempoTotal - tiempoTranscurrido;

  const porcentaje = (tiempoRestante / tiempoTotal) * 100;
  return Math.max(0, Math.min(100, porcentaje));
}
}