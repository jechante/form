/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { UserBaseDetailComponent } from 'app/entities/user-base/user-base-detail.component';
import { UserBase } from 'app/shared/model/user-base.model';

describe('Component Tests', () => {
    describe('UserBase Management Detail Component', () => {
        let comp: UserBaseDetailComponent;
        let fixture: ComponentFixture<UserBaseDetailComponent>;
        const route = ({ data: of({ userBase: new UserBase(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [UserBaseDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(UserBaseDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserBaseDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.userBase).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
