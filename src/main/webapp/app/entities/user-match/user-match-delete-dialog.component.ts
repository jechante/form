import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserMatch } from 'app/shared/model/user-match.model';
import { UserMatchService } from './user-match.service';

@Component({
    selector: 'jhi-user-match-delete-dialog',
    templateUrl: './user-match-delete-dialog.component.html'
})
export class UserMatchDeleteDialogComponent {
    userMatch: IUserMatch;

    constructor(private userMatchService: UserMatchService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userMatchService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'userMatchListModification',
                content: 'Deleted an userMatch'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-match-delete-popup',
    template: ''
})
export class UserMatchDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userMatch }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(UserMatchDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.userMatch = userMatch;
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
