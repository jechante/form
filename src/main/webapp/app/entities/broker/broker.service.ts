import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBroker } from 'app/shared/model/broker.model';

type EntityResponseType = HttpResponse<IBroker>;
type EntityArrayResponseType = HttpResponse<IBroker[]>;

@Injectable({ providedIn: 'root' })
export class BrokerService {
    private resourceUrl = SERVER_API_URL + 'api/brokers';

    constructor(private http: HttpClient) {}

    create(broker: IBroker): Observable<EntityResponseType> {
        return this.http.post<IBroker>(this.resourceUrl, broker, { observe: 'response' });
    }

    update(broker: IBroker): Observable<EntityResponseType> {
        return this.http.put<IBroker>(this.resourceUrl, broker, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBroker>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBroker[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
