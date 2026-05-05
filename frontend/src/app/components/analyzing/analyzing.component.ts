import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CompensationService } from '../../services/compensation.service';
import { FlightIncidentRequest } from '../../models/flight-incident';
import { CompensationResult } from '../../models/compensation-result';

interface ProcessingStep {
  label: string;
  done: boolean;
  active: boolean;
}

@Component({
  selector: 'app-analyzing',
  templateUrl: './analyzing.component.html',
  styleUrls: ['./analyzing.component.scss']
})
export class AnalyzingComponent implements OnInit {
  @Input() request!: FlightIncidentRequest;
  @Output() analysisComplete = new EventEmitter<CompensationResult>();
  @Output() analysisError = new EventEmitter<string>();

  steps: ProcessingStep[] = [
    { label: 'Verifying flight details', done: false, active: false },
    { label: 'Checking applicable regulations', done: false, active: false },
    { label: 'Calculating compensation amount', done: false, active: false },
    { label: 'Generating demand letter', done: false, active: false },
  ];

  constructor(private compensationService: CompensationService) {}

  ngOnInit(): void {
    this.runSteps();
  }

  private runSteps(): void {
    let delay = 0;
    this.steps.forEach((step, i) => {
      setTimeout(() => { step.active = true; }, delay);
      delay += 900;
      setTimeout(() => { step.done = true; step.active = false; }, delay);
      delay += 200;
    });

    setTimeout(() => {
      this.compensationService.analyzeIncident(this.request).subscribe({
        next: result => this.analysisComplete.emit(result),
        error: (err: Error) => this.analysisError.emit(err.message),
      });
    }, delay - 400);
  }
}
