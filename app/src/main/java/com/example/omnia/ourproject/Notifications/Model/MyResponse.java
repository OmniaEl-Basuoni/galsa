package com.example.omnia.ourproject.Notifications.Model;

import java.util.List;

/**
 * Created by zt on 15/03/18.
 */

public class MyResponse {

    public long multicast_id ;
    public int success ;

    public long getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(int canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public int failure ;
    public int canonical_ids ;
    public List<Result> results ;

    public MyResponse() {
    }

    public MyResponse(long multicast_id, int success, int failure, int canonical_ids, List<Result> results) {
        this.multicast_id = multicast_id;
        this.success = success;
        this.failure = failure;
        this.canonical_ids = canonical_ids;
        this.results = results;
    }
}
