/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { BasePropertyUpdateComponent } from 'app/entities/base-property/base-property-update.component';
import { BasePropertyService } from 'app/entities/base-property/base-property.service';
import { BaseProperty } from 'app/shared/model/base-property.model';

describe('Component Tests', () => {
    describe('BaseProperty Management Update Component', () => {
        let comp: BasePropertyUpdateComponent;
        let fixture: ComponentFixture<BasePropertyUpdateComponent>;
        let service: BasePropertyService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [BasePropertyUpdateComponent]
            })
                .overrideTemplate(BasePropertyUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BasePropertyUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BasePropertyService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new BaseProperty(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.baseProperty = entity;
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
                    const entity = new BaseProperty();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.baseProperty = entity;
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
