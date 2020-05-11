import { NgModule} from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Components
import { LoginComponent } from './login/login.component';
import { ProtectedComponent } from './components/protected/protected.component';
import { AuthGuardService } from './services/auth-guard.service';
import {MapComponent} from './map/map.component';

// Routes
const appRoutes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: '/' },
  { path: '', component: LoginComponent },
  { path: 'protected', component: ProtectedComponent,
    canActivate: [AuthGuardService] },
  { path: 'map', component: MapComponent,
    canActivate: [AuthGuardService] }
];

export const AppRouting = RouterModule.forRoot(appRoutes);
