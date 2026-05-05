export interface FlightVerification {
  flightNumber: string;
  date: string;
  airline: string;
  status: string;
  scheduledDeparture?: string;
  actualDeparture?: string;
  delayMinutes?: number;
  verified: boolean;
  source?: string;
}
