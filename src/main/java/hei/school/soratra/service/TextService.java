package hei.school.soratra.service;

import hei.school.soratra.repository.TextRepository;
import hei.school.soratra.repository.model.Text;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TextService {
  private final TextRepository repository;

  @Transactional
  public Text save(Text text) {
    return repository.save(text);
  }

  public Text getTextById(String id) {
    return repository.findById(id).orElse(null);
  }
}
