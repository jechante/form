import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFormSubmit } from 'app/shared/model/form-submit.model';
import { FormSubmitService } from './form-submit.service';

@Component({
    selector: 'jhi-form-submit-delete-dialog',
    templateUrl: './form-submit-delete-dialog.component.html'
})
export class FormSubmitDeleteDialogComponent {
    formSubmit: IFormSubmit;

    constructor(private formSubmitService: FormSubmitService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.formSubmitService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'formSubmitListModification',
                content: 'Deleted an formSubmit'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-form-submit-delete-popup',
    template: ''
})
export class FormSubmitDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ formSubmit }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FormSubmitDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.formSubmit = formSubmit;
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
