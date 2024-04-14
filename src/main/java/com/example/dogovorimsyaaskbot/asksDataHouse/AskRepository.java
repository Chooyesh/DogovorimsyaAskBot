package com.example.dogovorimsyaaskbot.asksDataHouse;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



public interface AskRepository extends CrudRepository<Ask, Long> {
}
