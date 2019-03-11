import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IWxUser } from 'app/shared/model/wx-user.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { WxUserService } from './wx-user.service';

@Component({
    selector: 'jhi-wx-user',
    templateUrl: './wx-user.component.html'
})
export class WxUserComponent implements OnInit, OnDestroy {
    wxUsers: IWxUser[];
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    queryCount: any;
    reverse: any;
    totalItems: number;
    nickname: string;
    openId: string;

    constructor(
        private wxUserService: WxUserService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private principal: Principal
    ) {
        this.wxUsers = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
    }

    loadAll() {
        const param = {
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort()
        };
        if (this.nickname) {
            param['wxNickName.contains'] = this.nickname;
        }
        if (this.openId) {
            param['id.contains'] = this.openId;
        }
        this.wxUserService
            .query(param)
            .subscribe(
                (res: HttpResponse<IWxUser[]>) => this.paginateWxUsers(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    reset() {
        this.page = 0;
        this.wxUsers = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInWxUsers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IWxUser) {
        return item.id;
    }

    registerChangeInWxUsers() {
        this.eventSubscriber = this.eventManager.subscribe('wxUserListModification', response => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateWxUsers(data: IWxUser[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            this.wxUsers.push(data[i]);
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    // 用户同步
    sync() {
        this.wxUserService.sync().subscribe(() => this.reset(),
            (res: HttpErrorResponse) => this.onError(res.message));
    }
}
