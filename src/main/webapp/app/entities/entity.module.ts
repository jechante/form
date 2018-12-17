import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { FormWxUserModule } from './wx-user/wx-user.module';
import { FormBrokerModule } from './broker/broker.module';
import { FormBasePropertyModule } from './base-property/base-property.module';
import { FormUserPropertyModule } from './user-property/user-property.module';
import { FormUserDemandModule } from './user-demand/user-demand.module';
import { FormBaseFormModule } from './base-form/base-form.module';
import { FormFormSubmitModule } from './form-submit/form-submit.module';
import { FormFormFieldModule } from './form-field/form-field.module';
import { FormAlgorithmModule } from './algorithm/algorithm.module';
import { FormUserMatchModule } from './user-match/user-match.module';
import { FormPushRecordModule } from './push-record/push-record.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        FormWxUserModule,
        FormBrokerModule,
        FormBasePropertyModule,
        FormUserPropertyModule,
        FormUserDemandModule,
        FormBaseFormModule,
        FormFormSubmitModule,
        FormFormFieldModule,
        FormAlgorithmModule,
        FormUserMatchModule,
        FormPushRecordModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormEntityModule {}
