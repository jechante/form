import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPushRecord } from 'app/shared/model/push-record.model';

type EntityResponseType = HttpResponse<IPushRecord>;
type EntityArrayResponseType = HttpResponse<IPushRecord[]>;

@Injectable({ providedIn: 'root' })
export class PushRecordService {
    private resourceUrl = SERVER_API_URL + 'api/push-records';

    constructor(private http: HttpClient) {}

    create(pushRecord: IPushRecord): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pushRecord);
        return this.http
            .post<IPushRecord>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(pushRecord: IPushRecord): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pushRecord);
        return this.http
            .put<IPushRecord>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPushRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPushRecord[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(pushRecord: IPushRecord): IPushRecord {
        const copy: IPushRecord = Object.assign({}, pushRecord, {
            pushDateTime: pushRecord.pushDateTime != null && pushRecord.pushDateTime.isValid() ? pushRecord.pushDateTime.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.pushDateTime = res.body.pushDateTime != null ? moment(res.body.pushDateTime) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((pushRecord: IPushRecord) => {
            pushRecord.pushDateTime = pushRecord.pushDateTime != null ? moment(pushRecord.pushDateTime) : null;
        });
        return res;
    }
}
