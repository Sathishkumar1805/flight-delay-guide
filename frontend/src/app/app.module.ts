import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { FlightFormComponent } from './components/flight-form/flight-form.component';
import { IncidentFormComponent } from './components/incident-form/incident-form.component';
import { AnalyzingComponent } from './components/analyzing/analyzing.component';
import { ResultsComponent } from './components/results/results.component';
import { HomeComponent } from './pages/home/home.component';
import { ClaimWizardComponent } from './pages/claim-wizard/claim-wizard.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FlightFormComponent,
    IncidentFormComponent,
    AnalyzingComponent,
    ResultsComponent,
    HomeComponent,
    ClaimWizardComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
