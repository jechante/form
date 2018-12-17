/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormTestModule } from '../../../test.module';
import { AlgorithmDetailComponent } from 'app/entities/algorithm/algorithm-detail.component';
import { Algorithm } from 'app/shared/model/algorithm.model';

describe('Component Tests', () => {
    describe('Algorithm Management Detail Component', () => {
        let comp: AlgorithmDetailComponent;
        let fixture: ComponentFixture<AlgorithmDetailComponent>;
        const route = ({ data: of({ algorithm: new Algorithm(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [AlgorithmDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AlgorithmDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AlgorithmDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.algorithm).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
