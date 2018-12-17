/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { PushRecordUpdateComponent } from 'app/entities/push-record/push-record-update.component';
import { PushRecordService } from 'app/entities/push-record/push-record.service';
import { PushRecord } from 'app/shared/model/push-record.model';

describe('Component Tests', () => {
    describe('PushRecord Management Update Component', () => {
        let comp: PushRecordUpdateComponent;
        let fixture: ComponentFixture<PushRecordUpdateComponent>;
        let service: PushRecordService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [PushRecordUpdateComponent]
            })
                .overrideTemplate(PushRecordUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PushRecordUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PushRecordService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new PushRecord(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.pushRecord = entity;
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
                    const entity = new PushRecord();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.pushRecord = entity;
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
