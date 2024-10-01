package com.example.daobe.objet.domain.repository;

import com.example.daobe.objet.domain.Objet;
import com.example.daobe.objet.domain.repository.dto.ObjetFindCondition;
import org.springframework.data.domain.Slice;

public interface CustomObjetRepository {

    Slice<Objet> getObjetListByCondition(ObjetFindCondition condition);
}
