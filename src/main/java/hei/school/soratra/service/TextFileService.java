package hei.school.soratra.service;

import hei.school.soratra.file.BucketComponent;
import hei.school.soratra.model.exeption.BadRequestException;
import hei.school.soratra.model.rest.RestText;
import hei.school.soratra.repository.model.Text;
import java.io.*;
import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TextFileService {
  private final BucketComponent bucketComponent;
  private final TextService textService;

  public File getUpperCaseImage(String id, byte[] file) {
    try {
      if (file == null) {
        throw new BadRequestException("Text missing");
      }
      String fileSuffix = ".txt";
      String inputFilePrefix = id + fileSuffix;
      String outputFilePrefix = id + "-upper" + fileSuffix;
      File originalTmpFile;
      File outputTmpFile;
      try {
        originalTmpFile = File.createTempFile(inputFilePrefix, fileSuffix);
        outputTmpFile = File.createTempFile(outputFilePrefix, fileSuffix);
      } catch (IOException e) {
        throw new RuntimeException("Creation of temp file failed");
      }
      writeFileFromByteArray(file, originalTmpFile);
      File upperTextFile = convertTextToUpper(originalTmpFile, outputTmpFile);
      String originalBucketKey = getTextBucketName(inputFilePrefix);
      String upperBucketKey = getTextBucketName(outputFilePrefix);
      uploadTextFile(originalTmpFile, originalBucketKey);
      uploadTextFile(upperTextFile, upperBucketKey);
      Text toSave = new Text(id, originalBucketKey, upperBucketKey, null, null);
      textService.save(toSave);
      return bucketComponent.download(upperBucketKey);

    } catch (Exception e) {
      return null;
    }
  }

  private File convertTextToUpper(File originalFile, File outputFile) {
    try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        writer.write(line.toUpperCase());
        writer.newLine();
      }
    } catch (IOException e) {
      throw new BadRequestException("Text file invalid");
    }
    return outputFile;
  }

  private void uploadTextFile(File textFile, String bucketKey) {
    bucketComponent.upload(textFile, bucketKey);
    textFile.delete();
  }

  private String getTextBucketName(String filename) {
    return "text/" + filename;
  }

  private File writeFileFromByteArray(byte[] bytes, File file) {
    try (FileWriter writer = new FileWriter(file)) {
      writer.write(new String(bytes));
      return file;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public RestText getRestText(Text text) {
    try {
      RestText restText = new RestText();
      restText.setOriginal_url(
          bucketComponent.presign(text.getOriginalBucketKey(), Duration.ofHours(12)).toString());
      restText.setTransformed_url(
          bucketComponent.presign(text.getUpperBucketKey(), Duration.ofHours(12)).toString());
      return restText;
    } catch (Exception e) {
      return null;
    }
  }
}
