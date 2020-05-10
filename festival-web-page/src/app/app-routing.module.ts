import { NgModule} from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Components
import { LoginComponent } from './login/login.component';
import { ProtectedComponent } from './components/protected/protected.component';
import { AuthGuardService } from './services/auth-guard.service';

// Routes
const appRoutes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: '/' },
  { path: '', component: LoginComponent },
  { path: 'protected', component: ProtectedComponent,
    canActivate: [AuthGuardService] },
];

export const AppRouting = RouterModule.forRoot(appRoutes);
