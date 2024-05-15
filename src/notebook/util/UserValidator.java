package notebook.util;

import java.util.HashSet;
import java.util.Set;

public class UserValidator {
    public String validateName(String name){
        if(name.isEmpty())
            throw new RuntimeException("Вы не ввели имя или фамилию");

        return name.replaceAll(" ", "").trim();
    }
}
