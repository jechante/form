import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IAlgorithm } from 'app/shared/model/algorithm.model';
import { AlgorithmService } from './algorithm.service';
import { IBaseProperty } from 'app/shared/model/base-property.model';
import { BasePropertyService } from 'app/entities/base-property';

@Component({
    selector: 'jhi-algorithm-update',
    templateUrl: './algorithm-update.component.html'
})
export class AlgorithmUpdateComponent implements OnInit {
    private _algorithm: IAlgorithm;
    isSaving: boolean;

    baseproperties: IBaseProperty[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private algorithmService: AlgorithmService,
        private basePropertyService: BasePropertyService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ algorithm }) => {
            this.algorithm = algorithm;
        });
        this.basePropertyService.findAll().subscribe(
            (res: HttpResponse<IBaseProperty[]>) => {
                this.baseproperties = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.algorithm.id !== undefined) {
            this.subscribeToSaveResponse(this.algorithmService.update(this.algorithm));
        } else {
            this.subscribeToSaveResponse(this.algorithmService.create(this.algorithm));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAlgorithm>>) {
        result.subscribe((res: HttpResponse<IAlgorithm>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackBasePropertyById(index: number, item: IBaseProperty) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get algorithm() {
        return this._algorithm;
    }

    set algorithm(algorithm: IAlgorithm) {
        this._algorithm = algorithm;
    }
}
