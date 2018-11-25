import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FormSharedModule } from 'app/shared';
import {
    FormToPropertyComponent,
    FormToPropertyDetailComponent,
    FormToPropertyUpdateComponent,
    FormToPropertyDeletePopupComponent,
    FormToPropertyDeleteDialogComponent,
    formToPropertyRoute,
    formToPropertyPopupRoute
} from './';

const ENTITY_STATES = [...formToPropertyRoute, ...formToPropertyPopupRoute];

@NgModule({
    imports: [FormSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FormToPropertyComponent,
        FormToPropertyDetailComponent,
        FormToPropertyUpdateComponent,
        FormToPropertyDeleteDialogComponent,
        FormToPropertyDeletePopupComponent
    ],
    entryComponents: [
        FormToPropertyComponent,
        FormToPropertyUpdateComponent,
        FormToPropertyDeleteDialogComponent,
        FormToPropertyDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormFormToPropertyModule {}
