import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FlightDataService, Airline, Country } from '../../services/flight-data.service';
import { FlightIncidentRequest } from '../../models/flight-incident';

@Component({
  selector: 'app-flight-form',
  templateUrl: './flight-form.component.html',
  styleUrls: ['./flight-form.component.scss']
})
export class FlightFormComponent implements OnInit {
  @Output() stepComplete = new EventEmitter<Partial<FlightIncidentRequest>>();

  form!: FormGroup;
  airlines: Airline[] = [];
  countries: Country[] = [];
  submitted = false;

  constructor(private fb: FormBuilder, private flightData: FlightDataService) {}

  ngOnInit(): void {
    this.airlines = this.flightData.getAirlines();
    this.countries = this.flightData.getCountries();

    this.form = this.fb.group({
      flightNumber: ['', [Validators.required, Validators.pattern(/^[A-Z]{2,3}\d{1,4}[A-Z]?$/i)]],
      flightDate: ['', Validators.required],
      airline: ['', Validators.required],
      departureAirport: ['', [Validators.required, Validators.pattern(/^[A-Z]{3}$/i)]],
      arrivalAirport: ['', [Validators.required, Validators.pattern(/^[A-Z]{3}$/i)]],
      departureCountry: ['', Validators.required],
      arrivalCountry: ['', Validators.required],
    });
  }

  get f() { return this.form.controls; }

  onSubmit(): void {
    this.submitted = true;
    if (this.form.invalid) return;
    const val = this.form.value;
    this.stepComplete.emit({
      flightNumber: val.flightNumber.toUpperCase(),
      flightDate: val.flightDate,
      airline: val.airline,
      departureAirport: val.departureAirport.toUpperCase(),
      arrivalAirport: val.arrivalAirport.toUpperCase(),
      departureCountry: val.departureCountry,
      arrivalCountry: val.arrivalCountry,
    });
  }

  maxDate(): string {
    return new Date().toISOString().split('T')[0];
  }
}
