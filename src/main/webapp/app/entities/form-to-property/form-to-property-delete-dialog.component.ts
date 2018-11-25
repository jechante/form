import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFormToProperty } from 'app/shared/model/form-to-property.model';
import { FormToPropertyService } from './form-to-property.service';

@Component({
    selector: 'jhi-form-to-property-delete-dialog',
    templateUrl: './form-to-property-delete-dialog.component.html'
})
export class FormToPropertyDeleteDialogComponent {
    formToProperty: IFormToProperty;

    constructor(
        private formToPropertyService: FormToPropertyService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.formToPropertyService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'formToPropertyListModification',
                content: 'Deleted an formToProperty'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-form-to-property-delete-popup',
    template: ''
})
export class FormToPropertyDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ formToProperty }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FormToPropertyDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.formToProperty = formToProperty;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
