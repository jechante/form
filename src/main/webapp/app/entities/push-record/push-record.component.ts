import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IPushRecord } from 'app/shared/model/push-record.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { PushRecordService } from './push-record.service';
import { SERVER_API_URL } from 'app/app.constants';
import {Moment} from "moment";
@Component({
    selector: 'jhi-push-record',
    templateUrl: './push-record.component.html'
})
export class PushRecordComponent implements OnInit, OnDestroy {
    currentAccount: any;
    pushRecords: IPushRecord[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    // 全部时间戳
    timestamps: Moment[];
    // 选择时间戳
    choosenTime: Moment;
    // 预览用户openid
    wxUserPreview: string;

    constructor(
        private pushRecordService: PushRecordService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    }

    loadAll(/*time?*/) {
        let time = this.getLocalDateTimeJsonFromMoment(this.choosenTime);
        let option = {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
        };
        if (time) option['time'] = time;
        this.pushRecordService
            .query(option)
            .subscribe(
                (res: HttpResponse<IPushRecord[]>) => this.paginatePushRecords(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    getLocalDateTimeJsonFromMoment(moment: Moment): string {
        let time = moment == null ? null : (<any>moment)._i; // todo 需要找到时差转换的方法 toJson\toISOString均得到的是带8小时时差的时间
        return time;
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/push-record'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/push-record',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.pushRecordService.findTimeOption().subscribe(res => this.timestamps = res, (res: HttpErrorResponse) => this.onError(res.message))
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInPushRecords();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IPushRecord) {
        return item.id;
    }

    registerChangeInPushRecords() {
        this.eventSubscriber = this.eventManager.subscribe('pushRecordListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginatePushRecords(data: IPushRecord[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.pushRecords = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    preview(id: number) {
        window.open(`${SERVER_API_URL}wx/redirect/111/match-result-preview?id=${id}`, '_blank');
    }

    // findByTime() {
    //     this.loadAll();
    // }

    generateNewRecords() {
        this.pushRecordService.generateNewRecords().subscribe(
            res => {
                this.choosenTime = res;
                this.timestamps.push(res);
                this.loadAll();
            },
            (res: HttpErrorResponse) => this.onError(res.message));
    }

    massPreview() {
        if (!this.choosenTime) this.jhiAlertService.error('必须选择一个时间批次', null, null);
        if (!this.wxUserPreview) this.jhiAlertService.error('必须填写预览人的openId', null, null);
        this.pushRecordService.massPreview({
            timestamp: this.getLocalDateTimeJsonFromMoment(this.choosenTime),
            openid: this.wxUserPreview
        }).subscribe(null, (res: HttpErrorResponse) => this.onError(res.message));
    }

    broadcast() {
        if (!this.choosenTime) this.jhiAlertService.error('必须选择一个时间批次', null, null);
        this.pushRecordService.broadcast({
            timestamp: this.getLocalDateTimeJsonFromMoment(this.choosenTime)
        }).subscribe(null, (res: HttpErrorResponse) => this.onError(res.message));
    }
}
