import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserProperty } from 'app/shared/model/user-property.model';
import { UserPropertyService } from './user-property.service';

@Component({
    selector: 'jhi-user-property-delete-dialog',
    templateUrl: './user-property-delete-dialog.component.html'
})
export class UserPropertyDeleteDialogComponent {
    userProperty: IUserProperty;

    constructor(
        private userPropertyService: UserPropertyService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userPropertyService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'userPropertyListModification',
                content: 'Deleted an userProperty'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-property-delete-popup',
    template: ''
})
export class UserPropertyDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userProperty }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(UserPropertyDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.userProperty = userProperty;
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
