/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FormTestModule } from '../../../test.module';
import { BasePropertyDeleteDialogComponent } from 'app/entities/base-property/base-property-delete-dialog.component';
import { BasePropertyService } from 'app/entities/base-property/base-property.service';

describe('Component Tests', () => {
    describe('BaseProperty Management Delete Component', () => {
        let comp: BasePropertyDeleteDialogComponent;
        let fixture: ComponentFixture<BasePropertyDeleteDialogComponent>;
        let service: BasePropertyService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FormTestModule],
                declarations: [BasePropertyDeleteDialogComponent]
            })
                .overrideTemplate(BasePropertyDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BasePropertyDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BasePropertyService);
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
