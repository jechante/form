import { Moment } from 'moment';
import { IWxUser } from 'app/shared/model//wx-user.model';
import { IUserMatch } from 'app/shared/model//user-match.model';

export const enum PushType {
    ASK = 'ASK',
    Regular = 'Regular'
}

export interface IPushRecord {
    id?: number;
    pushType?: PushType;
    pushDateTime?: Moment;
    success?: boolean;
    resultUrl?: string;
    user?: IWxUser;
    userMatches?: IUserMatch[];
}

export class PushRecord implements IPushRecord {
    constructor(
        public id?: number,
        public pushType?: PushType,
        public pushDateTime?: Moment,
        public success?: boolean,
        public resultUrl?: string,
        public user?: IWxUser,
        public userMatches?: IUserMatch[]
    ) {
        this.success = this.success || false;
    }
}
