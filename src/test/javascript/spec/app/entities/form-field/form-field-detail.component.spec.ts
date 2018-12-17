/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { FormFieldDetailComponent } from 'app/entities/form-field/form-field-detail.component';
import { FormField } from 'app/shared/model/form-field.model';

describe('Component Tests', () => {
    describe('FormField Management Detail Component', () => {
        let comp: FormFieldDetailComponent;
        let fixture: ComponentFixture<FormFieldDetailComponent>;
        const route = ({ data: of({ formField: new FormField(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [FormFieldDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FormFieldDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FormFieldDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.formField).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
