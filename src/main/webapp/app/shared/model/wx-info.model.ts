export interface IWxInfo {
    id?: number;
    userID?: string;
    wxOpenID?: string;
}

export class WxInfo implements IWxInfo {
    constructor(public id?: number, public userID?: string, public wxOpenID?: string) {}
}
