package hei.school.soratra.repository;

import hei.school.soratra.repository.model.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextRepository extends JpaRepository<Text, String> {}
