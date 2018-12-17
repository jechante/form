/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { UserPropertyUpdateComponent } from 'app/entities/user-property/user-property-update.component';
import { UserPropertyService } from 'app/entities/user-property/user-property.service';
import { UserProperty } from 'app/shared/model/user-property.model';

describe('Component Tests', () => {
    describe('UserProperty Management Update Component', () => {
        let comp: UserPropertyUpdateComponent;
        let fixture: ComponentFixture<UserPropertyUpdateComponent>;
        let service: UserPropertyService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [UserPropertyUpdateComponent]
            })
                .overrideTemplate(UserPropertyUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserPropertyUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserPropertyService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new UserProperty(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.userProperty = entity;
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
                    const entity = new UserProperty();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.userProperty = entity;
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
