/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { WxInfoDetailComponent } from 'app/entities/wx-info/wx-info-detail.component';
import { WxInfo } from 'app/shared/model/wx-info.model';

describe('Component Tests', () => {
    describe('WxInfo Management Detail Component', () => {
        let comp: WxInfoDetailComponent;
        let fixture: ComponentFixture<WxInfoDetailComponent>;
        const route = ({ data: of({ wxInfo: new WxInfo(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [WxInfoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(WxInfoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WxInfoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.wxInfo).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
