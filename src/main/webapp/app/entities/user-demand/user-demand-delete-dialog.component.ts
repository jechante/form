import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserDemand } from 'app/shared/model/user-demand.model';
import { UserDemandService } from './user-demand.service';

@Component({
    selector: 'jhi-user-demand-delete-dialog',
    templateUrl: './user-demand-delete-dialog.component.html'
})
export class UserDemandDeleteDialogComponent {
    userDemand: IUserDemand;

    constructor(private userDemandService: UserDemandService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userDemandService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'userDemandListModification',
                content: 'Deleted an userDemand'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-demand-delete-popup',
    template: ''
})
export class UserDemandDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userDemand }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(UserDemandDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.userDemand = userDemand;
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
