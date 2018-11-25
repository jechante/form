import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IUserBase } from 'app/shared/model/user-base.model';
import { UserBaseService } from './user-base.service';

@Component({
    selector: 'jhi-user-base-update',
    templateUrl: './user-base-update.component.html'
})
export class UserBaseUpdateComponent implements OnInit {
    private _userBase: IUserBase;
    isSaving: boolean;

    constructor(private userBaseService: UserBaseService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ userBase }) => {
            this.userBase = userBase;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.userBase.id !== undefined) {
            this.subscribeToSaveResponse(this.userBaseService.update(this.userBase));
        } else {
            this.subscribeToSaveResponse(this.userBaseService.create(this.userBase));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IUserBase>>) {
        result.subscribe((res: HttpResponse<IUserBase>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get userBase() {
        return this._userBase;
    }

    set userBase(userBase: IUserBase) {
        this._userBase = userBase;
    }
}
