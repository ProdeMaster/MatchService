package com.ProdeMaster.MatchService.application.dto.request;

import com.ProdeMaster.MatchService.domain.model.MatchStatus;

public class UpdateStatusRequest {

    private MatchStatus newStatus;

    public UpdateStatusRequest() {
    }

    public UpdateStatusRequest(MatchStatus newStatus) {
        this.newStatus = newStatus;
    }

    public MatchStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(MatchStatus newStatus) {
        this.newStatus = newStatus;
    }
}
