import { Routes } from '@angular/router';

import { Contenidocomponent } from './components/contenidocomponent/contenidocomponent';
import { Logincomponent } from './components/logincomponent/logincomponent';

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
