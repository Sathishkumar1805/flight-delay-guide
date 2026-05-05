import { FlightVerification } from './flight-verification';

export interface CompensationResult {
  eligible: boolean;
  compensationAmount: number;
  currency: string;
  regulation: string;
  regulationDetails: string;
  immediateRights: string[];
  explanation: string;
  demandLetter: string;
  flightVerification?: FlightVerification;
}
