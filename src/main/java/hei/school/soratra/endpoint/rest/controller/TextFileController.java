package hei.school.soratra.endpoint.rest.controller;

import hei.school.soratra.model.rest.RestText;
import hei.school.soratra.repository.model.Text;
import hei.school.soratra.service.TextFileService;
import hei.school.soratra.service.TextService;
import java.io.File;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class TextFileController {
  TextFileService service;
  TextService textService;

  @PutMapping(value = "/soratra/{id}")
  public ResponseEntity<String> getUpperText(
      @PathVariable(name = "id") String id, @RequestBody(required = false) byte[] file) {
    File output = service.getUpperCaseImage(id, file);
    if (output == null) {
      return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("");
    }
    return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("OK");
  }

  @GetMapping(value = "/soratra/{id}")
  public RestText getTextById(@PathVariable(name = "id") String id) {
    Text text = textService.getTextById(id);
    RestText output = service.getRestText(text);
    if (output == null) {
      RestText restText = new RestText();
      restText.setOriginal_url("https://original.url");
      restText.setTransformed_url("https://transformed.url");
      return restText;
    }
    return output;
  }
}
