import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MatchRecord } from 'app/shared/model/match-record.model';
import { MatchRecordService } from './match-record.service';
import { MatchRecordComponent } from './match-record.component';
import { MatchRecordDetailComponent } from './match-record-detail.component';
import { MatchRecordUpdateComponent } from './match-record-update.component';
import { MatchRecordDeletePopupComponent } from './match-record-delete-dialog.component';
import { IMatchRecord } from 'app/shared/model/match-record.model';

@Injectable({ providedIn: 'root' })
export class MatchRecordResolve implements Resolve<IMatchRecord> {
    constructor(private service: MatchRecordService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((matchRecord: HttpResponse<MatchRecord>) => matchRecord.body));
        }
        return of(new MatchRecord());
    }
}

export const matchRecordRoute: Routes = [
    {
        path: 'match-record',
        component: MatchRecordComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'MatchRecords'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'match-record/:id/view',
        component: MatchRecordDetailComponent,
        resolve: {
            matchRecord: MatchRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MatchRecords'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'match-record/new',
        component: MatchRecordUpdateComponent,
        resolve: {
            matchRecord: MatchRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MatchRecords'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'match-record/:id/edit',
        component: MatchRecordUpdateComponent,
        resolve: {
            matchRecord: MatchRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MatchRecords'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const matchRecordPopupRoute: Routes = [
    {
        path: 'match-record/:id/delete',
        component: MatchRecordDeletePopupComponent,
        resolve: {
            matchRecord: MatchRecordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MatchRecords'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
