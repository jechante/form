import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWxInfo } from 'app/shared/model/wx-info.model';
import { WxInfoService } from './wx-info.service';

@Component({
    selector: 'jhi-wx-info-delete-dialog',
    templateUrl: './wx-info-delete-dialog.component.html'
})
export class WxInfoDeleteDialogComponent {
    wxInfo: IWxInfo;

    constructor(private wxInfoService: WxInfoService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.wxInfoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'wxInfoListModification',
                content: 'Deleted an wxInfo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-wx-info-delete-popup',
    template: ''
})
export class WxInfoDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ wxInfo }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(WxInfoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.wxInfo = wxInfo;
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
