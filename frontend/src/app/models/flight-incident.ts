export interface FlightIncidentRequest {
  flightNumber: string;
  flightDate: string;
  airline: string;
  departureAirport: string;
  arrivalAirport: string;
  departureCountry: string;
  arrivalCountry: string;
  incidentType: string;
  delayDuration?: number;
  airlineReason?: string;
}
