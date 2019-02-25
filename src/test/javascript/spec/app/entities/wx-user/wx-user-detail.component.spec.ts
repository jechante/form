/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { WxUserDetailComponent } from 'app/entities/wx-user/wx-user-detail.component';
import { WxUser } from 'app/shared/model/wx-user.model';

describe('Component Tests', () => {
    describe('WxUser Management Detail Component', () => {
        let comp: WxUserDetailComponent;
        let fixture: ComponentFixture<WxUserDetailComponent>;
        const route = ({ data: of({ wxUser: new WxUser('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [WxUserDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(WxUserDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WxUserDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.wxUser).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
