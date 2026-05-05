import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CompensationResult } from '../../models/compensation-result';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.scss']
})
export class ResultsComponent {
  @Input() result!: CompensationResult;
  @Input() flightNumber: string = '';
  @Output() startOver = new EventEmitter<void>();

  copySuccess = false;

  copyLetter(): void {
    navigator.clipboard.writeText(this.result.demandLetter).then(() => {
      this.copySuccess = true;
      setTimeout(() => (this.copySuccess = false), 2500);
    });
  }

  downloadLetter(): void {
    const blob = new Blob([this.result.demandLetter], { type: 'text/plain' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    const name = this.flightNumber ? `demand-letter-${this.flightNumber}.txt` : 'demand-letter.txt';
    a.href = url;
    a.download = name;
    a.click();
    URL.revokeObjectURL(url);
  }
}
