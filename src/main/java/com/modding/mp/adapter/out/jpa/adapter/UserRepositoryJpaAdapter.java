package com.modding.mp.adapter.out.jpa.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.modding.mp.adapter.out.jpa.entity.UserEntity;
import com.modding.mp.adapter.out.jpa.mapper.UserMapper;
import com.modding.mp.domain.model.*;
import com.modding.mp.adapter.out.jpa.repository.SpringDataUserRepository;
import com.modding.mp.domain.port.out.IUserRepository;

@Repository
public class UserRepositoryJpaAdapter implements IUserRepository {
  private final SpringDataUserRepository repo;
  public UserRepositoryJpaAdapter(SpringDataUserRepository repo) { this.repo = repo; }

  @Override public Optional<User> byEmail(Email email){
    return repo.findByEmail(email.value()).map(UserMapper::toDomain);
  }
  @Override public Optional<User> byId(UserId id){
    return repo.findById(id.value()).map(UserMapper::toDomain);
  }
  @Override public User save(User u){
    UserEntity saved = repo.save(UserMapper.toEntity(u));
    return UserMapper.toDomain(saved);
  }
  @Override public boolean existsByEmail(Email email){ return repo.existsByEmail(email.value()); }

  @Override public Optional<User> byUsername(String username) {
    return repo.findByUsername(username).map(UserMapper::toDomain);
  }
}