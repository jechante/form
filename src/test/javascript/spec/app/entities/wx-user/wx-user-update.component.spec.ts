/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { WxUserUpdateComponent } from 'app/entities/wx-user/wx-user-update.component';
import { WxUserService } from 'app/entities/wx-user/wx-user.service';
import { WxUser } from 'app/shared/model/wx-user.model';

describe('Component Tests', () => {
    describe('WxUser Management Update Component', () => {
        let comp: WxUserUpdateComponent;
        let fixture: ComponentFixture<WxUserUpdateComponent>;
        let service: WxUserService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [WxUserUpdateComponent]
            })
                .overrideTemplate(WxUserUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WxUserUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WxUserService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new WxUser(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.wxUser = entity;
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
                    const entity = new WxUser();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.wxUser = entity;
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
