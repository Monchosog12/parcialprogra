import { Routes } from '@angular/router';

import { Contenidocomponent } from './contenidocomponent/contenidocomponent';
import { Logincomponent } from './logincomponent/logincomponent';

export const routes: Routes = [
  {
    path: '',
    component: Contenidocomponent,
  },

  {
    path: 'login',
    component: Logincomponent,
  },

  {
    path: '**',
    redirectTo: '',
  },
];
