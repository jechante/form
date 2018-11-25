/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { UserBaseUpdateComponent } from 'app/entities/user-base/user-base-update.component';
import { UserBaseService } from 'app/entities/user-base/user-base.service';
import { UserBase } from 'app/shared/model/user-base.model';

describe('Component Tests', () => {
    describe('UserBase Management Update Component', () => {
        let comp: UserBaseUpdateComponent;
        let fixture: ComponentFixture<UserBaseUpdateComponent>;
        let service: UserBaseService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [UserBaseUpdateComponent]
            })
                .overrideTemplate(UserBaseUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserBaseUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserBaseService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new UserBase(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.userBase = entity;
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
                    const entity = new UserBase();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.userBase = entity;
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
