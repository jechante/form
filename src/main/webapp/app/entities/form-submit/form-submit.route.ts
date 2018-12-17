import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { FormSubmit } from 'app/shared/model/form-submit.model';
import { FormSubmitService } from './form-submit.service';
import { FormSubmitComponent } from './form-submit.component';
import { FormSubmitDetailComponent } from './form-submit-detail.component';
import { FormSubmitUpdateComponent } from './form-submit-update.component';
import { FormSubmitDeletePopupComponent } from './form-submit-delete-dialog.component';
import { IFormSubmit } from 'app/shared/model/form-submit.model';

@Injectable({ providedIn: 'root' })
export class FormSubmitResolve implements Resolve<IFormSubmit> {
    constructor(private service: FormSubmitService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((formSubmit: HttpResponse<FormSubmit>) => formSubmit.body));
        }
        return of(new FormSubmit());
    }
}

export const formSubmitRoute: Routes = [
    {
        path: 'form-submit',
        component: FormSubmitComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'FormSubmits'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'form-submit/:id/view',
        component: FormSubmitDetailComponent,
        resolve: {
            formSubmit: FormSubmitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FormSubmits'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'form-submit/new',
        component: FormSubmitUpdateComponent,
        resolve: {
            formSubmit: FormSubmitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FormSubmits'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'form-submit/:id/edit',
        component: FormSubmitUpdateComponent,
        resolve: {
            formSubmit: FormSubmitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FormSubmits'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const formSubmitPopupRoute: Routes = [
    {
        path: 'form-submit/:id/delete',
        component: FormSubmitDeletePopupComponent,
        resolve: {
            formSubmit: FormSubmitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FormSubmits'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
