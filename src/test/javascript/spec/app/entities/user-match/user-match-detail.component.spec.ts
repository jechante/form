/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { UserMatchDetailComponent } from 'app/entities/user-match/user-match-detail.component';
import { UserMatch } from 'app/shared/model/user-match.model';

describe('Component Tests', () => {
    describe('UserMatch Management Detail Component', () => {
        let comp: UserMatchDetailComponent;
        let fixture: ComponentFixture<UserMatchDetailComponent>;
        const route = ({ data: of({ userMatch: new UserMatch(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [UserMatchDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(UserMatchDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserMatchDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.userMatch).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
