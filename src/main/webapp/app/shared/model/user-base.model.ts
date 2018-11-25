export interface IUserBase {
    id?: number;
    userID?: string;
    userName?: string;
    userStatus?: string;
    sex?: string;
    registerChannel?: string;
    registerBroker?: number;
    registerDate?: number;
    registerTime?: number;
}

export class UserBase implements IUserBase {
    constructor(
        public id?: number,
        public userID?: string,
        public userName?: string,
        public userStatus?: string,
        public sex?: string,
        public registerChannel?: string,
        public registerBroker?: number,
        public registerDate?: number,
        public registerTime?: number
    ) {}
}
