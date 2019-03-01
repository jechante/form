import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IWxUser } from 'app/shared/model/wx-user.model';

type EntityResponseType = HttpResponse<IWxUser>;
type EntityArrayResponseType = HttpResponse<IWxUser[]>;

@Injectable({ providedIn: 'root' })
export class WxUserService {
    private resourceUrl = SERVER_API_URL + 'api/wx-users';

    constructor(private http: HttpClient) {}

    create(wxUser: IWxUser): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(wxUser);
        return this.http
            .post<IWxUser>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(wxUser: IWxUser): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(wxUser);
        return this.http
            .put<IWxUser>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IWxUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IWxUser[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(wxUser: IWxUser): IWxUser {
        const copy: IWxUser = Object.assign({}, wxUser, {
            registerDateTime: wxUser.registerDateTime != null && wxUser.registerDateTime.isValid() ? wxUser.registerDateTime.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.registerDateTime = res.body.registerDateTime != null ? moment(res.body.registerDateTime) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((wxUser: IWxUser) => {
            wxUser.registerDateTime = wxUser.registerDateTime != null ? moment(wxUser.registerDateTime) : null;
        });
        return res;
    }

    sync(): Observable<HttpResponse<any>> {
        return this.http
            .get<any>(`${this.resourceUrl}/sync`, { observe: 'response' });
    }


}
