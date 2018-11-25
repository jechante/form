import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IUserProperty } from 'app/shared/model/user-property.model';
import { UserPropertyService } from './user-property.service';

@Component({
    selector: 'jhi-user-property-update',
    templateUrl: './user-property-update.component.html'
})
export class UserPropertyUpdateComponent implements OnInit {
    private _userProperty: IUserProperty;
    isSaving: boolean;

    constructor(private userPropertyService: UserPropertyService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ userProperty }) => {
            this.userProperty = userProperty;
        });
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
    get userProperty() {
        return this._userProperty;
    }

    set userProperty(userProperty: IUserProperty) {
        this._userProperty = userProperty;
    }
}
