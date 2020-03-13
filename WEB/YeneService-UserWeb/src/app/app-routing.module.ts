import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './User/login/login.component';
import { HomeComponent } from './content/home/home.component';
import { PageNotFoundComponent } from './content/page-not-found/page-not-found.component';


const routes: Routes = [
  {path: 'login', component:LoginComponent},
  {path: '', component:HomeComponent},
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
