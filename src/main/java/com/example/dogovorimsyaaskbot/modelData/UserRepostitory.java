package com.example.dogovorimsyaaskbot.modelData;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface UserRepostitory extends CrudRepository<User, Long> {
}
