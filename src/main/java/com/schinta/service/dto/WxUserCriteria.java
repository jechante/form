package com.schinta.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.schinta.domain.enumeration.Gender;
import com.schinta.domain.enumeration.Gender;
import com.schinta.domain.enumeration.UserStatus;
import com.schinta.domain.enumeration.RegisterChannel;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the WxUser entity. This class is used in WxUserResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /wx-users?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WxUserCriteria implements Serializable {
    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {
    }
    /**
     * Class for filtering UserStatus
     */
    public static class UserStatusFilter extends Filter<UserStatus> {
    }
    /**
     * Class for filtering RegisterChannel
     */
    public static class RegisterChannelFilter extends Filter<RegisterChannel> {
    }

    private static final long serialVersionUID = 1L;

    private StringFilter id;

    private StringFilter wxNickName;

    private GenderFilter wxGender;

    private StringFilter wxCountry;

    private StringFilter wxProvince;

    private StringFilter wxCity;

    private StringFilter wxHeadimgurl;

    private StringFilter wxUnionid;

    private StringFilter wxLanguage;

    private GenderFilter gender;

    private UserStatusFilter userStatus;

    private RegisterChannelFilter registerChannel;

    private ZonedDateTimeFilter registerDateTime;

    private IntegerFilter pushLimit;

    private LongFilter propertiesId;

    private LongFilter demandsId;

    private LongFilter submitsId;

    private LongFilter aMatchesId;

    private LongFilter bMatchesId;

    private LongFilter pushRecordsId;

    private LongFilter brokerId;

    public WxUserCriteria() {
    }

    public StringFilter getId() {
        return id;
    }

    public void setId(StringFilter id) {
        this.id = id;
    }

    public StringFilter getWxNickName() {
        return wxNickName;
    }

    public void setWxNickName(StringFilter wxNickName) {
        this.wxNickName = wxNickName;
    }

    public GenderFilter getWxGender() {
        return wxGender;
    }

    public void setWxGender(GenderFilter wxGender) {
        this.wxGender = wxGender;
    }

    public StringFilter getWxCountry() {
        return wxCountry;
    }

    public void setWxCountry(StringFilter wxCountry) {
        this.wxCountry = wxCountry;
    }

    public StringFilter getWxProvince() {
        return wxProvince;
    }

    public void setWxProvince(StringFilter wxProvince) {
        this.wxProvince = wxProvince;
    }

    public StringFilter getWxCity() {
        return wxCity;
    }

    public void setWxCity(StringFilter wxCity) {
        this.wxCity = wxCity;
    }

    public StringFilter getWxHeadimgurl() {
        return wxHeadimgurl;
    }

    public void setWxHeadimgurl(StringFilter wxHeadimgurl) {
        this.wxHeadimgurl = wxHeadimgurl;
    }

    public StringFilter getWxUnionid() {
        return wxUnionid;
    }

    public void setWxUnionid(StringFilter wxUnionid) {
        this.wxUnionid = wxUnionid;
    }

    public StringFilter getWxLanguage() {
        return wxLanguage;
    }

    public void setWxLanguage(StringFilter wxLanguage) {
        this.wxLanguage = wxLanguage;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public UserStatusFilter getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatusFilter userStatus) {
        this.userStatus = userStatus;
    }

    public RegisterChannelFilter getRegisterChannel() {
        return registerChannel;
    }

    public void setRegisterChannel(RegisterChannelFilter registerChannel) {
        this.registerChannel = registerChannel;
    }

    public ZonedDateTimeFilter getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(ZonedDateTimeFilter registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    public IntegerFilter getPushLimit() {
        return pushLimit;
    }

    public void setPushLimit(IntegerFilter pushLimit) {
        this.pushLimit = pushLimit;
    }

    public LongFilter getPropertiesId() {
        return propertiesId;
    }

    public void setPropertiesId(LongFilter propertiesId) {
        this.propertiesId = propertiesId;
    }

    public LongFilter getDemandsId() {
        return demandsId;
    }

    public void setDemandsId(LongFilter demandsId) {
        this.demandsId = demandsId;
    }

    public LongFilter getSubmitsId() {
        return submitsId;
    }

    public void setSubmitsId(LongFilter submitsId) {
        this.submitsId = submitsId;
    }

    public LongFilter getAMatchesId() {
        return aMatchesId;
    }

    public void setAMatchesId(LongFilter aMatchesId) {
        this.aMatchesId = aMatchesId;
    }

    public LongFilter getBMatchesId() {
        return bMatchesId;
    }

    public void setBMatchesId(LongFilter bMatchesId) {
        this.bMatchesId = bMatchesId;
    }

    public LongFilter getPushRecordsId() {
        return pushRecordsId;
    }

    public void setPushRecordsId(LongFilter pushRecordsId) {
        this.pushRecordsId = pushRecordsId;
    }

    public LongFilter getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(LongFilter brokerId) {
        this.brokerId = brokerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WxUserCriteria that = (WxUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(wxNickName, that.wxNickName) &&
            Objects.equals(wxGender, that.wxGender) &&
            Objects.equals(wxCountry, that.wxCountry) &&
            Objects.equals(wxProvince, that.wxProvince) &&
            Objects.equals(wxCity, that.wxCity) &&
            Objects.equals(wxHeadimgurl, that.wxHeadimgurl) &&
            Objects.equals(wxUnionid, that.wxUnionid) &&
            Objects.equals(wxLanguage, that.wxLanguage) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(userStatus, that.userStatus) &&
            Objects.equals(registerChannel, that.registerChannel) &&
            Objects.equals(registerDateTime, that.registerDateTime) &&
            Objects.equals(pushLimit, that.pushLimit) &&
            Objects.equals(propertiesId, that.propertiesId) &&
            Objects.equals(demandsId, that.demandsId) &&
            Objects.equals(submitsId, that.submitsId) &&
            Objects.equals(aMatchesId, that.aMatchesId) &&
            Objects.equals(bMatchesId, that.bMatchesId) &&
            Objects.equals(pushRecordsId, that.pushRecordsId) &&
            Objects.equals(brokerId, that.brokerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        wxNickName,
        wxGender,
        wxCountry,
        wxProvince,
        wxCity,
        wxHeadimgurl,
        wxUnionid,
        wxLanguage,
        gender,
        userStatus,
        registerChannel,
        registerDateTime,
        pushLimit,
        propertiesId,
        demandsId,
        submitsId,
        aMatchesId,
        bMatchesId,
        pushRecordsId,
        brokerId
        );
    }

    @Override
    public String toString() {
        return "WxUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (wxNickName != null ? "wxNickName=" + wxNickName + ", " : "") +
                (wxGender != null ? "wxGender=" + wxGender + ", " : "") +
                (wxCountry != null ? "wxCountry=" + wxCountry + ", " : "") +
                (wxProvince != null ? "wxProvince=" + wxProvince + ", " : "") +
                (wxCity != null ? "wxCity=" + wxCity + ", " : "") +
                (wxHeadimgurl != null ? "wxHeadimgurl=" + wxHeadimgurl + ", " : "") +
                (wxUnionid != null ? "wxUnionid=" + wxUnionid + ", " : "") +
                (wxLanguage != null ? "wxLanguage=" + wxLanguage + ", " : "") +
                (gender != null ? "gender=" + gender + ", " : "") +
                (userStatus != null ? "userStatus=" + userStatus + ", " : "") +
                (registerChannel != null ? "registerChannel=" + registerChannel + ", " : "") +
                (registerDateTime != null ? "registerDateTime=" + registerDateTime + ", " : "") +
                (pushLimit != null ? "pushLimit=" + pushLimit + ", " : "") +
                (propertiesId != null ? "propertiesId=" + propertiesId + ", " : "") +
                (demandsId != null ? "demandsId=" + demandsId + ", " : "") +
                (submitsId != null ? "submitsId=" + submitsId + ", " : "") +
                (aMatchesId != null ? "aMatchesId=" + aMatchesId + ", " : "") +
                (bMatchesId != null ? "bMatchesId=" + bMatchesId + ", " : "") +
                (pushRecordsId != null ? "pushRecordsId=" + pushRecordsId + ", " : "") +
                (brokerId != null ? "brokerId=" + brokerId + ", " : "") +
            "}";
    }

}
