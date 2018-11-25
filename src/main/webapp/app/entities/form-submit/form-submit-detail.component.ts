import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormSubmit } from 'app/shared/model/form-submit.model';

@Component({
    selector: 'jhi-form-submit-detail',
    templateUrl: './form-submit-detail.component.html'
})
export class FormSubmitDetailComponent implements OnInit {
    formSubmit: IFormSubmit;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ formSubmit }) => {
            this.formSubmit = formSubmit;
        });
    }

    previousState() {
        window.history.back();
    }
}
