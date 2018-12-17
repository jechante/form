/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { UserDemandUpdateComponent } from 'app/entities/user-demand/user-demand-update.component';
import { UserDemandService } from 'app/entities/user-demand/user-demand.service';
import { UserDemand } from 'app/shared/model/user-demand.model';

describe('Component Tests', () => {
    describe('UserDemand Management Update Component', () => {
        let comp: UserDemandUpdateComponent;
        let fixture: ComponentFixture<UserDemandUpdateComponent>;
        let service: UserDemandService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [UserDemandUpdateComponent]
            })
                .overrideTemplate(UserDemandUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserDemandUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserDemandService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new UserDemand(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.userDemand = entity;
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
                    const entity = new UserDemand();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.userDemand = entity;
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
