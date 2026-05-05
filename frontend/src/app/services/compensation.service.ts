import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { FlightIncidentRequest } from '../models/flight-incident';
import { CompensationResult } from '../models/compensation-result';
import { FlightVerification } from '../models/flight-verification';

@Injectable({ providedIn: 'root' })
export class CompensationService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  analyzeIncident(request: FlightIncidentRequest): Observable<CompensationResult> {
    return this.http.post<CompensationResult>(`${this.apiUrl}/compensation/analyze`, request).pipe(
      catchError(this.handleError)
    );
  }

  verifyFlight(flightNumber: string, date: string): Observable<FlightVerification> {
    return this.http.post<FlightVerification>(`${this.apiUrl}/compensation/verify-flight`, { flightNumber, date }).pipe(
      catchError(this.handleError)
    );
  }

  getHealth(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/compensation/health`).pipe(
      catchError(() => throwError(() => null))
    );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let message = 'An unexpected error occurred. Please try again.';
    if (error.status === 0) {
      message = 'Unable to reach the server. Please check your connection.';
    } else if (error.status === 400) {
      message = 'Invalid request. Please check your flight details.';
    } else if (error.status === 404) {
      message = 'Flight not found. Please verify the flight number and date.';
    } else if (error.status >= 500) {
      message = 'Server error. Please try again later.';
    }
    return throwError(() => new Error(message));
  }
}
