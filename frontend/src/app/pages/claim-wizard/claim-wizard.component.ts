import { Component } from '@angular/core';
import { FlightIncidentRequest } from '../../models/flight-incident';
import { CompensationResult } from '../../models/compensation-result';

type WizardStep = 'flight' | 'incident' | 'analyzing' | 'results';

@Component({
  selector: 'app-claim-wizard',
  templateUrl: './claim-wizard.component.html',
  styleUrls: ['./claim-wizard.component.scss']
})
export class ClaimWizardComponent {
  currentStep: WizardStep = 'flight';
  formData: Partial<FlightIncidentRequest> = {};
  result: CompensationResult | null = null;
  errorMessage = '';

  steps: { key: WizardStep; label: string }[] = [
    { key: 'flight', label: 'Flight' },
    { key: 'incident', label: 'Incident' },
    { key: 'analyzing', label: 'Analyzing' },
    { key: 'results', label: 'Results' },
  ];

  get stepIndex(): number {
    return this.steps.findIndex(s => s.key === this.currentStep);
  }

  get fullRequest(): FlightIncidentRequest {
    return this.formData as FlightIncidentRequest;
  }

  onFlightStepComplete(data: Partial<FlightIncidentRequest>): void {
    this.formData = { ...this.formData, ...data };
    this.currentStep = 'incident';
  }

  onIncidentStepComplete(data: Partial<FlightIncidentRequest>): void {
    this.formData = { ...this.formData, ...data };
    this.currentStep = 'analyzing';
  }

  onAnalysisComplete(result: CompensationResult): void {
    this.result = result;
    this.currentStep = 'results';
  }

  onAnalysisError(message: string): void {
    this.errorMessage = message;
    this.currentStep = 'incident';
  }

  onStartOver(): void {
    this.formData = {};
    this.result = null;
    this.errorMessage = '';
    this.currentStep = 'flight';
  }
}
