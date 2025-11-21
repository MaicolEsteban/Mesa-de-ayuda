import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CambiarContrasenaPrimerLoginComponent } from './cambiar-contrasena-primer-login';

describe('CambiarContrasenaPrimerLogin', () => {
  let component: CambiarContrasenaPrimerLoginComponent;
  let fixture: ComponentFixture<CambiarContrasenaPrimerLoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CambiarContrasenaPrimerLoginComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CambiarContrasenaPrimerLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
