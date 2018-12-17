import { IWxUser } from 'app/shared/model//wx-user.model';
import { IBaseProperty } from 'app/shared/model//base-property.model';

export interface IUserDemand {
    id?: number;
    propertyValue?: string;
    remark?: string;
    wxUser?: IWxUser;
    base?: IBaseProperty;
}

export class UserDemand implements IUserDemand {
    constructor(
        public id?: number,
        public propertyValue?: string,
        public remark?: string,
        public wxUser?: IWxUser,
        public base?: IBaseProperty
    ) {}
}
