/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { FormFieldUpdateComponent } from 'app/entities/form-field/form-field-update.component';
import { FormFieldService } from 'app/entities/form-field/form-field.service';
import { FormField } from 'app/shared/model/form-field.model';

describe('Component Tests', () => {
    describe('FormField Management Update Component', () => {
        let comp: FormFieldUpdateComponent;
        let fixture: ComponentFixture<FormFieldUpdateComponent>;
        let service: FormFieldService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [FormFieldUpdateComponent]
            })
                .overrideTemplate(FormFieldUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FormFieldUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FormFieldService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new FormField(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.formField = entity;
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
                    const entity = new FormField();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.formField = entity;
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
