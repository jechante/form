import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FormSharedModule } from 'app/shared';
import {
    WxUserComponent,
    WxUserDetailComponent,
    WxUserUpdateComponent,
    WxUserDeletePopupComponent,
    WxUserDeleteDialogComponent,
    wxUserRoute,
    wxUserPopupRoute
} from './';
import { UserPropertyDemandComponent } from './user-property-demand/user-property-demand.component';
import { UserPictureComponent } from './user-picture/user-picture.component';

const ENTITY_STATES = [...wxUserRoute, ...wxUserPopupRoute];

@NgModule({
    imports: [FormSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [WxUserComponent, WxUserDetailComponent, WxUserUpdateComponent, WxUserDeleteDialogComponent, WxUserDeletePopupComponent, UserPropertyDemandComponent, UserPictureComponent],
    entryComponents: [WxUserComponent, WxUserUpdateComponent, WxUserDeleteDialogComponent, WxUserDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormWxUserModule {}
