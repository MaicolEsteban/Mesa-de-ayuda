import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LowerCasePipe } from '@angular/common';

@Component({
  selector: 'app-crear-solicitud',
  standalone: true,
  imports: [CommonModule, FormsModule, LowerCasePipe],
  templateUrl: './crear-solicitud.html',
  styleUrls: ['./crear-solicitud.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CrearSolicitudComponent implements OnInit {
  usuario: any = null;
  loading = false;
  error = '';
  success = false;

  codigo = '';
  tipoSolicitud = '';
  descripcion = '';

  tiposDisponibles = [
    'CERTIFICADO_ACADEMICO',
    'HOMOLOGACION',
    'CAMBIO_PROGRAMA',
    'INCAPACIDAD',
    'CONVALIDACION',
    'OTRO'
  ];

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
    this.generarCodigo();
  }

  generarCodigo() {
    const fecha = new Date();
    const año = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const dia = String(fecha.getDate()).padStart(2, '0');
    const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0');
    
    this.codigo = `SOL-${año}${mes}${dia}-${random}`;
    this.cdr.markForCheck();
  }

  async crearSolicitud() {
  if (!this.tipoSolicitud) {
    this.error = 'Selecciona un tipo de solicitud';
    this.cdr.markForCheck();
    return;
  }

  if (!this.descripcion || this.descripcion.trim().length < 10) {
    this.error = 'La descripción debe tener al menos 10 caracteres';
    this.cdr.markForCheck();
    return;
  }

  this.loading = true;
  this.error = '';
  this.success = false;
  this.cdr.markForCheck();

  try {
    // ✅ OBTENER CÉDULA DEL BACKEND
    const usuarioActual: any = await this.apiService.obtenerPorEmail(this.usuario.email);
console.log('👤 Usuario desde API:', usuarioActual);  // ← AGREGA ESTA LÍNEA
const cedula = usuarioActual?.cedula || usuarioActual?.cedulaEstudiante || '0';
    await this.apiService.crearSolicitud(
      this.usuario.email,
      cedula,  // ← AHORA SÍ VIENE DEL BACKEND
      this.usuario.nombre,
      this.tipoSolicitud,
      this.descripcion
    );

    console.log('✅ Solicitud creada');
    this.success = true;
    this.cdr.markForCheck();
    
    setTimeout(() => {
      this.router.navigate(['/dashboard']);
    }, 2000);
  } catch (error: any) {
    console.error('❌ Error:', error);
    this.error = 'Error al crear la solicitud';
    this.cdr.markForCheck();
  } finally {
    this.loading = false;
    this.cdr.markForCheck();
  }
}

  volver() {
    this.router.navigate(['/dashboard']);
  }
}