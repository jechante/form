import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormToProperty } from 'app/shared/model/form-to-property.model';

@Component({
    selector: 'jhi-form-to-property-detail',
    templateUrl: './form-to-property-detail.component.html'
})
export class FormToPropertyDetailComponent implements OnInit {
    formToProperty: IFormToProperty;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ formToProperty }) => {
            this.formToProperty = formToProperty;
        });
    }

    previousState() {
        window.history.back();
    }
}
