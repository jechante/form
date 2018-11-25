import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBaseForm } from 'app/shared/model/base-form.model';
import { BaseFormService } from './base-form.service';

@Component({
    selector: 'jhi-base-form-delete-dialog',
    templateUrl: './base-form-delete-dialog.component.html'
})
export class BaseFormDeleteDialogComponent {
    baseForm: IBaseForm;

    constructor(private baseFormService: BaseFormService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.baseFormService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'baseFormListModification',
                content: 'Deleted an baseForm'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-base-form-delete-popup',
    template: ''
})
export class BaseFormDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ baseForm }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BaseFormDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.baseForm = baseForm;
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
