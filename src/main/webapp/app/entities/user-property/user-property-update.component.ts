import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IUserProperty } from 'app/shared/model/user-property.model';
import { UserPropertyService } from './user-property.service';
import { IWxUser } from 'app/shared/model/wx-user.model';
import { WxUserService } from 'app/entities/wx-user';
import { IBaseProperty } from 'app/shared/model/base-property.model';
import { BasePropertyService } from 'app/entities/base-property';

@Component({
    selector: 'jhi-user-property-update',
    templateUrl: './user-property-update.component.html'
})
export class UserPropertyUpdateComponent implements OnInit {
    private _userProperty: IUserProperty;
    isSaving: boolean;

    wxusers: IWxUser[];

    baseproperties: IBaseProperty[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private userPropertyService: UserPropertyService,
        private wxUserService: WxUserService,
        private basePropertyService: BasePropertyService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ userProperty }) => {
            this.userProperty = userProperty;
        });
        this.wxUserService.query().subscribe(
            (res: HttpResponse<IWxUser[]>) => {
                this.wxusers = res.body;
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
        if (this.userProperty.id !== undefined) {
            this.subscribeToSaveResponse(this.userPropertyService.update(this.userProperty));
        } else {
            this.subscribeToSaveResponse(this.userPropertyService.create(this.userProperty));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IUserProperty>>) {
        result.subscribe((res: HttpResponse<IUserProperty>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackBasePropertyById(index: number, item: IBaseProperty) {
        return item.id;
    }
    get userProperty() {
        return this._userProperty;
    }

    set userProperty(userProperty: IUserProperty) {
        this._userProperty = userProperty;
    }
}
