import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { FormRegionModule } from './region/region.module';
import { FormCountryModule } from './country/country.module';
import { FormLocationModule } from './location/location.module';
import { FormDepartmentModule } from './department/department.module';
import { FormTaskModule } from './task/task.module';
import { FormEmployeeModule } from './employee/employee.module';
import { FormJobModule } from './job/job.module';
import { FormJobHistoryModule } from './job-history/job-history.module';
import { FormFormSubmitModule } from './form-submit/form-submit.module';
import { FormWxInfoModule } from './wx-info/wx-info.module';
import { FormUserBaseModule } from './user-base/user-base.module';
import { FormUserPropertyModule } from './user-property/user-property.module';
import { FormBasePropertyModule } from './base-property/base-property.module';
import { FormAlgorithmModule } from './algorithm/algorithm.module';
import { FormMatchRecordModule } from './match-record/match-record.module';
import { FormFormToPropertyModule } from './form-to-property/form-to-property.module';
import { FormBaseFormModule } from './base-form/base-form.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        FormRegionModule,
        FormCountryModule,
        FormLocationModule,
        FormDepartmentModule,
        FormTaskModule,
        FormEmployeeModule,
        FormJobModule,
        FormJobHistoryModule,
        FormFormSubmitModule,
        FormWxInfoModule,
        FormUserBaseModule,
        FormUserPropertyModule,
        FormBasePropertyModule,
        FormAlgorithmModule,
        FormMatchRecordModule,
        FormFormToPropertyModule,
        FormBaseFormModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FormEntityModule {}
