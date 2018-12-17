/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { BaseFormUpdateComponent } from 'app/entities/base-form/base-form-update.component';
import { BaseFormService } from 'app/entities/base-form/base-form.service';
import { BaseForm } from 'app/shared/model/base-form.model';

describe('Component Tests', () => {
    describe('BaseForm Management Update Component', () => {
        let comp: BaseFormUpdateComponent;
        let fixture: ComponentFixture<BaseFormUpdateComponent>;
        let service: BaseFormService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [BaseFormUpdateComponent]
            })
                .overrideTemplate(BaseFormUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BaseFormUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BaseFormService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new BaseForm(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.baseForm = entity;
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
                    const entity = new BaseForm();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.baseForm = entity;
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
