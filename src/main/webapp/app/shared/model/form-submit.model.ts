import { Moment } from 'moment';
import { IWxUser } from 'app/shared/model//wx-user.model';
import { IBaseForm } from 'app/shared/model//base-form.model';

export interface IFormSubmit {
    id?: number;
    submitJosn?: string;
    serialNumber?: number;
    creatorName?: string;
    createdDateTime?: Moment;
    updatedDateTime?: Moment;
    infoRemoteIp?: string;
    dealflag?: boolean;
    computed?: boolean;
    wxUser?: IWxUser;
    base?: IBaseForm;
}

export class FormSubmit implements IFormSubmit {
    constructor(
        public id?: number,
        public submitJosn?: string,
        public serialNumber?: number,
        public creatorName?: string,
        public createdDateTime?: Moment,
        public updatedDateTime?: Moment,
        public infoRemoteIp?: string,
        public dealflag?: boolean,
        public computed?: boolean,
        public wxUser?: IWxUser,
        public base?: IBaseForm
    ) {
        this.dealflag = this.dealflag || false;
        this.computed = this.computed || false;
    }
}
