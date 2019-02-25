import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { BaseForm } from 'app/shared/model/base-form.model';
import { BaseFormService } from './base-form.service';
import { BaseFormComponent } from './base-form.component';
import { BaseFormDetailComponent } from './base-form-detail.component';
import { BaseFormUpdateComponent } from './base-form-update.component';
import { BaseFormDeletePopupComponent } from './base-form-delete-dialog.component';
import { IBaseForm } from 'app/shared/model/base-form.model';

@Injectable({ providedIn: 'root' })
export class BaseFormResolve implements Resolve<IBaseForm> {
    constructor(private service: BaseFormService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((baseForm: HttpResponse<BaseForm>) => baseForm.body));
        }
        return of(new BaseForm());
    }
}

export const baseFormRoute: Routes = [
    {
        path: 'base-form',
        component: BaseFormComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '表单基础信息'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'base-form/:id/view',
        component: BaseFormDetailComponent,
        resolve: {
            baseForm: BaseFormResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '表单基础信息'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'base-form/new',
        component: BaseFormUpdateComponent,
        resolve: {
            baseForm: BaseFormResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '表单基础信息'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'base-form/:id/edit',
        component: BaseFormUpdateComponent,
        resolve: {
            baseForm: BaseFormResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '表单基础信息'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const baseFormPopupRoute: Routes = [
    {
        path: 'base-form/:id/delete',
        component: BaseFormDeletePopupComponent,
        resolve: {
            baseForm: BaseFormResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '表单基础信息'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
