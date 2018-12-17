/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { BasePropertyDetailComponent } from 'app/entities/base-property/base-property-detail.component';
import { BaseProperty } from 'app/shared/model/base-property.model';

describe('Component Tests', () => {
    describe('BaseProperty Management Detail Component', () => {
        let comp: BasePropertyDetailComponent;
        let fixture: ComponentFixture<BasePropertyDetailComponent>;
        const route = ({ data: of({ baseProperty: new BaseProperty(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [BasePropertyDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BasePropertyDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BasePropertyDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.baseProperty).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
