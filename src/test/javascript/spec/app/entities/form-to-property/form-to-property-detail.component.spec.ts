/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { FormToPropertyDetailComponent } from 'app/entities/form-to-property/form-to-property-detail.component';
import { FormToProperty } from 'app/shared/model/form-to-property.model';

describe('Component Tests', () => {
    describe('FormToProperty Management Detail Component', () => {
        let comp: FormToPropertyDetailComponent;
        let fixture: ComponentFixture<FormToPropertyDetailComponent>;
        const route = ({ data: of({ formToProperty: new FormToProperty(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [FormToPropertyDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FormToPropertyDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FormToPropertyDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.formToProperty).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
