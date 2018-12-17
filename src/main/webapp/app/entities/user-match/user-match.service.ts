import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserMatch } from 'app/shared/model/user-match.model';

type EntityResponseType = HttpResponse<IUserMatch>;
type EntityArrayResponseType = HttpResponse<IUserMatch[]>;

@Injectable({ providedIn: 'root' })
export class UserMatchService {
    private resourceUrl = SERVER_API_URL + 'api/user-matches';

    constructor(private http: HttpClient) {}

    create(userMatch: IUserMatch): Observable<EntityResponseType> {
        return this.http.post<IUserMatch>(this.resourceUrl, userMatch, { observe: 'response' });
    }

    update(userMatch: IUserMatch): Observable<EntityResponseType> {
        return this.http.put<IUserMatch>(this.resourceUrl, userMatch, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IUserMatch>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IUserMatch[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
