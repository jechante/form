import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IFormToProperty } from 'app/shared/model/form-to-property.model';
import { FormToPropertyService } from './form-to-property.service';

@Component({
    selector: 'jhi-form-to-property-update',
    templateUrl: './form-to-property-update.component.html'
})
export class FormToPropertyUpdateComponent implements OnInit {
    private _formToProperty: IFormToProperty;
    isSaving: boolean;

    constructor(private formToPropertyService: FormToPropertyService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ formToProperty }) => {
            this.formToProperty = formToProperty;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.formToProperty.id !== undefined) {
            this.subscribeToSaveResponse(this.formToPropertyService.update(this.formToProperty));
        } else {
            this.subscribeToSaveResponse(this.formToPropertyService.create(this.formToProperty));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFormToProperty>>) {
        result.subscribe((res: HttpResponse<IFormToProperty>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get formToProperty() {
        return this._formToProperty;
    }

    set formToProperty(formToProperty: IFormToProperty) {
        this._formToProperty = formToProperty;
    }
}
