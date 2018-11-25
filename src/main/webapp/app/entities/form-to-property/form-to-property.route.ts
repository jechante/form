import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { FormToProperty } from 'app/shared/model/form-to-property.model';
import { FormToPropertyService } from './form-to-property.service';
import { FormToPropertyComponent } from './form-to-property.component';
import { FormToPropertyDetailComponent } from './form-to-property-detail.component';
import { FormToPropertyUpdateComponent } from './form-to-property-update.component';
import { FormToPropertyDeletePopupComponent } from './form-to-property-delete-dialog.component';
import { IFormToProperty } from 'app/shared/model/form-to-property.model';

@Injectable({ providedIn: 'root' })
export class FormToPropertyResolve implements Resolve<IFormToProperty> {
    constructor(private service: FormToPropertyService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((formToProperty: HttpResponse<FormToProperty>) => formToProperty.body));
        }
        return of(new FormToProperty());
    }
}

export const formToPropertyRoute: Routes = [
    {
        path: 'form-to-property',
        component: FormToPropertyComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'FormToProperties'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'form-to-property/:id/view',
        component: FormToPropertyDetailComponent,
        resolve: {
            formToProperty: FormToPropertyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FormToProperties'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'form-to-property/new',
        component: FormToPropertyUpdateComponent,
        resolve: {
            formToProperty: FormToPropertyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FormToProperties'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'form-to-property/:id/edit',
        component: FormToPropertyUpdateComponent,
        resolve: {
            formToProperty: FormToPropertyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FormToProperties'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const formToPropertyPopupRoute: Routes = [
    {
        path: 'form-to-property/:id/delete',
        component: FormToPropertyDeletePopupComponent,
        resolve: {
            formToProperty: FormToPropertyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FormToProperties'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
