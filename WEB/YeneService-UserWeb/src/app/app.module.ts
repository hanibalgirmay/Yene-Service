import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AdminLoginComponent } from './admin/admin-login/admin-login.component';
import { LoginComponent } from './User/login/login.component';
import { NavComponent } from './content/nav/nav.component';
import { HomeComponent } from './content/home/home.component';

@NgModule({
  declarations: [
    AppComponent,
    AdminLoginComponent,
    LoginComponent,
    NavComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
