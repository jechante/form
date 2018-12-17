import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPushRecord } from 'app/shared/model/push-record.model';
import { PushRecordService } from './push-record.service';

@Component({
    selector: 'jhi-push-record-delete-dialog',
    templateUrl: './push-record-delete-dialog.component.html'
})
export class PushRecordDeleteDialogComponent {
    pushRecord: IPushRecord;

    constructor(private pushRecordService: PushRecordService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pushRecordService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'pushRecordListModification',
                content: 'Deleted an pushRecord'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-push-record-delete-popup',
    template: ''
})
export class PushRecordDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pushRecord }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PushRecordDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.pushRecord = pushRecord;
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
