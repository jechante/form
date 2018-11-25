import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IBaseForm } from 'app/shared/model/base-form.model';
import { BaseFormService } from './base-form.service';

@Component({
    selector: 'jhi-base-form-update',
    templateUrl: './base-form-update.component.html'
})
export class BaseFormUpdateComponent implements OnInit {
    private _baseForm: IBaseForm;
    isSaving: boolean;

    constructor(private baseFormService: BaseFormService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ baseForm }) => {
            this.baseForm = baseForm;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.baseForm.id !== undefined) {
            this.subscribeToSaveResponse(this.baseFormService.update(this.baseForm));
        } else {
            this.subscribeToSaveResponse(this.baseFormService.create(this.baseForm));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBaseForm>>) {
        result.subscribe((res: HttpResponse<IBaseForm>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get baseForm() {
        return this._baseForm;
    }

    set baseForm(baseForm: IBaseForm) {
        this._baseForm = baseForm;
    }
}
