import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // ========== USUARIOS ==========
  
  loginUsuario(email: string, contrasena: string) {
    console.log('🔐 Login:', email);
    return this.http.post(`${this.apiUrl}/usuarios/login?email=${email}&contrasena=${contrasena}`, {}).toPromise();
  }

  obtenerTodos() {
    console.log('📥 GET: ' + this.apiUrl + '/usuarios');
    return this.http.get(`${this.apiUrl}/usuarios`).toPromise();
  }

  crearUsuario(usuario: any) {
    console.log('📤 POST: ' + this.apiUrl + '/usuarios/crear', usuario);
    return this.http.post(`${this.apiUrl}/usuarios/crear`, usuario).toPromise();
  }

  eliminarUsuario(id: number) {
    console.log('🗑️ DELETE: ' + this.apiUrl + '/usuarios/' + id);
    return this.http.delete(`${this.apiUrl}/usuarios/${id}`).toPromise();
  }

  obtenerEstadisticas() {
    console.log('📊 GET: Estadísticas');
    return this.http.get(`${this.apiUrl}/usuarios/stats`).toPromise();
  }

  obtenerPorEmail(email: string) {
    console.log('📥 GET: ' + this.apiUrl + '/usuarios/email/' + email);
    return this.http.get(`${this.apiUrl}/usuarios/email/${email}`).toPromise();
  }

  obtenerPorId(id: number) {
    console.log('📥 GET: ' + this.apiUrl + '/usuarios/' + id);
    return this.http.get(`${this.apiUrl}/usuarios/${id}`).toPromise();
  }

  obtenerUsuarioPorTipo(tipo: string) {
    console.log('📥 GET: ' + this.apiUrl + '/usuarios/tipo/' + tipo);
    return this.http.get(`${this.apiUrl}/usuarios/tipo/${tipo}`).toPromise();
  }

  resetPassword(email: string, newPassword: string) {
    console.log('🔄 POST: Reset password');
    return this.http.post(`${this.apiUrl}/usuarios/reset-password/${email}/${newPassword}`, {}).toPromise();
  }

  crearEstudiante(email: string, nombre: string, cedula: string, facultad: string, contrasena: string) {
    console.log('➕ POST: Crear estudiante');
    return this.http.post(`${this.apiUrl}/usuarios/estudiante?email=${email}&nombre=${nombre}&cedula=${cedula}&facultad=${facultad}&contrasena=${contrasena}`, {}).toPromise();
  }

  actualizarUsuario(id: number, usuario: any) {
    console.log('🔄 PUT: Actualizar usuario', id);
    return this.http.put(`${this.apiUrl}/usuarios/${id}`, usuario).toPromise();
  }

  // ========== SOLICITUDES ==========
  
  crearSolicitud(emailEstudiante: string, cedulaEstudiante: string, nombreEstudiante: string, 
                tipoSolicitud: string, descripcion: string) {
    console.log('📤 POST: Crear solicitud');
    
    // ✅ GENERAR CÓDIGO ÚNICO
    const fecha = new Date();
    const año = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const dia = String(fecha.getDate()).padStart(2, '0');
    const random = Math.floor(Math.random() * 10000).toString().padStart(4, '0');
    const codigo = `SOL-${año}${mes}${dia}-${random}`;

    const solicitud = {
      codigo: codigo,
      emailEstudiante: emailEstudiante,
      cedulaEstudiante: cedulaEstudiante,
      nombreEstudiante: nombreEstudiante,
      tipoSolicitud: tipoSolicitud,
      descripcion: descripcion,
      estado: 'RADICADA',
      activa: true
    };

    return this.http.post(`${this.apiUrl}/solicitudes`, solicitud).toPromise();
  }

  obtenerSolicitudes() {
    console.log('📥 GET: ' + this.apiUrl + '/solicitudes');
    return this.http.get(`${this.apiUrl}/solicitudes`).toPromise();
  }

  obtenerSolicitudPorCodigo(codigo: string) {
    console.log('📥 GET: ' + this.apiUrl + '/solicitudes/' + codigo);
    return this.http.get(`${this.apiUrl}/solicitudes/${codigo}`).toPromise();
  }

  obtenerMisSolicitudes(email: string) {
    console.log('📥 GET: Mis solicitudes -', email);
    return this.http.get(`${this.apiUrl}/solicitudes/estudiante?email=${email}`).toPromise();
  }

  obtenerMetricasDashboard() {
    console.log('📊 GET: Métricas dashboard');
    return this.http.get(`${this.apiUrl}/solicitudes/metricas/dashboard`).toPromise();
  }

  // ⭐ NUEVO: Obtener solicitudes asignadas a un revisor
  obtenerSolicitudesPorResponsable(emailResponsable: string): Promise<any> {
    console.log('📡 GET: Solicitudes asignadas a:', emailResponsable);
    return this.http.get(`${this.apiUrl}/solicitudes/responsable/${emailResponsable}`).toPromise();
  }

  // ========== ACTUALIZAR SOLICITUD ==========

  actualizarSolicitud(codigo: string, solicitud: any) {
    console.log('🔄 PUT: ' + this.apiUrl + '/solicitudes/' + codigo, solicitud);
    return this.http.put(`${this.apiUrl}/solicitudes/${codigo}`, solicitud).toPromise();
  }

  asignarResponsable(codigo: string, emailResponsable: string) {
    console.log('👤 PUT: Asignar responsable');
    return this.http.put(`${this.apiUrl}/solicitudes/${codigo}/asignar?emailResponsable=${emailResponsable}`, {}).toPromise();
  }

  cambiarEstado(codigo: string, nuevoEstado: string) {
    console.log('📋 PUT: Cambiar estado a', nuevoEstado);
    return this.http.put(`${this.apiUrl}/solicitudes/${codigo}/estado?estado=${nuevoEstado}`, {}).toPromise();
  }

  // ========== EVENTOS / TIMELINE ==========
  
  obtenerTimeline(codigo: string) {
    console.log('📅 GET: Timeline -', codigo);
    return this.http.get(`${this.apiUrl}/eventos/solicitud/${codigo}`).toPromise();
  }

  crearEvento(codigoSolicitud: string, tipoEvento: string, descripcion: string, usuario: string) {
    console.log('📤 POST: Crear evento');
    const evento = {
      codigoSolicitud: codigoSolicitud,
      tipoEvento: tipoEvento,
      descripcion: descripcion,
      usuario: usuario,
      fechaEvento: new Date().toISOString()
    };
    return this.http.post(`${this.apiUrl}/eventos`, evento).toPromise();
  }

  agregarComentario(codigoSolicitud: string, comentario: string, usuario: string) {
    console.log('💬 POST: Agregar comentario');
    return this.crearEvento(codigoSolicitud, 'COMENTARIO', comentario, usuario);
  }

  obtenerEventos(codigoSolicitud: string) {
    console.log('📥 GET: Eventos -', codigoSolicitud);
    return this.http.get(`${this.apiUrl}/eventos/${codigoSolicitud}`).toPromise();
  }

  obtenerEstadisticasEventos() {
    console.log('📊 GET: Estadísticas eventos');
    return this.http.get(`${this.apiUrl}/eventos/stats`).toPromise();
  }

  obtenerSolicitudesPorUsuario(email: string, tipoUsuario: string) {
    console.log('📥 GET: Solicitudes por usuario -', tipoUsuario);
    return this.http.get(`${this.apiUrl}/solicitudes/porUsuario?email=${email}&tipoUsuario=${tipoUsuario}`).toPromise();
  }

  obtenerCedulaPorEmail(email: string) {
    console.log('📥 GET: Cédula del usuario');
    return this.http.get(`${this.apiUrl}/usuarios/email/${email}`).toPromise();
  }

  cambiarContrasenaPrimerLogin(id: number, nuevaContrasena: string) {
    console.log('🔐 Cambiar contraseña primer login');
    return this.http.post(
      `${this.apiUrl}/usuarios/cambiar-contrasena-primer-login/${id}?nuevaContrasena=${nuevaContrasena}`,
      {}
    ).toPromise();
  }
}