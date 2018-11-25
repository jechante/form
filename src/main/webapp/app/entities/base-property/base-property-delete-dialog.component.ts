import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBaseProperty } from 'app/shared/model/base-property.model';
import { BasePropertyService } from './base-property.service';

@Component({
    selector: 'jhi-base-property-delete-dialog',
    templateUrl: './base-property-delete-dialog.component.html'
})
export class BasePropertyDeleteDialogComponent {
    baseProperty: IBaseProperty;

    constructor(
        private basePropertyService: BasePropertyService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.basePropertyService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'basePropertyListModification',
                content: 'Deleted an baseProperty'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-base-property-delete-popup',
    template: ''
})
export class BasePropertyDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ baseProperty }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BasePropertyDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.baseProperty = baseProperty;
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
