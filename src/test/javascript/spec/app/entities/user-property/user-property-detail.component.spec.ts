/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { UserPropertyDetailComponent } from 'app/entities/user-property/user-property-detail.component';
import { UserProperty } from 'app/shared/model/user-property.model';

describe('Component Tests', () => {
    describe('UserProperty Management Detail Component', () => {
        let comp: UserPropertyDetailComponent;
        let fixture: ComponentFixture<UserPropertyDetailComponent>;
        const route = ({ data: of({ userProperty: new UserProperty(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [UserPropertyDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(UserPropertyDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserPropertyDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.userProperty).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
