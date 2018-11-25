import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FormSharedModule } from 'app/shared';
import {
    FormSubmitComponent,
    FormSubmitDetailComponent,
    FormSubmitUpdateComponent,
    FormSubmitDeletePopupComponent,
    FormSubmitDeleteDialogComponent,
    formSubmitRoute,
    formSubmitPopupRoute
} from './';

const ENTITY_STATES = [...formSubmitRoute, ...formSubmitPopupRoute];

@NgModule({
    imports: [FormSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FormSubmitComponent,
        FormSubmitDetailComponent,
        FormSubmitUpdateComponent,
        FormSubmitDeleteDialogComponent,
        FormSubmitDeletePopupComponent
    ],
    entryComponents: [FormSubmitComponent, FormSubmitUpdateComponent, FormSubmitDeleteDialogComponent, FormSubmitDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormFormSubmitModule {}
