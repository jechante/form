import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IFormSubmit } from 'app/shared/model/form-submit.model';
import { FormSubmitService } from './form-submit.service';

@Component({
    selector: 'jhi-form-submit-update',
    templateUrl: './form-submit-update.component.html'
})
export class FormSubmitUpdateComponent implements OnInit {
    private _formSubmit: IFormSubmit;
    isSaving: boolean;

    constructor(private formSubmitService: FormSubmitService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ formSubmit }) => {
            this.formSubmit = formSubmit;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.formSubmit.id !== undefined) {
            this.subscribeToSaveResponse(this.formSubmitService.update(this.formSubmit));
        } else {
            this.subscribeToSaveResponse(this.formSubmitService.create(this.formSubmit));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFormSubmit>>) {
        result.subscribe((res: HttpResponse<IFormSubmit>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get formSubmit() {
        return this._formSubmit;
    }

    set formSubmit(formSubmit: IFormSubmit) {
        this._formSubmit = formSubmit;
    }
}
