import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { DashboardComponent } from './components/dashboard/dashboard';
import { CrearSolicitudComponent } from './crear-solicitud/crear-solicitud';
import { DetalleSolicitudComponent } from './components/detalle-solicitud/detalle-solicitud';
import { PanelAdmin } from './components/panel-admin/panel-admin';
import { ManageUsersComponent } from './components/manage-users/manage-users';
import { CambiarContrasenaPrimerLoginComponent } from './components/cambiar-contrasena-primer-login/cambiar-contrasena-primer-login';

export const routes: Routes = [
  {
    path: '',
    component: LoginComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent
  },
  {
    path: 'crear-solicitud',
    component: CrearSolicitudComponent
  },
  {
    path: 'solicitud/:codigo',
    component: DetalleSolicitudComponent
  },
  {
    path: 'admin',
    component: PanelAdmin
  },
  {
    path: 'manage-users',
    component: ManageUsersComponent
  },
  {
    path: 'cambiar-contrasena-primer-login',
    component: CambiarContrasenaPrimerLoginComponent
  },
  {
    path: '**',
    redirectTo: '/'
  }
];