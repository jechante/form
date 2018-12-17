/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FormTestModule } from '../../../test.module';
import { UserMatchDeleteDialogComponent } from 'app/entities/user-match/user-match-delete-dialog.component';
import { UserMatchService } from 'app/entities/user-match/user-match.service';

describe('Component Tests', () => {
    describe('UserMatch Management Delete Component', () => {
        let comp: UserMatchDeleteDialogComponent;
        let fixture: ComponentFixture<UserMatchDeleteDialogComponent>;
        let service: UserMatchService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [UserMatchDeleteDialogComponent]
            })
                .overrideTemplate(UserMatchDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserMatchDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserMatchService);
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
