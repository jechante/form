import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBaseProperty } from 'app/shared/model/base-property.model';

@Component({
    selector: 'jhi-base-property-detail',
    templateUrl: './base-property-detail.component.html'
})
export class BasePropertyDetailComponent implements OnInit {
    baseProperty: IBaseProperty;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ baseProperty }) => {
            this.baseProperty = baseProperty;
        });
    }

    previousState() {
        window.history.back();
    }
}
