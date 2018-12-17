import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAlgorithm } from 'app/shared/model/algorithm.model';
import { AlgorithmService } from './algorithm.service';

@Component({
    selector: 'jhi-algorithm-delete-dialog',
    templateUrl: './algorithm-delete-dialog.component.html'
})
export class AlgorithmDeleteDialogComponent {
    algorithm: IAlgorithm;

    constructor(private algorithmService: AlgorithmService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.algorithmService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'algorithmListModification',
                content: 'Deleted an algorithm'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-algorithm-delete-popup',
    template: ''
})
export class AlgorithmDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ algorithm }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AlgorithmDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.algorithm = algorithm;
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
