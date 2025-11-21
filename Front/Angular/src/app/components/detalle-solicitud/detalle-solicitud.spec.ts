import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleSolicitudComponent } from './detalle-solicitud';

describe('DetalleSolicitud', () => {
  let component: DetalleSolicitudComponent;
  let fixture: ComponentFixture<DetalleSolicitudComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleSolicitudComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetalleSolicitudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
