import { Component, OnDestroy, OnInit } from '@angular/core';
import { IWxUser } from 'app/shared/model/wx-user.model';
import { Subscription } from 'rxjs';
import { WxUserService } from 'app/entities/wx-user';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Principal } from 'app/core';
import { ITEMS_PER_PAGE } from 'app/shared';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { userPropertyRoute, UserPropertyService } from 'app/entities/user-property';
import { IUserProperty } from 'app/shared/model/user-property.model';

@Component({
    selector: 'jhi-user-picture',
    templateUrl: './user-picture.component.html',
    styles: []
})
export class UserPictureComponent implements OnInit, OnDestroy {
    pictures: any[];
    pictureGrid: any[][];
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    queryCount: any;
    reverse: any;
    totalItems: number;
    colNum: number; // 每行图片数

    constructor(
        private userPropertyService: UserPropertyService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private principal: Principal
    ) {
        this.pictures = [];
        // this.pictureGrid = [];
        this.colNum = 5;
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
    }

    loadAll() {
        this.userPropertyService
            .userPictures({
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IUserProperty[]>) => this.paginatePictures(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    reset() {
        this.page = 0;
        this.pictures = [];
        // this.pictureGrid = [];
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

    trackId(index: number, item: any) {
        return item.openId;
    }

    registerChangeInWxUsers() {
        this.eventSubscriber = this.eventManager.subscribe('pictureListModification', response => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginatePictures(data: IUserProperty[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            // 使用的是无限滚轮
            const userProperty = data[i];
            const picture = {};
            const urls = JSON.parse(userProperty.propertyValue);
            for (let url of urls) {
                picture['openId'] = userProperty.wxUser.id;
                picture['url'] = url;
                // picture['url'] = url;
                picture['nickname'] = userProperty.wxUser.wxNickName;
                this.pictures.push(picture);
            }
        }
        // 重建pictureGrid
        this.pictureGrid = [];
        const rowNum = Math.ceil(this.pictures.length / this.colNum);
        for (let i = 0; i < rowNum; i++) {
            const row = [];
            for (let j = 0; j < this.colNum; j++) {
                const index = j + i * this.colNum;
                if (index < this.pictures.length) {
                    row.push(this.pictures[index]);
                }
            }
            this.pictureGrid.push(row);
        }

        console.log(this.pictureGrid);
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
