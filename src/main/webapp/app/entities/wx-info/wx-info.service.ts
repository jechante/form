import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IWxInfo } from 'app/shared/model/wx-info.model';

type EntityResponseType = HttpResponse<IWxInfo>;
type EntityArrayResponseType = HttpResponse<IWxInfo[]>;

@Injectable({ providedIn: 'root' })
export class WxInfoService {
    private resourceUrl = SERVER_API_URL + 'api/wx-infos';

    constructor(private http: HttpClient) {}

    create(wxInfo: IWxInfo): Observable<EntityResponseType> {
        return this.http.post<IWxInfo>(this.resourceUrl, wxInfo, { observe: 'response' });
    }

    update(wxInfo: IWxInfo): Observable<EntityResponseType> {
        return this.http.put<IWxInfo>(this.resourceUrl, wxInfo, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IWxInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IWxInfo[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
