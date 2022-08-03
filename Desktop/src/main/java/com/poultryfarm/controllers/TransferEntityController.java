package com.poultryfarm.controllers;

import com.transfer.domain.TransferEntity;

import java.util.Collection;
import java.util.List;

public interface TransferEntityController {
    void addTransferEntities(Collection<TransferEntity> transferEntities);

    List<TransferEntity> getTransferEntities();
}
