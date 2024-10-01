package com.example.daobe.objet.domain.repository;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.repository.dto.ObjetListCondition;
import org.springframework.data.domain.Slice;

public interface ObjetListRepository {

    Slice<Objet> getObjetListOfSharerByCondition(ObjetListCondition condition);

    Slice<Objet> getObjetListByCondition(ObjetListCondition condition);
}
