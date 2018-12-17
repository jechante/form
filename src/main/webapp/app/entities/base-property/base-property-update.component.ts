import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IBaseProperty } from 'app/shared/model/base-property.model';
import { BasePropertyService } from './base-property.service';
import { IAlgorithm } from 'app/shared/model/algorithm.model';
import { AlgorithmService } from 'app/entities/algorithm';

@Component({
    selector: 'jhi-base-property-update',
    templateUrl: './base-property-update.component.html'
})
export class BasePropertyUpdateComponent implements OnInit {
    private _baseProperty: IBaseProperty;
    isSaving: boolean;

    algorithms: IAlgorithm[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private basePropertyService: BasePropertyService,
        private algorithmService: AlgorithmService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ baseProperty }) => {
            this.baseProperty = baseProperty;
        });
        this.algorithmService.query().subscribe(
            (res: HttpResponse<IAlgorithm[]>) => {
                this.algorithms = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.baseProperty.id !== undefined) {
            this.subscribeToSaveResponse(this.basePropertyService.update(this.baseProperty));
        } else {
            this.subscribeToSaveResponse(this.basePropertyService.create(this.baseProperty));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBaseProperty>>) {
        result.subscribe((res: HttpResponse<IBaseProperty>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackAlgorithmById(index: number, item: IAlgorithm) {
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
    get baseProperty() {
        return this._baseProperty;
    }

    set baseProperty(baseProperty: IBaseProperty) {
        this._baseProperty = baseProperty;
    }
}
