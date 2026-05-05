import { Component, OnInit } from '@angular/core';
import { CompensationService } from './services/compensation.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  constructor(private compensationService: CompensationService) {}

  ngOnInit(): void {
    this.compensationService.getHealth().subscribe({ error: () => {} });
  }
}
