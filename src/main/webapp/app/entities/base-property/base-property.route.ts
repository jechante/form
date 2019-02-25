import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { BaseProperty } from 'app/shared/model/base-property.model';
import { BasePropertyService } from './base-property.service';
import { BasePropertyComponent } from './base-property.component';
import { BasePropertyDetailComponent } from './base-property-detail.component';
import { BasePropertyUpdateComponent } from './base-property-update.component';
import { BasePropertyDeletePopupComponent } from './base-property-delete-dialog.component';
import { IBaseProperty } from 'app/shared/model/base-property.model';

@Injectable({ providedIn: 'root' })
export class BasePropertyResolve implements Resolve<IBaseProperty> {
    constructor(private service: BasePropertyService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((baseProperty: HttpResponse<BaseProperty>) => baseProperty.body));
        }
        return of(new BaseProperty());
    }
}

export const basePropertyRoute: Routes = [
    {
        path: 'base-property',
        component: BasePropertyComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '基础属性'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'base-property/:id/view',
        component: BasePropertyDetailComponent,
        resolve: {
            baseProperty: BasePropertyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '基础属性'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'base-property/new',
        component: BasePropertyUpdateComponent,
        resolve: {
            baseProperty: BasePropertyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '基础属性'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'base-property/:id/edit',
        component: BasePropertyUpdateComponent,
        resolve: {
            baseProperty: BasePropertyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '基础属性'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const basePropertyPopupRoute: Routes = [
    {
        path: 'base-property/:id/delete',
        component: BasePropertyDeletePopupComponent,
        resolve: {
            baseProperty: BasePropertyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: '基础属性'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
