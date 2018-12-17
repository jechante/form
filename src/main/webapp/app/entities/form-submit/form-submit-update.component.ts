import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IFormSubmit } from 'app/shared/model/form-submit.model';
import { FormSubmitService } from './form-submit.service';
import { IWxUser } from 'app/shared/model/wx-user.model';
import { WxUserService } from 'app/entities/wx-user';
import { IBaseForm } from 'app/shared/model/base-form.model';
import { BaseFormService } from 'app/entities/base-form';

@Component({
    selector: 'jhi-form-submit-update',
    templateUrl: './form-submit-update.component.html'
})
export class FormSubmitUpdateComponent implements OnInit {
    private _formSubmit: IFormSubmit;
    isSaving: boolean;

    wxusers: IWxUser[];

    baseforms: IBaseForm[];
    createdDateTime: string;
    updatedDateTime: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private formSubmitService: FormSubmitService,
        private wxUserService: WxUserService,
        private baseFormService: BaseFormService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ formSubmit }) => {
            this.formSubmit = formSubmit;
        });
        this.wxUserService.query().subscribe(
            (res: HttpResponse<IWxUser[]>) => {
                this.wxusers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.baseFormService.query().subscribe(
            (res: HttpResponse<IBaseForm[]>) => {
                this.baseforms = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.formSubmit.createdDateTime = moment(this.createdDateTime, DATE_TIME_FORMAT);
        this.formSubmit.updatedDateTime = moment(this.updatedDateTime, DATE_TIME_FORMAT);
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

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackWxUserById(index: number, item: IWxUser) {
        return item.id;
    }

    trackBaseFormById(index: number, item: IBaseForm) {
        return item.id;
    }
    get formSubmit() {
        return this._formSubmit;
    }

    set formSubmit(formSubmit: IFormSubmit) {
        this._formSubmit = formSubmit;
        this.createdDateTime = moment(formSubmit.createdDateTime).format(DATE_TIME_FORMAT);
        this.updatedDateTime = moment(formSubmit.updatedDateTime).format(DATE_TIME_FORMAT);
    }
}
