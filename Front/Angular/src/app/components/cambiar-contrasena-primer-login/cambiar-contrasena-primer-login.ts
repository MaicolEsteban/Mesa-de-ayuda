import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-cambiar-contrasena-primer-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cambiar-contrasena-primer-login.html',
  styleUrls: ['./cambiar-contrasena-primer-login.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CambiarContrasenaPrimerLoginComponent implements OnInit {
  usuario: any = null;
  nuevaContrasena = '';
  confirmarContrasena = '';
  loading = false;
  error = '';
  success = false;

  constructor(
    private apiService: ApiService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    const usuarioTemp = localStorage.getItem('cambiarContrasenaPrimerLogin');
    if (!usuarioTemp) {
      this.router.navigate(['/']);
      return;
    }

    this.usuario = JSON.parse(usuarioTemp);
    console.log('👤 Usuario requiere cambio de contraseña:', this.usuario);
  }

  async cambiarContrasena() {
    if (!this.nuevaContrasena || !this.confirmarContrasena) {
      this.error = 'Por favor completa los campos';
      this.cdr.markForCheck();
      return;
    }

    if (this.nuevaContrasena.length < 6) {
      this.error = 'La contraseña debe tener al menos 6 caracteres';
      this.cdr.markForCheck();
      return;
    }

    if (this.nuevaContrasena !== this.confirmarContrasena) {
      this.error = 'Las contraseñas no coinciden';
      this.cdr.markForCheck();
      return;
    }

    this.loading = true;
    this.error = '';
    this.cdr.markForCheck();

    try {
      await this.apiService.cambiarContrasenaPrimerLogin(
        this.usuario.id,
        this.nuevaContrasena
      );

      console.log(' Contraseña cambiada exitosamente');
      this.success = true;
      this.cdr.markForCheck();

      // Limpiar localStorage
      localStorage.removeItem('cambiarContrasenaPrimerLogin');

      setTimeout(() => {
        this.router.navigate(['/dashboard']);
      }, 2000);
    } catch (error: any) {
      console.error(' Error:', error);
      this.error = error.error?.error || 'Error al cambiar contraseña';
      this.cdr.markForCheck();
    } finally {
      this.loading = false;
      this.cdr.markForCheck();
    }
  }

  volver() {
    localStorage.removeItem('cambiarContrasenaPrimerLogin');
    this.router.navigate(['/']);
  }
}