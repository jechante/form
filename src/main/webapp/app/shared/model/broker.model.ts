import { IWxUser } from 'app/shared/model//wx-user.model';

export interface IBroker {
    id?: number;
    name?: string;
    wxUsers?: IWxUser[];
}

export class Broker implements IBroker {
    constructor(public id?: number, public name?: string, public wxUsers?: IWxUser[]) {}
}
