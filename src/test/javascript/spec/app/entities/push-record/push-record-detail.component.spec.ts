/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { PushRecordDetailComponent } from 'app/entities/push-record/push-record-detail.component';
import { PushRecord } from 'app/shared/model/push-record.model';

describe('Component Tests', () => {
    describe('PushRecord Management Detail Component', () => {
        let comp: PushRecordDetailComponent;
        let fixture: ComponentFixture<PushRecordDetailComponent>;
        const route = ({ data: of({ pushRecord: new PushRecord(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [PushRecordDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PushRecordDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PushRecordDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.pushRecord).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
