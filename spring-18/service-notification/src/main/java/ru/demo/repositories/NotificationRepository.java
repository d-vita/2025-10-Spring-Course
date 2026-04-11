package ru.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.demo.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
