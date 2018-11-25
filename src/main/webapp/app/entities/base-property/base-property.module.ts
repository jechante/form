import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FormSharedModule } from 'app/shared';
import {
    BasePropertyComponent,
    BasePropertyDetailComponent,
    BasePropertyUpdateComponent,
    BasePropertyDeletePopupComponent,
    BasePropertyDeleteDialogComponent,
    basePropertyRoute,
    basePropertyPopupRoute
} from './';

const ENTITY_STATES = [...basePropertyRoute, ...basePropertyPopupRoute];

@NgModule({
    imports: [FormSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BasePropertyComponent,
        BasePropertyDetailComponent,
        BasePropertyUpdateComponent,
        BasePropertyDeleteDialogComponent,
        BasePropertyDeletePopupComponent
    ],
    entryComponents: [
        BasePropertyComponent,
        BasePropertyUpdateComponent,
        BasePropertyDeleteDialogComponent,
        BasePropertyDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormBasePropertyModule {}
