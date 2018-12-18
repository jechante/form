import { IPushRecord } from 'app/shared/model//push-record.model';
import { IAlgorithm } from 'app/shared/model//algorithm.model';
import { IWxUser } from 'app/shared/model//wx-user.model';

export const enum MatchType {
    ATOB = 'ATOB',
    BTOA = 'BTOA',
    BIDIRECTIONAL = 'BIDIRECTIONAL'
}

export const enum PushStatus {
    NEITHER = 'NEITHER',
    EITHER = 'EITHER',
    BOTH = 'BOTH'
}

export interface IUserMatch {
    id?: number;
    scoreAtoB?: number;
    scoreBtoA?: number;
    scoreTotal?: number;
    ratio?: number;
    rankA?: number;
    rankB?: number;
    matchType?: MatchType;
    pushStatus?: PushStatus;
    pushRecords?: IPushRecord[];
    algorithm?: IAlgorithm;
    userA?: IWxUser;
    userB?: IWxUser;
}

export class UserMatch implements IUserMatch {
    constructor(
        public id?: number,
        public scoreAtoB?: number,
        public scoreBtoA?: number,
        public scoreTotal?: number,
        public ratio?: number,
        public rankA?: number,
        public rankB?: number,
        public matchType?: MatchType,
        public pushStatus?: PushStatus,
        public pushRecords?: IPushRecord[],
        public algorithm?: IAlgorithm,
        public userA?: IWxUser,
        public userB?: IWxUser
    ) {}
}
