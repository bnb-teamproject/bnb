/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team.bnb.repositories;

import com.team.bnb.model.Cities;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Kosta
 */
@Repository
public interface CitiesRepository extends JpaRepository<Cities,Integer>{
    
}
