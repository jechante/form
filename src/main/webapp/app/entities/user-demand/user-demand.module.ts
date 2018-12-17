import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FormSharedModule } from 'app/shared';
import {
    UserDemandComponent,
    UserDemandDetailComponent,
    UserDemandUpdateComponent,
    UserDemandDeletePopupComponent,
    UserDemandDeleteDialogComponent,
    userDemandRoute,
    userDemandPopupRoute
} from './';

const ENTITY_STATES = [...userDemandRoute, ...userDemandPopupRoute];

@NgModule({
    imports: [FormSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        UserDemandComponent,
        UserDemandDetailComponent,
        UserDemandUpdateComponent,
        UserDemandDeleteDialogComponent,
        UserDemandDeletePopupComponent
    ],
    entryComponents: [UserDemandComponent, UserDemandUpdateComponent, UserDemandDeleteDialogComponent, UserDemandDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormUserDemandModule {}
