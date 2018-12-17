/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { UserDemandDetailComponent } from 'app/entities/user-demand/user-demand-detail.component';
import { UserDemand } from 'app/shared/model/user-demand.model';

describe('Component Tests', () => {
    describe('UserDemand Management Detail Component', () => {
        let comp: UserDemandDetailComponent;
        let fixture: ComponentFixture<UserDemandDetailComponent>;
        const route = ({ data: of({ userDemand: new UserDemand(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [UserDemandDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(UserDemandDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserDemandDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.userDemand).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
