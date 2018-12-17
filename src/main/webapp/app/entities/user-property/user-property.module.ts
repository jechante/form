import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FormSharedModule } from 'app/shared';
import {
    UserPropertyComponent,
    UserPropertyDetailComponent,
    UserPropertyUpdateComponent,
    UserPropertyDeletePopupComponent,
    UserPropertyDeleteDialogComponent,
    userPropertyRoute,
    userPropertyPopupRoute
} from './';

const ENTITY_STATES = [...userPropertyRoute, ...userPropertyPopupRoute];

@NgModule({
    imports: [FormSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        UserPropertyComponent,
        UserPropertyDetailComponent,
        UserPropertyUpdateComponent,
        UserPropertyDeleteDialogComponent,
        UserPropertyDeletePopupComponent
    ],
    entryComponents: [
        UserPropertyComponent,
        UserPropertyUpdateComponent,
        UserPropertyDeleteDialogComponent,
        UserPropertyDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormUserPropertyModule {}
