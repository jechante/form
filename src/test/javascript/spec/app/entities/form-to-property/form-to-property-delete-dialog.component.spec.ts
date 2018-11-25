/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FormTestModule } from '../../../test.module';
import { FormToPropertyDeleteDialogComponent } from 'app/entities/form-to-property/form-to-property-delete-dialog.component';
import { FormToPropertyService } from 'app/entities/form-to-property/form-to-property.service';

describe('Component Tests', () => {
    describe('FormToProperty Management Delete Component', () => {
        let comp: FormToPropertyDeleteDialogComponent;
        let fixture: ComponentFixture<FormToPropertyDeleteDialogComponent>;
        let service: FormToPropertyService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [FormToPropertyDeleteDialogComponent]
            })
                .overrideTemplate(FormToPropertyDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FormToPropertyDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FormToPropertyService);
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
