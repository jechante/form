/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FormTestModule } from '../../../test.module';
import { AlgorithmDeleteDialogComponent } from 'app/entities/algorithm/algorithm-delete-dialog.component';
import { AlgorithmService } from 'app/entities/algorithm/algorithm.service';

describe('Component Tests', () => {
    describe('Algorithm Management Delete Component', () => {
        let comp: AlgorithmDeleteDialogComponent;
        let fixture: ComponentFixture<AlgorithmDeleteDialogComponent>;
        let service: AlgorithmService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [AlgorithmDeleteDialogComponent]
            })
                .overrideTemplate(AlgorithmDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AlgorithmDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AlgorithmService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
