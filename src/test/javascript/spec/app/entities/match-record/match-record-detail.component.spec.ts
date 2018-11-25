/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { MatchRecordDetailComponent } from 'app/entities/match-record/match-record-detail.component';
import { MatchRecord } from 'app/shared/model/match-record.model';

describe('Component Tests', () => {
    describe('MatchRecord Management Detail Component', () => {
        let comp: MatchRecordDetailComponent;
        let fixture: ComponentFixture<MatchRecordDetailComponent>;
        const route = ({ data: of({ matchRecord: new MatchRecord(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [MatchRecordDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(MatchRecordDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MatchRecordDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.matchRecord).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
