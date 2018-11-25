export interface IUserProperty {
    id?: number;
    userID?: string;
    propertyID?: string;
    propertyValue?: string;
    remark?: string;
}

export class UserProperty implements IUserProperty {
    constructor(
        public id?: number,
        public userID?: string,
        public propertyID?: string,
        public propertyValue?: string,
        public remark?: string
    ) {}
}
