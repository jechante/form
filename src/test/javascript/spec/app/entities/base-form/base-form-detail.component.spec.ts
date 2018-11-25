/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { BaseFormDetailComponent } from 'app/entities/base-form/base-form-detail.component';
import { BaseForm } from 'app/shared/model/base-form.model';

describe('Component Tests', () => {
    describe('BaseForm Management Detail Component', () => {
        let comp: BaseFormDetailComponent;
        let fixture: ComponentFixture<BaseFormDetailComponent>;
        const route = ({ data: of({ baseForm: new BaseForm(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [BaseFormDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BaseFormDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BaseFormDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.baseForm).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
