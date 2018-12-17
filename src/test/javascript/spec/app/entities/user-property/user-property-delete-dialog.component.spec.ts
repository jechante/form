/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FormTestModule } from '../../../test.module';
import { UserPropertyDeleteDialogComponent } from 'app/entities/user-property/user-property-delete-dialog.component';
import { UserPropertyService } from 'app/entities/user-property/user-property.service';

describe('Component Tests', () => {
    describe('UserProperty Management Delete Component', () => {
        let comp: UserPropertyDeleteDialogComponent;
        let fixture: ComponentFixture<UserPropertyDeleteDialogComponent>;
        let service: UserPropertyService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [UserPropertyDeleteDialogComponent]
            })
                .overrideTemplate(UserPropertyDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserPropertyDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserPropertyService);
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
