import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { CommonModule, DatePipe, LowerCasePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe, LowerCasePipe],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DashboardComponent implements OnInit {
  usuario: any = null;
  solicitudes: any[] = [];
  metricas: any = null;
  solicitudesCargadas = false;

  constructor(
    private apiService: ApiService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    const usuarioData = localStorage.getItem('usuario');
    if (!usuarioData) {
      this.router.navigate(['/']);
      return;
    }

    this.usuario = JSON.parse(usuarioData);
    console.log('👤 Usuario:', this.usuario);

    // ⭐ CORREGIDO: Verificar rol correctamente
    const rol = this.usuario?.rol || this.usuario?.tipo || this.usuario?.tipoUsuario;
    const esAdmin = rol && (rol.includes('ADMIN') || rol === 'PERSONAL');
    
    // Si es PERSONAL o ADMIN, redirigir al panel admin
    if (esAdmin) {
      console.log('→ Redirigiendo a admin...');
      this.router.navigate(['/admin']);
      return;
    }

    this.cargarDatos();
  }

  cargarDatos() {
  console.log('📥 Cargando datos del dashboard...');
  
  // ⭐ NUEVO: Verificar si es revisor
  const rol = this.usuario?.rol || this.usuario?.tipo || this.usuario?.tipoUsuario;
  const esRevisor = rol && (rol.includes('REVISOR') || rol === 'REVISOR');

  console.log('👁️ Rol del usuario:', rol);
  console.log('🔍 ¿Es revisor?', esRevisor);

  // Cargar solicitudes
  let solicitudPromise;
  
  if (esRevisor) {
    // ⭐ REVISOR: Obtener solicitudes asignadas a él
    console.log('📋 Obteniendo solicitudes asignadas al revisor:', this.usuario.email);
    solicitudPromise = this.apiService.obtenerSolicitudesPorResponsable(this.usuario.email);
  } else {
    // ⭐ ESTUDIANTE: Obtener sus propias solicitudes
    console.log('📋 Obteniendo solicitudes del estudiante:', this.usuario.email);
    solicitudPromise = this.apiService.obtenerMisSolicitudes(this.usuario.email);
  }
    

    solicitudPromise
      .then((response: any) => {
        console.log('✅ Respuesta solicitudes completa:', response);
        
        // Manejo flexible de la respuesta
        if (Array.isArray(response)) {
          this.solicitudes = response;
        } 
        else if (response && response.data && Array.isArray(response.data.solicitudes)) {
          this.solicitudes = response.data.solicitudes;
        }
        else if (response && response.data && Array.isArray(response.data)) {
          this.solicitudes = response.data;
        }
        else if (response && Array.isArray(response.solicitudes)) {
          this.solicitudes = response.solicitudes;
        }
        else if (response && Array.isArray(response)) {
          this.solicitudes = response;
        }
        else {
          console.warn('⚠️ Respuesta inesperada:', response);
          this.solicitudes = [];
        }

        console.log('✅ Solicitudes procesadas:', this.solicitudes.length);
        this.solicitudesCargadas = true;
        this.cdr.markForCheck();
      })
      .catch((error: any) => {
        console.error('❌ Error al cargar solicitudes:', error);
        this.solicitudes = [];
        this.solicitudesCargadas = true;
        this.cdr.markForCheck();
      });

    // Cargar métricas
    this.apiService.obtenerMetricasDashboard()
      .then((response: any) => {
        console.log('✅ Respuesta métricas completa:', response);
        
        // Manejo flexible de la respuesta
        if (response && response.data) {
          this.metricas = response.data;
        }
        else if (response && typeof response === 'object') {
          this.metricas = response;
        }
        else {
          console.warn('⚠️ Respuesta de métricas inesperada:', response);
          this.metricas = {};
        }

        console.log('✅ Métricas procesadas:', this.metricas);
        this.cdr.markForCheck();
      })
      .catch((error: any) => {
        console.error('❌ Error al cargar métricas:', error);
        this.metricas = {
          totalSolicitudes: 0,
          enRevision: 0,
          resueltas: 0
        };
        this.cdr.markForCheck();
      });
  }

  irACrearSolicitud() {
    console.log('➕ Navegando a crear solicitud');
    this.router.navigate(['/crear-solicitud']);
  }

  verDetalles(codigo: string) {
    console.log('👁️ Ver detalles:', codigo);
    // ⭐ NUEVO: Guardar ruta anterior
    sessionStorage.setItem('rutaAnterior', '/dashboard');
    this.router.navigate(['/solicitud', codigo]);
  }

  getEstadoColor(estado: string): string {
    const colores: any = {
      'RADICADA': '#fbbf24',
      'EN_REVISIÓN': '#60a5fa',
      'ESPERANDO_INFO': '#f87171',
      'RESUELTA': '#34d399',
      'EN_REVISION': '#60a5fa'
    };
    return colores[estado] || '#999';
  }

  handleLogout() {
    console.log('🚪 Cerrando sesión');
    localStorage.clear();
    this.router.navigate(['/']);
  }
}