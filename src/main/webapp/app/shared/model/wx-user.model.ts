import { Moment } from 'moment';
import { IUserProperty } from 'app/shared/model//user-property.model';
import { IUserDemand } from 'app/shared/model//user-demand.model';
import { IFormSubmit } from 'app/shared/model//form-submit.model';
import { IUserMatch } from 'app/shared/model//user-match.model';
import { IPushRecord } from 'app/shared/model//push-record.model';
import { IBroker } from 'app/shared/model//broker.model';

export const enum Gender {
    MALE = 'MALE',
    FEMALE = 'FEMALE'
}

export const enum UserStatus {
    ACTIVE = 'ACTIVE',
    UNACTIVE = 'UNACTIVE',
    LEFT = 'LEFT',
    FORBIDDEN = 'FORBIDDEN'
}

export const enum RegisterChannel {
    POSTER = 'POSTER',
    SITE = 'SITE',
    BUSSTATION = 'BUSSTATION',
    WECHAT = 'WECHAT',
    QQ = 'QQ'
}

export interface IWxUser {
    id?: number;
    wxNickName?: string;
    wxGender?: Gender;
    wxCountry?: string;
    wxProvince?: string;
    wxCity?: string;
    wxHeadimgurl?: string;
    wxUnionid?: string;
    wxLanguage?: string;
    gender?: Gender;
    userStatus?: UserStatus;
    registerChannel?: RegisterChannel;
    registerDateTime?: Moment;
    pushLimit?: number;
    properties?: IUserProperty[];
    demands?: IUserDemand[];
    submits?: IFormSubmit[];
    aMatches?: IUserMatch[];
    bMatches?: IUserMatch[];
    pushRecords?: IPushRecord[];
    broker?: IBroker;
}

export class WxUser implements IWxUser {
    constructor(
        public id?: number,
        public wxNickName?: string,
        public wxGender?: Gender,
        public wxCountry?: string,
        public wxProvince?: string,
        public wxCity?: string,
        public wxHeadimgurl?: string,
        public wxUnionid?: string,
        public wxLanguage?: string,
        public gender?: Gender,
        public userStatus?: UserStatus,
        public registerChannel?: RegisterChannel,
        public registerDateTime?: Moment,
        public pushLimit?: number,
        public properties?: IUserProperty[],
        public demands?: IUserDemand[],
        public submits?: IFormSubmit[],
        public aMatches?: IUserMatch[],
        public bMatches?: IUserMatch[],
        public pushRecords?: IPushRecord[],
        public broker?: IBroker
    ) {}
}
