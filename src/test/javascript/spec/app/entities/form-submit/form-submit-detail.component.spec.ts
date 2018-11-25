/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { FormSubmitDetailComponent } from 'app/entities/form-submit/form-submit-detail.component';
import { FormSubmit } from 'app/shared/model/form-submit.model';

describe('Component Tests', () => {
    describe('FormSubmit Management Detail Component', () => {
        let comp: FormSubmitDetailComponent;
        let fixture: ComponentFixture<FormSubmitDetailComponent>;
        const route = ({ data: of({ formSubmit: new FormSubmit(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [FormSubmitDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FormSubmitDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FormSubmitDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.formSubmit).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
