package com.atnb.africathenextbrain.entities;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "C1",types = Contribution.class)
public interface ContributionProjection {
    public String getMessage();
    public String getSignature();
}
