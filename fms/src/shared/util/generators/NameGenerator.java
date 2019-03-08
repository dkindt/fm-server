package shared.util.generators;

import server.database.model.Names;

import javax.naming.Name;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static shared.util.FileHelper.readJsonFile;

public class NameGenerator {

    public enum NameType {
        FEMALE,
        MALE,
        SURNAME,
    }

    private Random random;
    private Map<NameType, Names> names;

    public NameGenerator() {

        try {
            random = new Random();
            names = new HashMap<>();
            addNames(NameType.FEMALE, "fnames.json");
            addNames(NameType.MALE, "mnames.json");
            addNames(NameType.SURNAME, "snames.json");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addNames(NameType type, String fileName) throws FileNotFoundException {
        names.put(type, getNames(fileName));
    }

    private Names getNames(String fileName) throws FileNotFoundException {
        Path path = Paths.get("json", fileName).toAbsolutePath();
        return (Names) readJsonFile(path.toString(), Names.class);
    }

    public String generateName(NameType type) {
        System.out.println(String.format("Attempting to generate %s name", type.toString()));
        int idx = random.nextInt(names.get(type).totalNames());
        String name = names.get(type).get(idx);
        name = name.substring(1, name.length() - 1);
        return name;
    }

}
