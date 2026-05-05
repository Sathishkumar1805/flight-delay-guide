import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ClaimWizardComponent } from './pages/claim-wizard/claim-wizard.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'claim', component: ClaimWizardComponent },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
