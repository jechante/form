/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FormTestModule } from '../../../test.module';
import { BaseFormDeleteDialogComponent } from 'app/entities/base-form/base-form-delete-dialog.component';
import { BaseFormService } from 'app/entities/base-form/base-form.service';

describe('Component Tests', () => {
    describe('BaseForm Management Delete Component', () => {
        let comp: BaseFormDeleteDialogComponent;
        let fixture: ComponentFixture<BaseFormDeleteDialogComponent>;
        let service: BaseFormService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [BaseFormDeleteDialogComponent]
            })
                .overrideTemplate(BaseFormDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BaseFormDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BaseFormService);
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
