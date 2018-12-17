import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { FormField } from 'app/shared/model/form-field.model';
import { FormFieldService } from './form-field.service';
import { FormFieldComponent } from './form-field.component';
import { FormFieldDetailComponent } from './form-field-detail.component';
import { FormFieldUpdateComponent } from './form-field-update.component';
import { FormFieldDeletePopupComponent } from './form-field-delete-dialog.component';
import { IFormField } from 'app/shared/model/form-field.model';

@Injectable({ providedIn: 'root' })
export class FormFieldResolve implements Resolve<IFormField> {
    constructor(private service: FormFieldService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((formField: HttpResponse<FormField>) => formField.body));
        }
        return of(new FormField());
    }
}

export const formFieldRoute: Routes = [
    {
        path: 'form-field',
        component: FormFieldComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'FormFields'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'form-field/:id/view',
        component: FormFieldDetailComponent,
        resolve: {
            formField: FormFieldResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FormFields'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'form-field/new',
        component: FormFieldUpdateComponent,
        resolve: {
            formField: FormFieldResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FormFields'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'form-field/:id/edit',
        component: FormFieldUpdateComponent,
        resolve: {
            formField: FormFieldResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FormFields'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const formFieldPopupRoute: Routes = [
    {
        path: 'form-field/:id/delete',
        component: FormFieldDeletePopupComponent,
        resolve: {
            formField: FormFieldResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FormFields'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
