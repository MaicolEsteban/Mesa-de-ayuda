import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-panel-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './panel-admin.html',
  styleUrls: ['./panel-admin.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PanelAdmin implements OnInit {
  usuario: any = null;
  solicitudes: any[] = [];
  solicitudesFiltradas: any[] = [];
  
  filtroBusqueda: string = '';
  filtroEstado: string = '';
  filtroTipo: string = '';
  
  loading: boolean = false;
  error: string = '';
  mensajeExito: string = '';
  
  // Arrays para los selects
  estados: string[] = ['RADICADA', 'EN_REVISION', 'ASIGNADA', 'RESUELTA', 'ARCHIVADA'];
  tipos: string[] = ['CERTIFICADO', 'CONSTANCIA', 'RECLAMACION', 'SOLICITUD_ACADEMICA', 'OTRO'];

  constructor(
    private apiService: ApiService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    console.log('📄 Entrando a Panel Admin');
    const usuarioData = localStorage.getItem('usuario');
    
    if (!usuarioData) {
      console.log('❌ No hay usuario en localStorage');
      this.router.navigate(['/']);
      return;
    }

    this.usuario = JSON.parse(usuarioData);
    console.log('👤 Usuario parseado:', this.usuario);

    this.cargarSolicitudes();
  }

  cargarSolicitudes() {
    console.log('📥 Cargando solicitudes...');
    this.loading = true;
    this.error = '';

   // Mapear tipo a tipoUsuario

// Mapear rol a tipoUsuario (usar ROL, no TIPO)
let tipoUsuario = this.usuario.rol;
if (tipoUsuario && tipoUsuario.includes('ADMIN')) {
  tipoUsuario = 'ADMINISTRADOR';
} else if (tipoUsuario && tipoUsuario.includes('REVISOR')) {
  tipoUsuario = 'REVISOR';
} else {
  tipoUsuario = 'ESTUDIANTE';
}

this.apiService.obtenerSolicitudesPorUsuario(
  this.usuario.email, 
  tipoUsuario
)
      .then((response: any) => {
        console.log('✅ Solicitudes cargadas:', response);
        
        // Manejo correcto de la respuesta
        if (Array.isArray(response)) {
          this.solicitudes = response;
        } 
        else if (response && Array.isArray(response.solicitudes)) {
          this.solicitudes = response.solicitudes;
        }
        else if (response && Array.isArray(response.data)) {
          this.solicitudes = response.data;
        }
        else {
          this.solicitudes = [];
        }

        console.log('📊 Solicitudes procesadas:', this.solicitudes.length);
        this.aplicarFiltros();
        this.loading = false;
        this.cdr.markForCheck();
      })
      .catch((error: any) => {
        console.error('❌ Error al cargar solicitudes:', error);
        this.error = 'Error al cargar solicitudes';
        this.solicitudes = [];
        this.solicitudesFiltradas = [];
        this.loading = false;
        this.cdr.markForCheck();
      });
  }

  aplicarFiltros() {
    console.log('🔍 Aplicando filtros');
    
    if (!Array.isArray(this.solicitudes)) {
      console.warn('⚠️ solicitudes no es un array:', this.solicitudes);
      this.solicitudesFiltradas = [];
      return;
    }

    this.solicitudesFiltradas = this.solicitudes.filter((s: any) => {
      let cumpleEstado = true;
      let cumpleTipo = true;
      let cumpleBusqueda = true;

      // Filtro por estado
      if (this.filtroEstado) {
        cumpleEstado = s.estado === this.filtroEstado;
      }

      // Filtro por tipo
      if (this.filtroTipo) {
        cumpleTipo = s.tipoSolicitud === this.filtroTipo;
      }

      // Filtro por búsqueda
      if (this.filtroBusqueda) {
        const busquedaLower = this.filtroBusqueda.toLowerCase();
        cumpleBusqueda = 
          (s.codigo && s.codigo.toLowerCase().includes(busquedaLower)) ||
          (s.emailEstudiante && s.emailEstudiante.toLowerCase().includes(busquedaLower)) ||
          (s.nombreEstudiante && s.nombreEstudiante.toLowerCase().includes(busquedaLower)) ||
          (s.cedulaEstudiante && s.cedulaEstudiante.toLowerCase().includes(busquedaLower));
      }

      return cumpleEstado && cumpleTipo && cumpleBusqueda;
    });

    console.log('📊 Solicitudes filtradas:', this.solicitudesFiltradas.length);
    this.cdr.markForCheck();
  }

  onFiltroChange() {
    console.log('🔍 Cambio en filtros');
    this.aplicarFiltros();
  }

  limpiarFiltros() {
    console.log('🧹 Limpiar filtros');
    this.filtroBusqueda = '';
    this.filtroEstado = '';
    this.filtroTipo = '';
    this.aplicarFiltros();
    this.cdr.markForCheck();
  }

  getEstadoColor(estado: string): string {
    const colores: any = {
      'RADICADA': '#FFA500',
      'EN_REVISION': '#4169E1',
      'ASIGNADA': '#32CD32',
      'RESUELTA': '#228B22',
      'ARCHIVADA': '#808080'
    };
    return colores[estado] || '#999';
  }

  getCountByEstado(estado: string): number {
    if (!Array.isArray(this.solicitudes)) return 0;
    return this.solicitudes.filter((s: any) => s.estado === estado).length;
  }

  getCountSinAsignar(): number {
    if (!Array.isArray(this.solicitudes)) return 0;
    return this.solicitudes.filter((s: any) => !s.emailResponsable).length;
  }

  verDetalles(codigo: string) {
    console.log('👁️ Ver detalles:', codigo);
    this.router.navigate(['/solicitud', codigo]);
    sessionStorage.setItem('rutaAnterior', '/admin');
  }

  cambiarEstado(codigo: string) {
    console.log('🔄 Cambiar estado:', codigo);
    const nuevoEstado = prompt('Ingresa el nuevo estado:\nRADICADA\nEN_REVISION\nASIGNADA\nRESUELTA\nARCHIVADA');
    
    if (!nuevoEstado) {
      return;
    }

    this.apiService.cambiarEstado(codigo, nuevoEstado)
      .then(() => {
        console.log('✅ Estado cambiado');
        this.mensajeExito = 'Estado actualizado correctamente';
        this.cargarSolicitudes();
        
        setTimeout(() => {
          this.mensajeExito = '';
          this.cdr.markForCheck();
        }, 3000);
      })
      .catch((error: any) => {
        console.error('❌ Error:', error);
        this.error = 'Error al cambiar estado';
        this.cdr.markForCheck();
      });
  }

  asignarResponsable(codigo: string) {
    console.log('👤 Asignar responsable:', codigo);
    const email = prompt('Ingresa el email del responsable:');
    
    if (!email) {
      return;
    }

    this.apiService.asignarResponsable(codigo, email)
      .then(() => {
        console.log('✅ Responsable asignado');
        this.mensajeExito = 'Responsable asignado correctamente';
        this.cargarSolicitudes();
        
        setTimeout(() => {
          this.mensajeExito = '';
          this.cdr.markForCheck();
        }, 3000);
      })
      .catch((error: any) => {
        console.error('❌ Error:', error);
        this.error = 'Error al asignar responsable';
        this.cdr.markForCheck();
      });
  }

  irAGestionarUsuarios() {
    console.log('👥 Ir a Gestionar Usuarios');
    this.router.navigate(['/manage-users']);
  }

  descargarReportePDF() {
    console.log('📄 Descargar reporte PDF');
    
    if (this.solicitudesFiltradas.length === 0) {
      alert('No hay solicitudes para descargar');
      return;
    }

    // Generar contenido HTML para el PDF
    let contenido = '<h1>Reporte de Solicitudes</h1>';
    contenido += '<p>Generado: ' + new Date().toLocaleString() + '</p>';
    contenido += '<table border="1" cellpadding="10">';
    contenido += '<tr><th>Código</th><th>Solicitante</th><th>Email</th><th>Tipo</th><th>Estado</th><th>Responsable</th></tr>';

    this.solicitudesFiltradas.forEach((sol: any) => {
      contenido += '<tr>';
      contenido += '<td>' + (sol.codigo || '') + '</td>';
      contenido += '<td>' + (sol.nombreEstudiante || '') + '</td>';
      contenido += '<td>' + (sol.emailEstudiante || '') + '</td>';
      contenido += '<td>' + (sol.tipoSolicitud || '') + '</td>';
      contenido += '<td>' + (sol.estado || '') + '</td>';
      contenido += '<td>' + (sol.emailResponsable || 'Sin asignar') + '</td>';
      contenido += '</tr>';
    });

    contenido += '</table>';

    // Abrir en nueva ventana para imprimir
    const ventana = window.open('', '', 'width=800,height=600');
    if (ventana) {
      ventana.document.write(contenido);
      ventana.document.close();
      ventana.print();
    }
  }

  handleLogout() {
    console.log('👋 Logout');
    localStorage.clear();
    this.router.navigate(['/']);
  }
}