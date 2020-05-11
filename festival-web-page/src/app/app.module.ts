import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule} from '@angular/common/http';
import { AppComponent } from './app.component';
import {FormsModule} from '@angular/forms';
import { LoginComponent } from './login/login.component';
import {RouterModule, Routes} from '@angular/router';
import { MapComponent } from './map/map.component';
import { GoogleMapsModule } from '@angular/google-maps';

// OAuth2 Imports Start https://www.oreilly.com/content/implement-oauth0-in-15-minutes-with-firebase/

// Import environment configuration
import { environment } from 'src/environments/environment';
// Import AngularFire
import { AngularFireModule } from 'angularfire2';
import { AngularFireAuthModule } from 'angularfire2/auth';

import { AppRouting } from './app-routing.module';

// Services
import { AuthenticationService } from './services/authentication.service';
import { ProtectedComponent } from './components/protected/protected.component';

import { AuthGuardService } from './services/auth-guard.service';
import {AgmCoreModule} from '@agm/core';
// OAuth2 Imports end


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    MapComponent,
    ProtectedComponent,
  ],
    imports: [
        BrowserModule,
        FormsModule,
        AngularFireModule.initializeApp(environment.firebase),
        AngularFireAuthModule,
        AppRouting,
        GoogleMapsModule,
        HttpClientModule
    ],
  providers: [
    AuthenticationService,
    AuthenticationService,
    AuthGuardService],
  bootstrap: [AppComponent]
})

export class AppModule { }
