import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FlightIncidentRequest } from '../../models/flight-incident';

interface ToggleOption {
  value: string;
  label: string;
  icon: string;
  description: string;
}

@Component({
  selector: 'app-incident-form',
  templateUrl: './incident-form.component.html',
  styleUrls: ['./incident-form.component.scss']
})
export class IncidentFormComponent implements OnInit {
  @Output() stepComplete = new EventEmitter<Partial<FlightIncidentRequest>>();

  incidentType = '';
  delayDuration = '';
  airlineReason = '';
  submitted = false;

  incidentTypes: ToggleOption[] = [
    { value: 'DELAY', label: 'Delay', icon: '⏱', description: 'Flight arrived late' },
    { value: 'CANCELLATION', label: 'Cancellation', icon: '✕', description: 'Flight was cancelled' },
    { value: 'DENIED_BOARDING', label: 'Denied Boarding', icon: '🚫', description: 'Refused boarding (overbooking)' },
  ];

  delayDurations: ToggleOption[] = [
    { value: '2', label: '2–3 hours', icon: '⏱', description: 'Between 2 and 3 hours' },
    { value: '3', label: '3–4 hours', icon: '⏲', description: 'Between 3 and 4 hours' },
    { value: '4', label: '4+ hours', icon: '🕐', description: 'More than 4 hours' },
  ];

  airlineReasons: ToggleOption[] = [
    { value: 'TECHNICAL', label: 'Technical Issue', icon: '🔧', description: 'Aircraft technical problem' },
    { value: 'WEATHER', label: 'Bad Weather', icon: '⛈', description: 'Adverse weather conditions' },
    { value: 'ATC', label: 'Air Traffic Control', icon: '📡', description: 'ATC restrictions or strikes' },
    { value: 'STRIKE', label: 'Strike', icon: '✊', description: 'Airline staff strike' },
    { value: 'UNKNOWN', label: "Don't Know", icon: '❓', description: 'Reason not given' },
  ];

  ngOnInit(): void {}

  get showDelayDuration(): boolean {
    return this.incidentType === 'DELAY';
  }

  get isValid(): boolean {
    if (!this.incidentType) return false;
    if (this.incidentType === 'DELAY' && !this.delayDuration) return false;
    return true;
  }

  onSubmit(): void {
    this.submitted = true;
    if (!this.isValid) return;
    this.stepComplete.emit({
      incidentType: this.incidentType,
      delayDuration: this.delayDuration ? Number(this.delayDuration) : undefined,
      airlineReason: this.airlineReason || undefined,
    });
  }
}
