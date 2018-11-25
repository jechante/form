import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMatchRecord } from 'app/shared/model/match-record.model';

type EntityResponseType = HttpResponse<IMatchRecord>;
type EntityArrayResponseType = HttpResponse<IMatchRecord[]>;

@Injectable({ providedIn: 'root' })
export class MatchRecordService {
    private resourceUrl = SERVER_API_URL + 'api/match-records';

    constructor(private http: HttpClient) {}

    create(matchRecord: IMatchRecord): Observable<EntityResponseType> {
        return this.http.post<IMatchRecord>(this.resourceUrl, matchRecord, { observe: 'response' });
    }

    update(matchRecord: IMatchRecord): Observable<EntityResponseType> {
        return this.http.put<IMatchRecord>(this.resourceUrl, matchRecord, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IMatchRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IMatchRecord[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
