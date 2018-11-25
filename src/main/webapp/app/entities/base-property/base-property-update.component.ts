import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IBaseProperty } from 'app/shared/model/base-property.model';
import { BasePropertyService } from './base-property.service';

@Component({
    selector: 'jhi-base-property-update',
    templateUrl: './base-property-update.component.html'
})
export class BasePropertyUpdateComponent implements OnInit {
    private _baseProperty: IBaseProperty;
    isSaving: boolean;

    constructor(private basePropertyService: BasePropertyService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ baseProperty }) => {
            this.baseProperty = baseProperty;
        });
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
    get baseProperty() {
        return this._baseProperty;
    }

    set baseProperty(baseProperty: IBaseProperty) {
        this._baseProperty = baseProperty;
    }
}
