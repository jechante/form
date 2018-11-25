/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { WxInfoUpdateComponent } from 'app/entities/wx-info/wx-info-update.component';
import { WxInfoService } from 'app/entities/wx-info/wx-info.service';
import { WxInfo } from 'app/shared/model/wx-info.model';

describe('Component Tests', () => {
    describe('WxInfo Management Update Component', () => {
        let comp: WxInfoUpdateComponent;
        let fixture: ComponentFixture<WxInfoUpdateComponent>;
        let service: WxInfoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [WxInfoUpdateComponent]
            })
                .overrideTemplate(WxInfoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WxInfoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WxInfoService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new WxInfo(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.wxInfo = entity;
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
                    const entity = new WxInfo();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.wxInfo = entity;
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
