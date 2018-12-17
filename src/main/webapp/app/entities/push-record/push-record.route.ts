import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PushRecord } from 'app/shared/model/push-record.model';
import { PushRecordService } from './push-record.service';
import { PushRecordComponent } from './push-record.component';
import { PushRecordDetailComponent } from './push-record-detail.component';
import { PushRecordUpdateComponent } from './push-record-update.component';
import { PushRecordDeletePopupComponent } from './push-record-delete-dialog.component';
import { IPushRecord } from 'app/shared/model/push-record.model';

@Injectable({ providedIn: 'root' })
export class PushRecordResolve implements Resolve<IPushRecord> {
    constructor(private service: PushRecordService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((pushRecord: HttpResponse<PushRecord>) => pushRecord.body));
        }
        return of(new PushRecord());
    }
}

export const pushRecordRoute: Routes = [
    {
        path: 'push-record',
        component: PushRecordComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'PushRecords'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'push-record/:id/view',
        component: PushRecordDetailComponent,
        resolve: {
            pushRecord: PushRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'PushRecords'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'push-record/new',
        component: PushRecordUpdateComponent,
        resolve: {
            pushRecord: PushRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'PushRecords'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'push-record/:id/edit',
        component: PushRecordUpdateComponent,
        resolve: {
            pushRecord: PushRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'PushRecords'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pushRecordPopupRoute: Routes = [
    {
        path: 'push-record/:id/delete',
        component: PushRecordDeletePopupComponent,
        resolve: {
            pushRecord: PushRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'PushRecords'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
