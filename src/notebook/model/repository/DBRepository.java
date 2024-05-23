package notebook.model.repository;

import java.util.List;

public interface DBRepository {
    List<String> readAll();
    void saveAll(List<String> data);
}
