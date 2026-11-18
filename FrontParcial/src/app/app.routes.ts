import { Routes } from '@angular/router';

import { Logincomponent } from './components/logincomponent/logincomponent';
import { Contenidocomponent } from './components/contenidocomponent/contenidocomponent';
import { Admincomponent } from './components/admincomponent/admincomponent';

export const routes: Routes = [
  {
    path: '',
    component: Logincomponent,
  },

  {
    path: 'contenido',
    component: Contenidocomponent,
  },

  {
    path: 'admin',
    component: Admincomponent,
  },

  {
    path: '**',
    redirectTo: '',
  },
];
