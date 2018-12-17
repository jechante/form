import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IFormField } from 'app/shared/model/form-field.model';
import { FormFieldService } from './form-field.service';
import { IBaseForm } from 'app/shared/model/base-form.model';
import { BaseFormService } from 'app/entities/base-form';
import { IBaseProperty } from 'app/shared/model/base-property.model';
import { BasePropertyService } from 'app/entities/base-property';

@Component({
    selector: 'jhi-form-field-update',
    templateUrl: './form-field-update.component.html'
})
export class FormFieldUpdateComponent implements OnInit {
    private _formField: IFormField;
    isSaving: boolean;

    baseforms: IBaseForm[];

    baseproperties: IBaseProperty[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private formFieldService: FormFieldService,
        private baseFormService: BaseFormService,
        private basePropertyService: BasePropertyService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ formField }) => {
            this.formField = formField;
        });
        this.baseFormService.query().subscribe(
            (res: HttpResponse<IBaseForm[]>) => {
                this.baseforms = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.basePropertyService.query().subscribe(
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
        if (this.formField.id !== undefined) {
            this.subscribeToSaveResponse(this.formFieldService.update(this.formField));
        } else {
            this.subscribeToSaveResponse(this.formFieldService.create(this.formField));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFormField>>) {
        result.subscribe((res: HttpResponse<IFormField>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackBaseFormById(index: number, item: IBaseForm) {
        return item.id;
    }

    trackBasePropertyById(index: number, item: IBaseProperty) {
        return item.id;
    }
    get formField() {
        return this._formField;
    }

    set formField(formField: IFormField) {
        this._formField = formField;
    }
}
