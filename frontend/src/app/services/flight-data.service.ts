import { Injectable } from '@angular/core';

export interface Airline {
  iata: string;
  name: string;
}

export interface Country {
  code: string;
  name: string;
  region: 'EU' | 'UK' | 'US' | 'OTHER';
}

@Injectable({ providedIn: 'root' })
export class FlightDataService {
  private airlines: Airline[] = [
    { iata: 'BA', name: 'British Airways' },
    { iata: 'AF', name: 'Air France' },
    { iata: 'LH', name: 'Lufthansa' },
    { iata: 'KL', name: 'KLM Royal Dutch Airlines' },
    { iata: 'AZ', name: 'ITA Airways' },
    { iata: 'IB', name: 'Iberia' },
    { iata: 'SK', name: 'Scandinavian Airlines' },
    { iata: 'AY', name: 'Finnair' },
    { iata: 'EI', name: 'Aer Lingus' },
    { iata: 'VY', name: 'Vueling' },
    { iata: 'FR', name: 'Ryanair' },
    { iata: 'U2', name: 'easyJet' },
    { iata: 'W6', name: 'Wizz Air' },
    { iata: 'PC', name: 'Pegasus Airlines' },
    { iata: 'EW', name: 'Eurowings' },
    { iata: 'LO', name: 'LOT Polish Airlines' },
    { iata: 'TP', name: 'TAP Air Portugal' },
    { iata: 'OK', name: 'Czech Airlines' },
    { iata: 'RO', name: 'TAROM' },
    { iata: 'BT', name: 'Air Baltic' },
    { iata: 'AA', name: 'American Airlines' },
    { iata: 'DL', name: 'Delta Air Lines' },
    { iata: 'UA', name: 'United Airlines' },
    { iata: 'VS', name: 'Virgin Atlantic' },
    { iata: 'WN', name: 'Southwest Airlines' },
    { iata: 'QF', name: 'Qantas' },
    { iata: 'EK', name: 'Emirates' },
    { iata: 'SQ', name: 'Singapore Airlines' },
    { iata: 'CX', name: 'Cathay Pacific' },
    { iata: 'TK', name: 'Turkish Airlines' },
    { iata: 'QR', name: 'Qatar Airways' },
    { iata: 'LX', name: 'Swiss International Air Lines' },
    { iata: 'OS', name: 'Austrian Airlines' },
    { iata: 'SN', name: 'Brussels Airlines' },
    { iata: 'G3', name: 'Gol Transportes Aéreos' },
  ];

  private countries: Country[] = [
    { code: 'DE', name: 'Germany', region: 'EU' },
    { code: 'FR', name: 'France', region: 'EU' },
    { code: 'ES', name: 'Spain', region: 'EU' },
    { code: 'IT', name: 'Italy', region: 'EU' },
    { code: 'NL', name: 'Netherlands', region: 'EU' },
    { code: 'BE', name: 'Belgium', region: 'EU' },
    { code: 'SE', name: 'Sweden', region: 'EU' },
    { code: 'DK', name: 'Denmark', region: 'EU' },
    { code: 'FI', name: 'Finland', region: 'EU' },
    { code: 'AT', name: 'Austria', region: 'EU' },
    { code: 'IE', name: 'Ireland', region: 'EU' },
    { code: 'PL', name: 'Poland', region: 'EU' },
    { code: 'PT', name: 'Portugal', region: 'EU' },
    { code: 'GR', name: 'Greece', region: 'EU' },
    { code: 'CZ', name: 'Czech Republic', region: 'EU' },
    { code: 'HU', name: 'Hungary', region: 'EU' },
    { code: 'RO', name: 'Romania', region: 'EU' },
    { code: 'BG', name: 'Bulgaria', region: 'EU' },
    { code: 'HR', name: 'Croatia', region: 'EU' },
    { code: 'SK', name: 'Slovakia', region: 'EU' },
    { code: 'SI', name: 'Slovenia', region: 'EU' },
    { code: 'LT', name: 'Lithuania', region: 'EU' },
    { code: 'LV', name: 'Latvia', region: 'EU' },
    { code: 'EE', name: 'Estonia', region: 'EU' },
    { code: 'LU', name: 'Luxembourg', region: 'EU' },
    { code: 'MT', name: 'Malta', region: 'EU' },
    { code: 'CY', name: 'Cyprus', region: 'EU' },
    { code: 'GB', name: 'United Kingdom', region: 'UK' },
    { code: 'US', name: 'United States', region: 'US' },
    { code: 'CA', name: 'Canada', region: 'OTHER' },
    { code: 'AU', name: 'Australia', region: 'OTHER' },
    { code: 'IN', name: 'India', region: 'OTHER' },
    { code: 'JP', name: 'Japan', region: 'OTHER' },
    { code: 'SG', name: 'Singapore', region: 'OTHER' },
    { code: 'AE', name: 'United Arab Emirates', region: 'OTHER' },
    { code: 'TR', name: 'Turkey', region: 'OTHER' },
    { code: 'CH', name: 'Switzerland', region: 'OTHER' },
    { code: 'NO', name: 'Norway', region: 'OTHER' },
    { code: 'IS', name: 'Iceland', region: 'OTHER' },
    { code: 'ZA', name: 'South Africa', region: 'OTHER' },
  ];

  getAirlines(): Airline[] {
    return this.airlines.sort((a, b) => a.name.localeCompare(b.name));
  }

  getCountries(): Country[] {
    return this.countries.sort((a, b) => a.name.localeCompare(b.name));
  }

  isEUCountry(code: string): boolean {
    return this.countries.some(c => c.code === code && c.region === 'EU');
  }
}
