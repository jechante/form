/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FormTestModule } from '../../../test.module';
import { FormSubmitDeleteDialogComponent } from 'app/entities/form-submit/form-submit-delete-dialog.component';
import { FormSubmitService } from 'app/entities/form-submit/form-submit.service';

describe('Component Tests', () => {
    describe('FormSubmit Management Delete Component', () => {
        let comp: FormSubmitDeleteDialogComponent;
        let fixture: ComponentFixture<FormSubmitDeleteDialogComponent>;
        let service: FormSubmitService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [FormSubmitDeleteDialogComponent]
            })
                .overrideTemplate(FormSubmitDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FormSubmitDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FormSubmitService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
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
            ));
        });
    });
});
