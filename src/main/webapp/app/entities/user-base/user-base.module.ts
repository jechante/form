import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FormSharedModule } from 'app/shared';
import {
    UserBaseComponent,
    UserBaseDetailComponent,
    UserBaseUpdateComponent,
    UserBaseDeletePopupComponent,
    UserBaseDeleteDialogComponent,
    userBaseRoute,
    userBasePopupRoute
} from './';

const ENTITY_STATES = [...userBaseRoute, ...userBasePopupRoute];

@NgModule({
    imports: [FormSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        UserBaseComponent,
        UserBaseDetailComponent,
        UserBaseUpdateComponent,
        UserBaseDeleteDialogComponent,
        UserBaseDeletePopupComponent
    ],
    entryComponents: [UserBaseComponent, UserBaseUpdateComponent, UserBaseDeleteDialogComponent, UserBaseDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormUserBaseModule {}
