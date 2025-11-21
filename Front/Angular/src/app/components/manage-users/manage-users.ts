import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-manage-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './manage-users.html',
  styleUrls: ['./manage-users.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ManageUsersComponent implements OnInit {
  usuario: any = null;
  usuarios: any[] = [];
  usuariosFiltrados: any[] = [];
  
  // Tipo de usuario a crear
  tipoCrear: string = ''; // 'ESTUDIANTE', 'REVISOR', 'ADMINISTRADOR'
  
  // Formulario general
  nuevoUsuario: any = {
    email: '',
    nombre: '',
    cedula: '',
    contrasena: '',
    tipoUsuario: '',
    rol: ''
  };

  // Formulario específico para ESTUDIANTE
  nuevoEstudiante: any = {
    email: '',
    nombre: '',
    cedula: '',
    facultad: '',
    contrasena: ''
  };

  // ⭐ NUEVO: Variables para edición
  mostrarModalEdicion = false;
  usuarioEnEdicion: any = null;
  usuarioEnEdicionOriginal: any = null;

  cargando = false;
  mensajeExito = '';
  mensajeError = '';

  filtroEmail = '';
  filtroTipo = '';

  // Arrays para selects
  tiposUsuario = ['ESTUDIANTE', 'REVISOR', 'ADMINISTRADOR'];
  facultades = [
    'Ingeniería de Sistemas',
    'Ingeniería Civil',
    'Administración de Empresas',
    'Contabilidad',
    'Psicología',
    'Comunicación Social',
    'Derecho',
    'Medicina',
    'Enfermería',
    'Educación'
  ];

  constructor(
    private apiService: ApiService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    console.log('📄 Entrando a Manage Users');
    const usuarioData = localStorage.getItem('usuario');
    console.log('👤 Usuario data:', usuarioData);
    
    if (!usuarioData) {
      console.log('❌ No hay usuario en localStorage');
      this.router.navigate(['/']);
      return;
    }

    this.usuario = JSON.parse(usuarioData);
    console.log('👤 Usuario parseado:', this.usuario);

    console.log('✅ Usuario validado correctamente');
    this.cargarUsuarios();
  }

  cargarUsuarios() {
    console.log('📥 Cargando usuarios...');
    this.apiService.obtenerTodos()
      .then((response: any) => {
        console.log('✅ Usuarios cargados:', response);
        this.usuarios = Array.isArray(response) ? response : response.data || [];
        this.aplicarFiltros();
        this.cdr.markForCheck();
      })
      .catch((error: any) => {
        console.error('❌ Error al cargar usuarios:', error);
        this.usuarios = [];
        this.aplicarFiltros();
        this.cdr.markForCheck();
      });
  }

  // ========== CREAR USUARIO GENERAL ==========

  crearUsuario() {
    console.log('➕ Intentando crear usuario:', this.nuevoUsuario);
    
    if (!this.validarFormularioGeneral()) {
      this.mensajeError = 'Por favor completa todos los campos';
      this.cdr.markForCheck();
      return;
    }

    this.cargando = true;
    this.mensajeError = '';
    this.mensajeExito = '';
    this.cdr.markForCheck();

    this.apiService.crearUsuario(this.nuevoUsuario)
      .then((response: any) => {
        console.log('✅ Usuario creado:', response);
        this.mensajeExito = `✅ Usuario ${this.nuevoUsuario.email} creado exitosamente`;
        
        this.nuevoUsuario = {
          email: '',
          nombre: '',
          cedula: '',
          contrasena: '',
          tipoUsuario: '',
          rol: ''
        };
        this.tipoCrear = '';

        this.cargarUsuarios();
        this.cdr.markForCheck();

        setTimeout(() => {
          this.mensajeExito = '';
          this.cdr.markForCheck();
        }, 3000);
      })
      .catch((error: any) => {
        console.error('❌ Error:', error);
        this.mensajeError = error.error?.error || error.mensaje || 'Error al crear usuario';
        this.cdr.markForCheck();
      })
      .finally(() => {
        this.cargando = false;
        this.cdr.markForCheck();
      });
  }

  // ========== CREAR ESTUDIANTE (CON FACULTAD) ==========

  crearEstudiante() {
    console.log('➕ Intentando crear estudiante:', this.nuevoEstudiante);
    
    if (!this.validarFormularioEstudiante()) {
      this.mensajeError = 'Por favor completa todos los campos';
      this.cdr.markForCheck();
      return;
    }

    this.cargando = true;
    this.mensajeError = '';
    this.mensajeExito = '';
    this.cdr.markForCheck();

    // Usar el endpoint específico para estudiantes
    this.apiService.crearEstudiante(
      this.nuevoEstudiante.email,
      this.nuevoEstudiante.nombre,
      this.nuevoEstudiante.cedula,
      this.nuevoEstudiante.facultad,
      this.nuevoEstudiante.contrasena
    )
      .then((response: any) => {
        console.log('✅ Estudiante creado:', response);
        this.mensajeExito = `✅ Estudiante ${this.nuevoEstudiante.email} creado exitosamente`;
        
        this.nuevoEstudiante = {
          email: '',
          nombre: '',
          cedula: '',
          facultad: '',
          contrasena: ''
        };
        this.tipoCrear = '';

        this.cargarUsuarios();
        this.cdr.markForCheck();

        setTimeout(() => {
          this.mensajeExito = '';
          this.cdr.markForCheck();
        }, 3000);
      })
      .catch((error: any) => {
        console.error('❌ Error:', error);
        this.mensajeError = error.error?.error || error.mensaje || 'Error al crear estudiante';
        this.cdr.markForCheck();
      })
      .finally(() => {
        this.cargando = false;
        this.cdr.markForCheck();
      });
  }

  // ========== EDITAR USUARIO ========== 
  // ⭐ NUEVO: Abrir modal de edición

  editarUsuario(usuario: any) {
    console.log('✏️ Editar usuario:', usuario);
    this.usuarioEnEdicionOriginal = usuario;
    // Hacer una copia profunda para edición
    this.usuarioEnEdicion = JSON.parse(JSON.stringify(usuario));
    this.mostrarModalEdicion = true;
    this.cdr.markForCheck();
  }

  // ⭐ NUEVO: Guardar cambios del usuario editado

  guardarEdicion() {
    console.log('💾 Guardando edición:', this.usuarioEnEdicion);

    if (!this.usuarioEnEdicion.nombre || !this.usuarioEnEdicion.cedula) {
      this.mensajeError = 'Nombre y cédula son obligatorios';
      this.cdr.markForCheck();
      return;
    }

    this.cargando = true;
    this.mensajeError = '';
    this.mensajeExito = '';
    this.cdr.markForCheck();

    // Crear objeto con solo los campos a actualizar
    const usuarioActualizado = {
      id: this.usuarioEnEdicion.id,
      email: this.usuarioEnEdicion.email,
      nombre: this.usuarioEnEdicion.nombre,
      cedula: this.usuarioEnEdicion.cedula,
      facultad: this.usuarioEnEdicion.facultad || '',
      contrasena: this.usuarioEnEdicion.contrasena,
      tipoUsuario: this.usuarioEnEdicion.tipoUsuario,
      rol: this.usuarioEnEdicion.rol,
      activo: this.usuarioEnEdicion.activo,
      verificado: this.usuarioEnEdicion.verificado
    };

    this.apiService.actualizarUsuario(this.usuarioEnEdicion.id, usuarioActualizado)
      .then((response: any) => {
        console.log('✅ Usuario actualizado:', response);
        this.mensajeExito = `✅ Usuario ${this.usuarioEnEdicion.nombre} actualizado exitosamente`;
        
        this.cerrarModalEdicion();
        this.cargarUsuarios();
        this.cdr.markForCheck();

        setTimeout(() => {
          this.mensajeExito = '';
          this.cdr.markForCheck();
        }, 3000);
      })
      .catch((error: any) => {
        console.error('❌ Error:', error);
        this.mensajeError = error.error?.error || 'Error al actualizar usuario';
        this.cdr.markForCheck();
      })
      .finally(() => {
        this.cargando = false;
        this.cdr.markForCheck();
      });
  }

  // ⭐ NUEVO: Cerrar modal de edición

  cerrarModalEdicion() {
    this.mostrarModalEdicion = false;
    this.usuarioEnEdicion = null;
    this.usuarioEnEdicionOriginal = null;
    this.cdr.markForCheck();
  }

  // ⭐ NUEVO: Agregar método para actualizar usuario en ApiService
  // Este método ya existe en ApiService, solo confirmamos que lo usamos

  eliminarUsuario(id: number) {
    if (!confirm('¿Estás seguro de que deseas eliminar este usuario?')) {
      return;
    }

    console.log('🗑️ Eliminando usuario:', id);
    this.apiService.eliminarUsuario(id)
      .then((response: any) => {
        console.log('✅ Usuario eliminado:', response);
        this.mensajeExito = '✅ Usuario eliminado exitosamente';
        this.cargarUsuarios();
        this.cdr.markForCheck();

        setTimeout(() => {
          this.mensajeExito = '';
          this.cdr.markForCheck();
        }, 3000);
      })
      .catch((error: any) => {
        console.error('❌ Error:', error);
        this.mensajeError = 'Error al eliminar usuario';
        this.cdr.markForCheck();
      });
  }

  aplicarFiltros() {
    console.log('🔍 Aplicando filtros');
    this.usuariosFiltrados = this.usuarios.filter((u: any) => {
      let coincideEmail = true;
      let coincideTipo = true;

      if (this.filtroEmail) {
        coincideEmail = u.email.toLowerCase().includes(this.filtroEmail.toLowerCase());
      }

      if (this.filtroTipo) {
        coincideTipo = u.tipoUsuario === this.filtroTipo;
      }

      return coincideEmail && coincideTipo;
    });

    console.log('📊 Usuarios filtrados:', this.usuariosFiltrados.length);
    this.cdr.markForCheck();
  }

  validarFormularioGeneral(): boolean {
    return (
      this.nuevoUsuario.email.trim() !== '' &&
      this.nuevoUsuario.nombre.trim() !== '' &&
      this.nuevoUsuario.cedula.trim() !== '' &&
      this.nuevoUsuario.contrasena.trim() !== '' &&
      this.nuevoUsuario.tipoUsuario !== '' &&
      this.nuevoUsuario.rol !== ''
    );
  }

  validarFormularioEstudiante(): boolean {
    return (
      this.nuevoEstudiante.email.trim() !== '' &&
      this.nuevoEstudiante.nombre.trim() !== '' &&
      this.nuevoEstudiante.cedula.trim() !== '' &&
      this.nuevoEstudiante.facultad.trim() !== '' &&
      this.nuevoEstudiante.contrasena.trim() !== ''
    );
  }

  limpiarFormularios() {
    this.tipoCrear = '';
    this.nuevoUsuario = {
      email: '',
      nombre: '',
      cedula: '',
      contrasena: '',
      tipoUsuario: '',
      rol: ''
    };
    this.nuevoEstudiante = {
      email: '',
      nombre: '',
      cedula: '',
      facultad: '',
      contrasena: ''
    };
    this.mensajeError = '';
    this.mensajeExito = '';
    this.cdr.markForCheck();
  }

  volver() {
    this.router.navigate(['/admin']);
  }
}