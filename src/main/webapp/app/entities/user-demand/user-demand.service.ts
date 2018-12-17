import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserDemand } from 'app/shared/model/user-demand.model';

type EntityResponseType = HttpResponse<IUserDemand>;
type EntityArrayResponseType = HttpResponse<IUserDemand[]>;

@Injectable({ providedIn: 'root' })
export class UserDemandService {
    private resourceUrl = SERVER_API_URL + 'api/user-demands';

    constructor(private http: HttpClient) {}

    create(userDemand: IUserDemand): Observable<EntityResponseType> {
        return this.http.post<IUserDemand>(this.resourceUrl, userDemand, { observe: 'response' });
    }

    update(userDemand: IUserDemand): Observable<EntityResponseType> {
        return this.http.put<IUserDemand>(this.resourceUrl, userDemand, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IUserDemand>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IUserDemand[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
