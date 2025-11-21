import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent implements OnInit {
  email: string = '';
  contrasena: string = '';
  loading: boolean = false;
  error: string = '';

  constructor(
    private apiService: ApiService,
    private router: Router
  ) {}

  ngOnInit() {
    // Verificar si ya hay sesión
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      console.log('✅ Sesión activa, redirigiendo...');
      try {
        const usuario = JSON.parse(usuarioGuardado);
        if (usuario.tipoUsuario === 'ESTUDIANTE' || usuario.tipo === 'ESTUDIANTE') {
          this.router.navigate(['/dashboard']);
        } else {
          this.router.navigate(['/admin']);
        }
      } catch (e) {
        console.error('Error al parsear usuario:', e);
        localStorage.clear();
      }
    }
  }

  handleLogin() {
  console.log('🔐 Iniciando login...');

  if (!this.email || !this.contrasena) {
    this.error = 'Por favor ingresa email y contraseña';
    return;
  }

  this.loading = true;
  this.error = '';

  this.apiService.loginUsuario(this.email, this.contrasena)
    .then((response: any) => {
      console.log('✅ Login exitoso:', response);

      const usuario = response.usuario || response;
      
      // ⭐ NUEVO: Verificar si debe cambiar contraseña
      const debesCambiarContrasena = response.debesCambiarContrasena === true;
      
      // Guardar en localStorage
      const usuarioGuardado = {
        id: usuario.id,
        email: usuario.email,
        nombre: usuario.nombre,
        cedula: usuario.cedula,
        facultad: usuario.facultad,
        rol: usuario.rol,
        tipo: usuario.tipoUsuario,
        tipoUsuario: usuario.tipoUsuario
      };

      localStorage.setItem('usuario', JSON.stringify(usuarioGuardado));
      localStorage.setItem('rol', usuario.rol || usuario.tipoUsuario || 'ESTUDIANTE');
      localStorage.setItem('email', usuario.email);
      localStorage.setItem('cedula', usuario.cedula || '');

      console.log('📦 Usuario guardado en localStorage:', usuarioGuardado);

      // ⭐ NUEVO: Si debe cambiar contraseña
      if (debesCambiarContrasena) {
        console.log('🔐 Usuario debe cambiar contraseña en primer login');
        localStorage.setItem('cambiarContrasenaPrimerLogin', JSON.stringify(usuario));
        this.router.navigate(['/cambiar-contrasena-primer-login']);
      } else {
        // Login normal, redirigir según tipo
        const tipo = usuario.tipoUsuario || usuario.tipo || 'ESTUDIANTE';
        
        if (tipo === 'ESTUDIANTE') {
          console.log('→ Redirigiendo a Dashboard (Estudiante)');
          this.router.navigate(['/dashboard']);
        } else if (tipo === 'PERSONAL' || tipo === 'ADMIN') {
          console.log('→ Redirigiendo a Panel Admin');
          this.router.navigate(['/admin']);
        } else {
          console.log('→ Redirigiendo a Dashboard (Default)');
          this.router.navigate(['/dashboard']);
        }
      }

      this.loading = false;
    })
    .catch((error: any) => {
      console.error('❌ Error en login:', error);
      
      let mensaje = 'Error al iniciar sesión';
      
      if (error.status === 401) {
        mensaje = 'Email o contraseña incorrectos';
      } else if (error.status === 404) {
        mensaje = 'Usuario no encontrado';
      } else if (error.status === 0) {
        mensaje = 'No se puede conectar con el servidor';
      } else if (error.error?.mensaje) {
        mensaje = error.error.mensaje;
      } else if (error.error?.error) {
        mensaje = error.error.error;
      }

      this.error = mensaje;
      console.log('📝 Mensaje error:', this.error);
      this.loading = false;
    });
}
}