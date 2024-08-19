package com.tripleseven.android;

public class ResultModel {


    private String _name;
    private String _result;
    private String _openTime;
    private String _closeTime;
    private String _isOpen;

    public boolean get_isOpenSort() {
        return _isOpenSort;
    }

    public void set_isOpenSort(boolean _isOpenSort) {
        this._isOpenSort = _isOpenSort;
    }

    private boolean _isOpenSort;

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_result() {
        return _result;
    }

    public void set_result(String _result) {
        this._result = _result;
    }

    public String get_openTime() {
        return _openTime;
    }

    public void set_openTime(String _openTime) {
        this._openTime = _openTime;
    }

    public String get_closeTime() {
        return _closeTime;
    }

    public void set_closeTime(String _closeTime) {
        this._closeTime = _closeTime;
    }

    public String get_isOpen() {
        return _isOpen;
    }

    public void set_isOpen(String _isOpen) {
        this._isOpen = _isOpen;
    }

    public String get_openAv() {
        return _openAv;
    }

    public void set_openAv(String _openAv) {
        this._openAv = _openAv;
    }

    public String get_marketType() {
        return _marketType;
    }

    public void set_marketType(String _marketType) {
        this._marketType = _marketType;
    }

    private String _openAv;
    private String _marketType;

}
