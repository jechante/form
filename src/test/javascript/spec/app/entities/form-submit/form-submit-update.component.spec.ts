/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { FormSubmitUpdateComponent } from 'app/entities/form-submit/form-submit-update.component';
import { FormSubmitService } from 'app/entities/form-submit/form-submit.service';
import { FormSubmit } from 'app/shared/model/form-submit.model';

describe('Component Tests', () => {
    describe('FormSubmit Management Update Component', () => {
        let comp: FormSubmitUpdateComponent;
        let fixture: ComponentFixture<FormSubmitUpdateComponent>;
        let service: FormSubmitService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [FormSubmitUpdateComponent]
            })
                .overrideTemplate(FormSubmitUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FormSubmitUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FormSubmitService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new FormSubmit(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.formSubmit = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new FormSubmit();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.formSubmit = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
